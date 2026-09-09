#!/usr/bin/env python3
"""Copy newly accepted LeetCode submissions into this repository."""

from __future__ import annotations

import json
import os
import re
import subprocess
import sys
import time
import urllib.error
import urllib.request
import fcntl
from pathlib import Path


REPOSITORY = Path(__file__).resolve().parents[1]
STATE_FILE = REPOSITORY / ".leetcode-sync-state.json"
ENV_FILE = REPOSITORY / ".env"
LANGUAGE_EXTENSIONS = {
    "c": "c", "cpp": "cpp", "csharp": "cs", "dart": "dart", "golang": "go",
    "java": "java", "javascript": "js", "kotlin": "kt", "php": "php",
    "python": "py", "python3": "py", "ruby": "rb", "rust": "rs",
    "scala": "scala", "swift": "swift", "typescript": "ts",
}


def load_env(path: Path) -> None:
    if not path.exists():
        return
    for line in path.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if not line or line.startswith("#") or "=" not in line:
            continue
        key, value = line.split("=", 1)
        os.environ.setdefault(key.strip(), value.strip().strip("\"'"))


def request_json(url: str, *, method: str = "GET", payload: dict | None = None) -> dict:
    session = os.environ["LEETCODE_SESSION"]
    csrf = os.environ.get("LEETCODE_CSRF_TOKEN", "")
    headers = {
        "Cookie": f"LEETCODE_SESSION={session}; csrftoken={csrf}",
        "Referer": "https://leetcode.com/",
        "User-Agent": "leetcode-github-sync/1.0",
        "Accept": "application/json",
    }
    data = None
    if payload is not None:
        headers["Content-Type"] = "application/json"
        data = json.dumps(payload).encode()
        if csrf:
            headers["x-csrftoken"] = csrf
    request = urllib.request.Request(url, data=data, headers=headers, method=method)
    for attempt in range(3):
        try:
            with urllib.request.urlopen(request, timeout=30) as response:
                return json.load(response)
        except urllib.error.HTTPError as error:
            raise RuntimeError(f"LeetCode returned HTTP {error.code}. Your session cookie may have expired.") from error
        except urllib.error.URLError as error:
            if attempt == 2:
                raise RuntimeError("Could not reach LeetCode after three attempts. Check your internet connection and try again.") from error
            time.sleep(2 ** attempt)
    raise AssertionError("unreachable")


def graphql(query: str, variables: dict, operation_name: str) -> dict:
    response = request_json(
        "https://leetcode.com/graphql/", method="POST",
        payload={"query": query, "variables": variables, "operationName": operation_name},
    )
    if response.get("errors"):
        message = response["errors"][0].get("message", "unknown GraphQL error")
        raise RuntimeError(f"LeetCode GraphQL request failed: {message}")
    return response.get("data", {})


def strip_java_comments(source: str) -> str:
    """Remove Java line/block comments while preserving literals and text blocks."""
    result: list[str] = []
    index = 0
    state = "code"
    while index < len(source):
        char = source[index]
        next_char = source[index + 1] if index + 1 < len(source) else ""
        trio = source[index:index + 3]
        if state == "code":
            if trio == '\"\"\"':
                result.append(trio); index += 3; state = "text_block"; continue
            if char == '\"':
                result.append(char); index += 1; state = "string"; continue
            if char == "'":
                result.append(char); index += 1; state = "character"; continue
            if char == "/" and next_char == "/":
                index = source.find("\n", index)
                if index == -1: break
                result.append("\n"); index += 1; continue
            if char == "/" and next_char == "*":
                end = source.find("*/", index + 2)
                removed = source[index:] if end == -1 else source[index:end + 2]
                result.extend("\n" for _ in removed.split("\n")[1:])
                index = len(source) if end == -1 else end + 2
                continue
        elif state == "text_block" and trio == '\"\"\"':
            result.append(trio); index += 3; state = "code"; continue
        elif state in {"string", "character"}:
            if char == "\\" and index + 1 < len(source):
                result.append(source[index:index + 2]); index += 2; continue
            if (state == "string" and char == '\"') or (state == "character" and char == "'"):
                state = "code"
        result.append(char)
        index += 1
    return "\n".join(line.rstrip() for line in "".join(result).splitlines()) + "\n"


def run_git(*args: str) -> None:
    subprocess.run(["git", *args], cwd=REPOSITORY, check=True)


def has_staged_changes() -> bool:
    return subprocess.run(
        ["git", "diff", "--cached", "--quiet"], cwd=REPOSITORY, check=False
    ).returncode != 0


def push_changes() -> None:
    for attempt in range(3):
        result = subprocess.run(["git", "push", "origin", "main"], cwd=REPOSITORY, check=False)
        if result.returncode == 0:
            return
        if attempt == 2:
            result.check_returncode()
        run_git("pull", "--rebase", "origin", "main")
    raise AssertionError("unreachable")


def load_state() -> dict:
    if not STATE_FILE.exists():
        return {"processed_submission_ids": []}
    return json.loads(STATE_FILE.read_text(encoding="utf-8"))


def write_solution(submission: dict) -> str:
    submission_id = int(submission["id"])
    data = graphql(
        """query submissionDetails($submissionId: Int!) {
          submissionDetails(submissionId: $submissionId) {
            code
            lang { name }
            question { questionFrontendId title titleSlug difficulty }
          }
        }""",
        {"submissionId": submission_id}, "submissionDetails",
    )
    details = data.get("submissionDetails") or {}
    source = details.get("code")
    if not source:
        raise RuntimeError(f"LeetCode did not return source code for submission {submission_id}.")
    language = (details.get("lang") or {}).get("name", "").lower()
    extension = LANGUAGE_EXTENSIONS.get(language)
    if not extension:
        raise RuntimeError(f"Unsupported LeetCode language: {language or 'unknown'}.")
    if language == "java":
        source = strip_java_comments(source)
    question = details.get("question") or {}
    if not question.get("questionFrontendId"):
        raise RuntimeError(f"LeetCode did not return problem metadata for submission {submission_id}.")
    slug = question["titleSlug"]
    frontend_id = question["questionFrontendId"]
    directory = REPOSITORY / "problems" / f"{frontend_id}-{slug}"
    directory.mkdir(parents=True, exist_ok=True)
    solution_path = directory / f"solution.{extension}"
    solution_path.write_text(source, encoding="utf-8")
    readme = (
        f"# {frontend_id}. {question['title']}\n\n"
        f"- Difficulty: {question['difficulty']}\n"
        f"- Language: {language}\n"
        f"- [LeetCode problem](https://leetcode.com/problems/{slug}/)\n"
    )
    (directory / "README.md").write_text(readme, encoding="utf-8")
    return f"{frontend_id}. {question['title']}"


def sync() -> int:
    load_env(ENV_FILE)
    username = os.environ.get("LEETCODE_USERNAME")
    if not username or not os.environ.get("LEETCODE_SESSION"):
        print("Missing LEETCODE_USERNAME or LEETCODE_SESSION. Copy .env.example to .env and fill it in.", file=sys.stderr)
        return 2
    run_git("pull", "--rebase", "origin", "main")
    data = graphql(
        """query recentAcSubmissions($username: String!, $limit: Int!) {
          recentAcSubmissionList(username: $username, limit: $limit) {
            id title titleSlug timestamp statusDisplay lang
          }
        }""",
        {"username": username, "limit": 20}, "recentAcSubmissions",
    )
    accepted = data.get("recentAcSubmissionList", [])
    state = load_state()
    already_processed = set(state.get("processed_submission_ids", []))
    pending = [item for item in accepted if item["id"] not in already_processed]
    if not pending:
        print("No new accepted submissions.")
        return 0
    for submission in reversed(pending):
        label = write_solution(submission)
        run_git("add", "problems")
        if has_staged_changes():
            run_git("commit", "-m", f"Add LeetCode solution: {label}")
            push_changes()
        already_processed.add(submission["id"])
        state["processed_submission_ids"] = sorted(already_processed, key=int)[-500:]
        STATE_FILE.write_text(json.dumps(state, indent=2) + "\n", encoding="utf-8")
        print(f"Pushed {label}.")
        time.sleep(1)
    return 0


def main() -> int:
    lock_path = REPOSITORY / ".sync.lock"
    with lock_path.open("w", encoding="utf-8") as lock:
        try:
            fcntl.flock(lock, fcntl.LOCK_EX | fcntl.LOCK_NB)
        except BlockingIOError:
            print("A sync is already running; skipping this scheduled check.")
            return 0
        return sync()


if __name__ == "__main__":
    raise SystemExit(main())

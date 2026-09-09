# LeetCode Solutions

Accepted LeetCode submissions are synced here automatically, using a small local process on the Mac that submits them.

## Layout

Each problem is written to `problems/<LeetCode-number>-<problem-slug>/` with a `solution.<extension>` and a short README. If the accepted submission is Java, its saved solution is automatically stripped of line and block comments.

## One-time setup

1. Make sure this repository can push to GitHub without prompting. From this folder, run `git push origin main`; authenticate with GitHub if it asks. A GitHub token is not stored by this project—Git handles authentication through your existing credential manager or SSH key.
2. Copy `.env.example` to `.env` and fill in the values. `LEETCODE_USERNAME` is your public LeetCode username. `LEETCODE_SESSION` and, if available, `csrftoken` are browser cookies from an active logged-in session on `leetcode.com` (browser Developer Tools → Storage/Application → Cookies). Treat them like passwords: do not share or commit them.
3. Test it once with `python3 scripts/sync_leetcode.py`. It will import recent accepted submissions that have not yet been recorded.
4. Start the background scheduler with `./scripts/install_macos_schedule.sh`. It runs immediately and then checks once a minute while you are logged in.

## Operation and troubleshooting

- The scheduler keeps its private progress in `.leetcode-sync-state.json`, which is ignored by Git. Delete that file only if you intentionally want to re-import the recent accepted submissions.
- Logs are kept in `.sync-logs/sync.log` and `.sync-logs/error.log` (also local only).
- LeetCode session cookies eventually expire. If the error log says LeetCode returned HTTP 401 or 403, refresh the values in `.env` from a new logged-in LeetCode session, then run the sync script again.
- To stop the scheduler, run `launchctl bootout "gui/$(id -u)/com.beheraajit.leetcode-github-sync"`.

<!---LeetCode Topics Start-->
# LeetCode Topics
## Math
| Problem Name | Difficulty |
| ------- | ------- |
| [3871-count-commas-in-range-ii](https://github.com/beheraajit/LeetCode-Solutions/tree/main/3871-count-commas-in-range-ii/) | Medium |
<!---LeetCode Topics End-->
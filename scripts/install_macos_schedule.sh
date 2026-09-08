#!/bin/zsh
set -euo pipefail

repo_dir="$(cd "$(dirname "$0")/.." && pwd)"
python_path="$(command -v python3)"
label="com.beheraajit.leetcode-github-sync"
agent_path="$HOME/Library/LaunchAgents/$label.plist"
log_dir="$repo_dir/.sync-logs"
mkdir -p "$log_dir" "$HOME/Library/LaunchAgents"

cat > "$agent_path" <<PLIST
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0"><dict>
  <key>Label</key><string>$label</string>
  <key>ProgramArguments</key><array><string>$python_path</string><string>$repo_dir/scripts/sync_leetcode.py</string></array>
  <key>WorkingDirectory</key><string>$repo_dir</string>
  <key>StartInterval</key><integer>60</integer>
  <key>RunAtLoad</key><true/>
  <key>StandardOutPath</key><string>$log_dir/sync.log</string>
  <key>StandardErrorPath</key><string>$log_dir/error.log</string>
</dict></plist>
PLIST

launchctl bootout "gui/$(id -u)/$label" 2>/dev/null || true
launchctl bootstrap "gui/$(id -u)" "$agent_path"
echo "Installed. The sync runs now and then every 60 seconds."

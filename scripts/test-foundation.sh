#!/usr/bin/env bash
set -euo pipefail

project_root=$(cd "$(dirname "$0")/.." && pwd)

cd "$project_root/vhr"
mvn -pl vhr-web -am test

cd "$project_root/vhr-vue"
npm run test:e2e
npm run test:stagehand:system-basic

#!/bin/bash
set -e

source $HOME/build/build_functions.sh

trap cleanup EXIT
cleanup() {
  cd "$(git rev-parse --show-toplevel)"
  rm private.gpg || true
  rm public.gpg || true
}

set +x
setup_credentials "$1"
set -x

declare -r _PROJECT_NAME='unified-diff-parser'
declare -r _GIT_REPO='git@github.com:ScottG489/unified-diff-parser.git'
declare -r _RUN_TASK=$(jq -r .RUN_TASK <<< "$1")
declare -r _GIT_BRANCH=$(jq -r .GIT_BRANCH <<< "$1")

if [ ! -d "$_PROJECT_NAME" ]; then
  git clone --branch $_GIT_BRANCH $_GIT_REPO
fi
cd $_PROJECT_NAME

build_test

[ "$_RUN_TASK" != "deploy" ] && exit 0

set +x
upload_archives "$1"
set -x

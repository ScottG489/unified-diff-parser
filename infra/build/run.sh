#!/bin/bash
set -e

source /home/build-user/build/build_functions.sh

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

git clone $_GIT_REPO
cd $_PROJECT_NAME

build_test

set +x
upload_archives "$1"
set -x

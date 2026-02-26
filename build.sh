#!/usr/bin/env bash

readonly GIT_BRANCH=${GITHUB_HEAD_REF:-$GITHUB_REF_NAME}
readonly DOCKER_IMAGE_TAG=$([[ $GIT_BRANCH == "master" ]] && echo -n "latest" || sed 's/[^a-zA-Z0-9]/-/g' <<< "$GIT_BRANCH")
readonly IMAGE_NAME="scottg489/unified-diff-parser-build:$DOCKER_IMAGE_TAG"
readonly RUN_TASK=$1
readonly ID_RSA=$2
readonly MAVENCENTRAL_USERNAME=$3
readonly MAVENCENTRAL_PASSWORD=$4

read -r -d '' JSON_BODY <<- EOM
  {
  "RUN_TASK": "$RUN_TASK",
  "GIT_BRANCH": "$GIT_BRANCH",
  "ID_RSA": "$ID_RSA",
  "MAVENCENTRAL_USERNAME": "$MAVENCENTRAL_USERNAME",
  "MAVENCENTRAL_PASSWORD": "$MAVENCENTRAL_PASSWORD"
  }
EOM

curl -v -sS -w '\n%{http_code}' \
  --data-binary "$JSON_BODY" \
  "https://api.conjob.io/job/run?image=$IMAGE_NAME&remove=true" \
  | tee /tmp/foo \
  | sed '$d' && \
  [ "$(tail -1 /tmp/foo)" -eq 200 ]

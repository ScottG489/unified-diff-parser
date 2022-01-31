#!/usr/bin/env bash

readonly IMAGE_NAME='scottg489/unified-diff-parser-build:latest'
readonly ID_RSA=$1
readonly OSSRH_USERNAME=$2
readonly OSSRH_PASSWORD=$3

read -r -d '' JSON_BODY <<- EOM
  {
  "ID_RSA": "$ID_RSA",
  "OSSRH_USERNAME": "$OSSRH_USERNAME",
  "OSSRH_PASSWORD": "$OSSRH_PASSWORD"
  }
EOM

curl -v -sS -w '\n%{http_code}' \
  --data-binary "$JSON_BODY" \
  "https://api.conjob.io/job/run?image=$IMAGE_NAME" \
  | tee /tmp/foo \
  | sed '$d' && \
  [ "$(tail -1 /tmp/foo)" -eq 200 ]

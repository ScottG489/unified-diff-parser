#!/usr/bin/env bash

readonly IMAGE_NAME='scottg489/unified-diff-parser-build:latest'
readonly ID_RSA=$1
readonly MAVENCENTRAL_USERNAME=$2
readonly MAVENCENTRAL_PASSWORD=$3

read -r -d '' JSON_BODY <<- EOM
  {
  "ID_RSA": "$ID_RSA",
  "MAVENCENTRAL_USERNAME": "$MAVENCENTRAL_USERNAME",
  "MAVENCENTRAL_PASSWORD": "$MAVENCENTRAL_PASSWORD"
  }
EOM

curl -v -sS -w '\n%{http_code}' \
  --data-binary "$JSON_BODY" \
  "https://api.conjob.io/job/run?image=$IMAGE_NAME" \
  | tee /tmp/foo \
  | sed '$d' && \
  [ "$(tail -1 /tmp/foo)" -eq 200 ]

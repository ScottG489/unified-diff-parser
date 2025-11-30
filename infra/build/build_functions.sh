#!/bin/bash
set -e

get_git_root_dir() {
  echo -n "$(git rev-parse --show-toplevel)"
}

setup_credentials() {
  set +x
  local ID_RSA_CONTENTS

  readonly ID_RSA_CONTENTS=$(echo -n "$1" | jq -r .ID_RSA | base64 --decode)
  [[ -n $ID_RSA_CONTENTS ]]

  printf -- "$ID_RSA_CONTENTS" > $HOME/.ssh/id_rsa

  chmod 400 $HOME/.ssh/id_rsa
}

build_test() {
  local ROOT_DIR
  readonly ROOT_DIR=$(get_git_root_dir)
  cd "$ROOT_DIR"

  ./gradlew --info clean build
}

upload_archives() {
  set +x
  declare -rx JRELEASER_GPG_PASSPHRASE=$(tr -dc A-Za-z0-9 </dev/urandom | head -c 16 ; echo -n '')
  declare -rx JRELEASER_MAVENCENTRAL_USERNAME=$(echo -n "$1" | jq -r .MAVENCENTRAL_USERNAME | base64 --decode)
  declare -rx JRELEASER_MAVENCENTRAL_PASSWORD=$(echo -n "$1" | jq -r .MAVENCENTRAL_PASSWORD | base64 --decode)

  declare -rx JRELEASER_DEPLOY_MAVEN_NEXUS2_SNAPSHOT_DEPLOY_USERNAME=$JRELEASER_MAVENCENTRAL_USERNAME
  declare -rx JRELEASER_DEPLOY_MAVEN_NEXUS2_SNAPSHOT_DEPLOY_PASSWORD=$JRELEASER_MAVENCENTRAL_PASSWORD

  gpg --batch --gen-key <<EOF
Key-Type: 1
Name-Real: Scott Giminiani
Name-Email: scottg489@gmail.com
Passphrase: $JRELEASER_GPG_PASSPHRASE
EOF

  gpg --armor --batch --pinentry-mode loopback --export --passphrase "$JRELEASER_GPG_PASSPHRASE" -o public.gpg
  gpg --armor --batch --pinentry-mode loopback --export-secret-keys --passphrase "$JRELEASER_GPG_PASSPHRASE" -o private.gpg

  readonly GPG_PUB_KEY=$(gpg --list-keys --with-colons | grep '^pub' | cut -d ':' -f 5)

  gpg --keyserver keyserver.ubuntu.com --send-keys "$GPG_PUB_KEY"
  set -x
  sleep 30  # Wait for key to be fully available on keyserver

  ./gradlew publish jreleaserDeploy
}

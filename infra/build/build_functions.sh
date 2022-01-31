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

  printf -- "$ID_RSA_CONTENTS" >/home/build-user/.ssh/id_rsa

  chmod 400 /home/build-user/.ssh/id_rsa
}

build_test() {
  local ROOT_DIR
  readonly ROOT_DIR=$(get_git_root_dir)
  cd "$ROOT_DIR"

  ./gradlew --info build -x signArchives
}

upload_archives() {
  set +x
  readonly GPG_KEY_PASSWORD=$(tr -dc A-Za-z0-9 </dev/urandom | head -c 16 ; echo -n '')
  readonly OSSRH_USERNAME=$(echo -n "$1" | jq -r .OSSRH_USERNAME | base64 --decode)
  readonly OSSRH_PASSWORD=$(echo -n "$1" | jq -r .OSSRH_PASSWORD | base64 --decode)

  gpg --batch --gen-key <<EOF
Key-Type: 1
Name-Real: Scott Giminiani
Name-Email: scottg489@gmail.com
Passphrase: $GPG_KEY_PASSWORD
EOF

  gpg --batch --pinentry-mode loopback --export-secret-keys --passphrase "$GPG_KEY_PASSWORD" -o secring.gpg

  readonly GPG_PUB_KEY=$(gpg --list-keys --with-colons | grep '^pub' | cut -d ':' -f 5)
  readonly GPG_PUB_KEY_SHORT=$(echo -n "${GPG_PUB_KEY: -8}")

  export OSSRH_USERNAME
  export OSSRH_PASSWORD

  ./gradlew uploadArchives -Psigning.secretKeyRingFile=secring.gpg -Psigning.password="$GPG_KEY_PASSWORD" -Psigning.keyId="$GPG_PUB_KEY_SHORT"
  gpg --keyserver keys.openpgp.org --send-keys "$GPG_PUB_KEY"

  set -x
}

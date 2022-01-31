FROM ubuntu:20.04

RUN apt-get update && \
  DEBIAN_FRONTEND=noninteractive apt-get install -y \
  git \
  openjdk-11-jdk \
  jq \
  gnupg

RUN useradd -m -u 1000 -s /usr/bin/bash build-user
USER build-user

RUN mkdir /home/build-user/.ssh
COPY known_hosts /home/build-user/.ssh/known_hosts

RUN mkdir -p /home/build-user/build
COPY run.sh /home/build-user/build/run.sh
COPY build_functions.sh /home/build-user/build/build_functions.sh

WORKDIR /home/build-user/build
ENTRYPOINT ["./run.sh"]

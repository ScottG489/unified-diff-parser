FROM ubuntu:26.04@sha256:5e275723f82c67e387ba9e3c24baa0abdcb268917f276a0561c97bef9450d0b4

ARG BUILD_USER=ubuntu
ENV HOME=/home/${BUILD_USER}

RUN apt-get update && \
  DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
  git=1:2.53.0-1ubuntu1 \
  openssh-client=1:10.2p1-2ubuntu3 \
  openjdk-17-jdk=17.0.18+8-1 \
  jq=1.8.1-4ubuntu2 \
  gnupg=2.4.8-4ubuntu3 \
  && apt-get clean \
  && rm -rf /var/lib/apt/lists/*

USER ${BUILD_USER}

RUN mkdir -p ${HOME}/.ssh \
  && mkdir -p ${HOME}/build

COPY known_hosts ${HOME}/.ssh/known_hosts
COPY run.sh ${HOME}/build/run.sh
COPY build_functions.sh ${HOME}/build/build_functions.sh

WORKDIR ${HOME}/build
ENTRYPOINT ["./run.sh"]

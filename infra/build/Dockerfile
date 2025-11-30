FROM ubuntu:24.04@sha256:c35e29c9450151419d9448b0fd75374fec4fff364a27f176fb458d472dfc9e54

ARG BUILD_USER=ubuntu
ENV HOME=/home/${BUILD_USER}

RUN apt-get update && \
  DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
  git=1:2.43.0-1ubuntu7 \
  openjdk-11-jdk=11.0.29+7-1ubuntu1~24.04 \
  jq=1.7.1-3ubuntu0.24.04.1 \
  gnupg=2.4.4-2ubuntu17.3 \
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

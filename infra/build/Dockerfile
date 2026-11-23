FROM ubuntu:24.04@sha256:4fbb8e6a8395de5a7550b33509421a2bafbc0aab6c06ba2cef9ebffbc7092d90

ARG BUILD_USER=ubuntu
ENV HOME=/home/${BUILD_USER}

# Ubuntu packages come from this point-in-time snapshot of the archive instead of
# per-package version pins, which stop building as soon as Ubuntu supersedes a version.
# Renovate bumps it through the ubuntu-snapshot datasource in ScottG489/renovate-config.
ARG UBUNTU_SNAPSHOT=20261120T050039Z
RUN echo "APT::Snapshot \"${UBUNTU_SNAPSHOT}\";" > /etc/apt/apt.conf.d/50snapshot

# snapshot.ubuntu.com is HTTPS-only and the base image has no CA bundle yet, so the
# bootstrap skips TLS peer verification. apt still checks every index and package
# against the Ubuntu archive key, same as the image's default http:// sources.
RUN apt-get -o Acquire::https::Verify-Peer=false update && \
  apt-get -o Acquire::https::Verify-Peer=false install -y --no-install-recommends ca-certificates && \
  DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends \
  git \
  openssh-client \
  openjdk-17-jdk \
  jq \
  gnupg \
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

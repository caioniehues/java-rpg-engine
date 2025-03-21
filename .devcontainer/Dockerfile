# Use Eclipse Temurin as base image with Java 21
FROM eclipse-temurin:21-jdk-jammy

# Install development tools
RUN apt-get update && export DEBIAN_FRONTEND=noninteractive \
    && apt-get -y install git curl unzip zip \
    && apt-get clean -y && rm -rf /var/lib/apt/lists/*

# Install Maven (instead of using wrapper to ensure latest version)
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz | tar xzf - -C /opt \
    && ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/local/bin/mvn

# Set environment variables
ENV JAVA_HOME=/opt/java/openjdk \
    MAVEN_HOME=/opt/apache-maven-3.9.6 \
    MAVEN_CONFIG=/root/.m2

# Create source directory
WORKDIR /workspace/app

# Add a non-root user 'vscode' for development
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID
RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && mkdir -p /home/$USERNAME/.m2 \
    && chown -R $USERNAME:$USERNAME /home/$USERNAME

# Switch to non-root user
USER $USERNAME 
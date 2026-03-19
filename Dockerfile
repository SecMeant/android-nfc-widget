FROM fedora:40

# Install dependencies and Java 17
RUN dnf update -y && \
    dnf install -y \
        java-17-openjdk-devel \
        wget \
        unzip \
        git \
        findutils \
        which \
        && dnf clean all

# Set environment variables for Java and Android SDK
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk
ENV ANDROID_HOME=/opt/android-sdk
ENV ANDROID_SDK_ROOT=/opt/android-sdk
ENV PATH=${PATH}:${ANDROID_HOME}/cmdline-tools/latest/bin:${ANDROID_HOME}/platform-tools

# Download and install Android Command Line Tools
# Using version 11076708 (latest as of early 2024)
ARG CMDLINE_TOOLS_VERSION=11076708
RUN mkdir -p ${ANDROID_HOME}/cmdline-tools && \
    wget -q https://dl.google.com/android/repository/commandlinetools-linux-${CMDLINE_TOOLS_VERSION}_latest.zip -O /tmp/cmdline-tools.zip && \
    unzip -q /tmp/cmdline-tools.zip -d ${ANDROID_HOME}/cmdline-tools && \
    mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest && \
    rm /tmp/cmdline-tools.zip

# Accept Android SDK licenses
RUN yes | sdkmanager --licenses

# Install required Android SDK components (platform-tools and compileSdk 35)
RUN sdkmanager "platform-tools" "platforms;android-35"

# Set up the working directory
WORKDIR /app

# Copy the project files
COPY . .

# Ensure gradlew is executable
RUN chmod +x gradlew

# Default command to build the debug APK
CMD ["./gradlew", "assembleDebug"]

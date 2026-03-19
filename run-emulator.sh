#!/bin/bash

export ANDROID_HOME=~/Android/Sdk
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
export PATH=$JAVA_HOME/bin:$ANDROID_HOME/emulator:$ANDROID_HOME/platform-tools:$PATH

emulator -avd NfcTest_API35 -no-boot-anim -gpu swiftshader_indirect

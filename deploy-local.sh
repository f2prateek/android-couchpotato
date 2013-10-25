#!/bin/bash

./gradlew assemble

set -e

APK=`\ls CouchPotato/build/apk/*debug*.apk`

adb uninstall com.f2prateek.couchpotato.debug
adb install $APK
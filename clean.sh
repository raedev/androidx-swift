#!/bin/bash
echo "clean swift cache"
PACKAGE_NAME=com.github.raedev/swift/
rm -rf ~/.gradle/caches/modules-2/files-2.1/$PACKAGE_NAME
rm -rf ~/.gradle/caches/modules-2/metadata-2.96/descriptors/$PACKAGE_NAME
echo 'clean swift success!'
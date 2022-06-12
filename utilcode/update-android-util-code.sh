#!/bin/zsh
DIR=$(dirname $(pwd))
echo "current dir is $DIR"
# 更新 https://github.com/Blankj/AndroidUtilCode 源代码库到当前项目
# 删除当前目录
#rm -rf AndroidUtilCode
# 拉代码
# git clone git@github.com:Blankj/AndroidUtilCode.git --depth=1
# 更新源码
javac CopyUtilCode.java
java CopyUtilCode  $(pwd)/AndroidUtilCode/lib/utilcode/src/main/java/com/blankj/utilcode/util $DIR/kotlin/src/main/java/androidx/swift/util
rm -rf CopyUtilCode.class
echo ok...
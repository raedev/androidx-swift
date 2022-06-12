import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author RAE
 * @date 2022/06/12
 * Copyright (c) https://github.com/raedev All rights reserved.
 */
@SuppressWarnings("ConstantConditions")
public class CopyUtilCode {

    public static void main(String[] args) {
        String source = args[0];//"/Users/rae/Project/github/android-component/AndroidUtilCode/lib/utilcode/src/main/java/com/blankj/utilcode/util";
        String target = args[1];//"/Users/rae/Project/github/android-component/kotlin/src/main/java/androidx/swift/util";
        if (!new File(source).exists()) {
            throw new NullPointerException("源代码不存在：" + source);
        }

        if (!new File(target).exists()) {
            throw new NullPointerException("输出目录不存在：" + target);
        }

        // 排除列表
        List<String> excludeList = Arrays.asList("DialogUtils.java", "ToastUtils.java", "SnackbarUtils.java");
        // 静态变量包
        File constantDir = new File(target, "constant");
        if (!constantDir.exists()) {
            constantDir.mkdir();
        }
        for (File file : new File(new File(source).getParentFile(), "constant").listFiles()) {
            String content = read(file)
                    .replace("package com.blankj.utilcode.constant;", "package androidx.swift.util.constant;")
//                    .replace("import java.lang.annotation.Retention;", "")
//                    .replace("import java.lang.annotation.RetentionPolicy;", "")
//                    .replaceFirst("@Retention.*", "")
                    ;
            write(new File(constantDir, file.getName()), content);
        }

        // 遍历
        for (File file : Objects.requireNonNull(new File(source).listFiles())) {
            String name = file.getName();
            File targetFile = new File(target, name);
            System.out.println(name);
            if (targetFile.exists()) {
                targetFile.delete();
            }
            if (excludeList.contains(name))
                continue;
            String content = read(file);
            if (content == null)
                continue;
            content = content.replace("package com.blankj.utilcode.util;", "package androidx.swift.util;")
                    .replace("com.blankj.utilcode.constant", "androidx.swift.util.constant")
                    .replace("com.blankj.utilcode.util", "androidx.swift.util")
//                    .replace("import java.lang.annotation.Retention;", "")
//                    .replace("import java.lang.annotation.RetentionPolicy;", "")
//                    .replace("import java.lang.annotation.ElementType;", "")
//                    .replace("import java.lang.annotation.Target;", "")
//                    .replaceFirst("@Retention.*", "")
            ;
            if (name.startsWith("UtilsBridge")) {
                content = content
                        .replace("import android.view.View;", "import android.view.View;\nimport android.widget.Toast;")
                        .replace("ToastUtils.showShort(text);", "Toast.makeText(Utils.getApp(), text, Toast.LENGTH_SHORT).show();")
                        .replace("ToastUtils.cancel();", "// nothing");
            }
            write(targetFile, content);
        }
    }


    private static String read(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            byte[] bytes = stream.readAllBytes();
            stream.close();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String write(File file, String content) {
        try {
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(content.getBytes(StandardCharsets.UTF_8));
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

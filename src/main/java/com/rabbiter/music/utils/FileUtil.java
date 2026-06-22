package com.rabbiter.music.utils;

import java.io.File;
import java.io.FileOutputStream;

public class FileUtil {

    /**
     * 淇濆瓨鏂囦欢鍒版湰鍦?
     */
    public static void uploadFile(byte[] file, String filePath, String fileName) throws Exception {
        File targetDir = new File(filePath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File target = new File(targetDir, fileName);
        try (FileOutputStream out = new FileOutputStream(target)) {
            out.write(file);
            out.flush();
        }
    }
}



package com.effective.router.compiler.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by yummyLau on 2018/8/16.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */

public class FileUtils {


    /**
     * @param fileName
     */
    public static boolean createFile(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param fileName
     * @param content
     */
    public static void writeStringToFile(String fileName, String content, boolean append) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(fileName, append), "UTF-8"));
            out.write(content);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

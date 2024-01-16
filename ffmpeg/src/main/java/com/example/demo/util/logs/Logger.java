package com.example.demo.util.logs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author libiao
 * @date 2021/12/16
 */
public class Logger {

    private static final String logPath = "logs\\";

    public static void info(String content) {
        writer("Info", content);
    }

    private static File getFile() {
        // String path = Logger.class.getResource("").getPath();
        File file = new File(logPath);
        if (!file.exists()) {
            file.mkdir();
        }
        return new File(logPath + new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".log");
    }

    private static void writer(String type, String content) {
        File file = getFile();
        try {
            // append=true,每次从内容的尾部写入，不会覆写以前的内容
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            String str = type + "  " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "  :");
            fileOutputStream.write(str.getBytes("GBK"));
            fileOutputStream.write(content.getBytes("GBK"));
            fileOutputStream.write("\r\n".getBytes("GBK"));
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

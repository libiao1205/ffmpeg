package com.example.demo.util.merge;

import java.io.*;
import java.text.SimpleDateFormat;

/**
 * @author libiao
 * @date 2021/11/16
 */
public class VideoJoin {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static void main(String[] args) {
        VideoJoin videoJoin = new VideoJoin();
        videoJoin.mergeVideo("D:\\video\\2fb06d1\\mergerList.txt", "D:\\video\\2fb06d1\\output.mp4");
    }

    public int mergeVideo(String inputPath, String outputFile) {
        // D:\
        StringBuilder builder = new StringBuilder();
        builder.append("ffmpeg -f concat -safe 0 -i ")
                .append(inputPath + " ")
                .append("-c copy ")
                .append(outputFile);
        return execCommand(builder.toString());
    }

    private Integer execCommand(String command) {
        Process proc = null;
        BufferedReader stdout;
        InputStream inputStream = null;
        try {
//            ProcessBuilder builder = new ProcessBuilder();
//            builder.command(command);
//            builder.redirectErrorStream(true);
            proc = Runtime.getRuntime().exec(command);
            inputStream = proc.getErrorStream();
            stdout = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = stdout.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        int exitValue = proc.exitValue();
        System.out.println(exitValue);
        return exitValue;
    }

}

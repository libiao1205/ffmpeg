package com.example.demo.util.watermark;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.logs.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author libiao
 * @date 2021/11/25
 */
public class VideoTest {

    /**
     * 是否退出日志打印
     */
    private boolean exit = false;

    /**
     * 需要打印的日志信息
     */
    private String log = "";

    public static void main(String[] args) {
        VideoTest videoTest = new VideoTest();
        // videoTest.execMethod("D\\\\:/video/logo.png", "C:\\Users\\Admin\\Desktop\\piantou", "C:\\Users\\Admin\\Desktop\\001");
        videoTest.execMethod(args[0], args[1], args[2]);
    }

    public void execMethod(String logoPath, String inputPath, String outputPath){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        File file = new File(inputPath);
        List<String> inputFiles = new ArrayList<>();
        List<String> outputFiles = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        this.method(file, inputFiles, outputFiles, fileNames, inputPath, outputPath);
        int successCount = 0;
        int videoSum = inputFiles.size();
        this.printAwait();
        String printLog = "";
        try {
            for (int i = 0; i < videoSum; i++) {
                String inputFile = inputFiles.get(i);
                String outputFile = outputFiles.get(i);
                String fileName = fileNames.get(i);
                int bitRate = this.getVideoDetail(inputFile);
                if (bitRate == 0) {
                    this.log = sdf.format(new Date()) + "  " + fileName + " 水印添加失败 ";
                    continue;
                }
                int success = this.videoWatermark(logoPath, inputFile, outputFile, bitRate);
                if (success == 0) {
                    printLog = sdf.format(new Date()) + "  " + fileName + " 水印添加成功 ";
                    successCount += 1;
                } else {
                    printLog = sdf.format(new Date()) + "  " + fileName + " 水印添加失败 ";
                }
                if (i < (videoSum - 1)) {
                    this.log = printLog;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            exit = true;
        }
        this.log = printLog + "\n" + sdf.format(new Date()) + "  " + successCount + "个成功，" + (videoSum - successCount) + "个失败";
        // 执行完毕，退出日志打印
        exit = true;
    }

    /**
     * 打印日志
     */
    private void printAwait(){
        new Thread() {
            @Override
            public void run() {
                //初始用
                System.out.printf("%s", "正在添加水印");
                String str = "";
                for (int i = 1; i < 8; i++) {
                    if (exit) {
                        // \r将光标定位在该行的首个字符
                        System.out.printf("%s", "\r添加水印结束      ");
                        break;
                    }
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!"".equals(log)) {
                        System.out.printf("%s", "\r                  ");
                        System.out.printf("%s", "\r" + log + "\n");
                        System.out.printf("%s", "正在添加水印");
                        log = "";
                        i = 0;
                        str = "";
                    }
                    if (i  == 7) {
                        // \b光标向左移动
                        str = "\b\b\b\b\b\b";
                        System.out.printf(str + "%s", "      ");
                        i = 0;
                    } else if (i == 1 && !"".equals(str)){
                        System.out.printf("\b\b\b\b\b\b%s", ".");
                    } else {
                        System.out.printf("%s", ".");
                    }
                }
            }
        }.start();
    }

    /**
     * 视频添加水印
     * @param logoPath logo地址
     * @param inputPath 需要加水印的视频地址
     * @param outputFile 加好水印后视频输出地址
     * @param bitRate 视频比特率
     * @return int 返回结果 0：成功，1：失败
     * @author libiao
     */
    private int videoWatermark(String logoPath, String inputPath, String outputFile, int bitRate) {
        StringBuilder builder = new StringBuilder();
        builder.append("ffmpeg -i ")
                .append(inputPath)
                .append(" -acodec copy -b:v ")
                .append(bitRate)
                .append("k -vf \"movie=")
                .append(logoPath)
                .append("[watermark];[in][watermark] overlay=main_w-overlay_w-40:32 [out] \" ")
                .append(outputFile)
                .append(" -y");
        StringBuilder stringBuilder = new StringBuilder();
        int exitValue = execCommand(builder.toString(), stringBuilder, 1);
        Logger.info("exec start: " + builder.toString());
        Logger.info("exec result: " + stringBuilder.toString());
        return exitValue;
    }

    /**
     * 获取视频比特率
     * @param inputPath 视频地址
     * @return int 比特率
     * @author libiao
     */
    private int getVideoDetail(String inputPath) {
        StringBuilder builder = new StringBuilder();
        builder.append("ffprobe -print_format json  -show_format -show_streams -i ")
                .append(inputPath);
        StringBuilder stringBuilder = new StringBuilder();
        execCommand(builder.toString(), stringBuilder, 2);
        Logger.info("exec start: " + builder.toString());
        Logger.info( "exec result: " + stringBuilder.toString());
        JSONObject json = JSONObject.parseObject(stringBuilder.toString());
        VideoInfo videoInfo = JSONObject.toJavaObject(json, VideoInfo.class);
        if (Objects.isNull(videoInfo) || Objects.isNull(videoInfo.getFormat())) {
            return 0;
        }
        int bitRate = Integer.parseInt(videoInfo.getFormat().getBit_rate());
        return bitRate/1000;
    }

    /**
     * 执行命令
     * @param command 命令
     * @param outLog 日志
     * @param streamType 输出流类型，1：异常输出，2：普通输出
     * @return 执行结果 0：成功，1：失败
     * @author libiao
     */
    private Integer execCommand(String command, StringBuilder outLog, int streamType) {
        Process proc = null;
        BufferedReader stdout;
        InputStream inputStream = null;
        try {
            proc = Runtime.getRuntime().exec(command);
            if (streamType == 1) {
                inputStream = proc.getErrorStream();
            } else {
                inputStream = proc.getInputStream();
            }
            stdout = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            String line;
            while ((line = stdout.readLine()) != null) {
                outLog.append(line).append("\r\n");
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
        return proc.exitValue();
    }

    /**
     * 循环读取文件夹中所有视频路径
     * @param file 文件
     * @param inputPaths 拼接视频加水印前的输入地址
     * @param outputPaths 拼接视频加水印后的输出地址
     * @param fileNames 视频名称
     * @param inputPath 视频输入地址
     * @param outputPath 视频输出地址
     * @return void
     * @author libiao
     */
    private void method(File file, List<String> inputPaths, List<String> outputPaths, List<String> fileNames, String inputPath, String outputPath) {
        File[] FList = file.listFiles();
        for (int i = 0; i < FList.length; i++) {
            if (FList[i].isDirectory()==true) {
                method(FList[i], inputPaths, outputPaths, fileNames, inputPath, outputPath);
            } else {
                inputPaths.add(inputPath + "\\" + FList[i].getName());
                outputPaths.add(outputPath + "\\" + FList[i].getName());
                fileNames.add(FList[i].getName());
            }
        }
    }
}

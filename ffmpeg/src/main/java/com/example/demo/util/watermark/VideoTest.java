package com.example.demo.util.watermark;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.logs.Logger;
import org.springframework.util.StringUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

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

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 15, 1000 * 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), new ThreadFactoryInfo("customThread-"));

    public static void main(String[] args) {
        VideoTest videoTest = new VideoTest();
        /*for (int i = 0; i < 5; i++){
            videoTest.downVideo("https://video-shell.toant.top/video/encode/huiketang/202191/ac7662f/9835e99.mp4", "D:\\video" + i + ".mp4");
        }*/

        //videoTest.execMethod("D\\\\:/video/logo.png", "D:\\video\\inputPath", "D:\\video\\outputPath", "50", "32");
        //videoTest.execMethod(args[0], args[1], args[2], args[3], args[4]);

    }

    public void execMethod(String logoPath, String inputPath, String outputPath, String height, String width) {
        int H = StringUtils.isEmpty(height) ? 50 : Integer.parseInt(height);
        int W = StringUtils.isEmpty(height) ? 32 : Integer.parseInt(width);
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
                int success = this.videoWatermark(logoPath, inputFile, outputFile, bitRate, H, W);
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
    private void printAwait() {
        threadPoolExecutor.execute(() -> {
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
                if (i == 7) {
                    // \b光标向左移动
                    str = "\b\b\b\b\b\b";
                    System.out.printf(str + "%s", "      ");
                    i = 0;
                } else if (i == 1 && !"".equals(str)) {
                    System.out.printf("\b\b\b\b\b\b%s", ".");
                } else {
                    System.out.printf("%s", ".");
                }
            }
        });
        threadPoolExecutor.shutdown();
    }

    /**
     * 视频添加水印
     *
     * @param logoPath   logo地址
     * @param inputPath  需要加水印的视频地址
     * @param outputFile 加好水印后视频输出地址
     * @param bitRate    视频比特率
     * @return int 返回结果 0：成功，1：失败
     * @author libiao
     */
    private int videoWatermark(String logoPath, String inputPath, String outputFile, int bitRate, int H, int W) {
        StringBuilder builder = new StringBuilder();
        builder.append("ffmpeg -i ")
                .append(inputPath)
                .append(" -acodec copy -b:v ")
                .append(bitRate)
                .append("k -vf \"movie=")
                .append(logoPath)
                .append("[watermark];[in][watermark] overlay=main_w-overlay_w-" + H + ":" + W + " [out] \" ")
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
     *
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
        Logger.info("exec result: " + stringBuilder.toString());
        JSONObject json = JSONObject.parseObject(stringBuilder.toString());
        VideoInfo videoInfo = JSONObject.toJavaObject(json, VideoInfo.class);
        if (Objects.isNull(videoInfo) || Objects.isNull(videoInfo.getFormat())) {
            return 0;
        }
        int bitRate = Integer.parseInt(videoInfo.getFormat().getBit_rate());
        return bitRate / 1000;
    }

    /**
     * 执行命令
     *
     * @param command    命令
     * @param outLog     日志
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
     *
     * @param file        文件
     * @param inputPaths  拼接视频加水印前的输入地址
     * @param outputPaths 拼接视频加水印后的输出地址
     * @param fileNames   视频名称
     * @param inputPath   视频输入地址
     * @param outputPath  视频输出地址
     * @return void
     * @author libiao
     */
    private void method(File file, List<String> inputPaths, List<String> outputPaths, List<String> fileNames, String inputPath, String outputPath) {
        File[] fList = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(fList).length; i++) {
            if (fList[i].isDirectory()) {
                method(fList[i], inputPaths, outputPaths, fileNames, inputPath, outputPath);
            } else {
                inputPaths.add(inputPath + "\\" + fList[i].getName());
                outputPaths.add(outputPath + "\\" + fList[i].getName());
                fileNames.add(fList[i].getName());
            }
        }
    }

    /**
     * 下载视频
     *
     * @param videoUrl     视频网络地址
     * @param downloadPath 视频保存地址
     */
    public void downVideo(String videoUrl, String downloadPath) {
        threadPoolExecutor.execute(() -> {


            HttpURLConnection connection = null;
            InputStream inputStream = null;
            RandomAccessFile randomAccessFile = null;
            boolean re;
            try {

                URL url = new URL(videoUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Range", "bytes=0-");
                connection.connect();
                if (connection.getResponseCode() / 100 != 2) {
                    System.out.println("连接失败...");
                    return;
                }
                inputStream = connection.getInputStream();
                int downloaded = 0;
                int fileSize = connection.getContentLength();
                randomAccessFile = new RandomAccessFile(downloadPath, "rw");
                while (downloaded < fileSize) {
                    byte[] buffer = null;
                    if (fileSize - downloaded >= 1000000) {
                        buffer = new byte[1000000];
                    } else {
                        buffer = new byte[fileSize - downloaded];
                    }
                    int read = -1;
                    int currentDownload = 0;
                    long startTime = System.currentTimeMillis();
                    while (currentDownload < buffer.length) {
                        read = inputStream.read();
                        buffer[currentDownload++] = (byte) read;
                    }
                    long endTime = System.currentTimeMillis();
                    double speed = 0.0;
                    if (endTime - startTime > 0) {
                        speed = currentDownload / 1024.0 / ((double) (endTime - startTime) / 1000);
                    }
                    randomAccessFile.write(buffer);
                    downloaded += currentDownload;
                    randomAccessFile.seek(downloaded);
                    System.out.printf(downloadPath + "下载了进度:%.2f%%,下载速度：%.1fkb/s(%.1fM/s)%n", downloaded * 1.0 / fileSize * 10000 / 100,
                            speed, speed / 1000);
                }
                re = true;
                return;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                re = false;
                return;
            } catch (IOException e) {
                e.printStackTrace();
                re = false;
                return;
            } finally {
                try {
                    connection.disconnect();
                    inputStream.close();
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

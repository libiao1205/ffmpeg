package com.example.demo.util.watermark;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.manager.websocket.WebSocketManager;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author libiao
 * @date 2023/7/4
 */

@Component
public class ImgCompressImg {

    @Autowired
    private WebSocketManager webSocketManager;

    public static void main(String[] args) {
        ImgCompressImg imgCompressImg = new ImgCompressImg();
        int height = Integer.parseInt(args[2]);
        int width = Integer.parseInt(args[3]);
        imgCompressImg.start(args[0], args[1], width, height, "", 0);
    }

    public void start(String inputPath, String outputPath, int width, int height, String customPrefix, int customNum) {
        ImgCompressImg imgCompressImg = new ImgCompressImg();
        List<String> inputPaths = new ArrayList<>();
        List<String> outputPaths = new ArrayList<>();
        File file = new File(inputPath);
        imgCompressImg.method(file, inputPaths, outputPaths, inputPath, outputPath, customPrefix, customNum);
        int sum = inputPaths.size();
        int successCount = 0;
        int failCount = 0;
        JSONObject result = new JSONObject();
        for (int i = 0; i < sum; i++) {
            String imgName = inputPaths.get(i);
            int state = imgCompressImg.compressImg(imgName, outputPaths.get(i), height, width);
            System.out.println(imgName);
            if (state == 1) {
                successCount += 1;
            } else if(state == 0) {
                failCount += 1;
            }
            result.put("inputPath", imgName);
            result.put("outputPath", outputPaths.get(i));
            int percentage = 100 * (successCount + failCount) / sum;
            result.put("percentage", percentage);
            result.put("state", state);
            webSocketManager.pushMessageFromRedis(result);
        }
    }

    private int compressImg(String imgName, String outFileName, int h, int w) {

        try {
            File file = new File(imgName);
            long fileSize = file.length() / 1024;
            /*if (fileSize < 500) {
                return;
            }*/
            //图片所在路径
            BufferedImage templateImage = ImageIO.read(file);

            //原始图片的长度和宽度
            int height = templateImage.getHeight();
            int width = templateImage.getWidth();

            //压缩之后的长度和宽度
            int doWithHeight = h <= 0 ? height : Math.min(height, h);
            int doWithWidth = w <= 0 ? width : Math.min(width, w);

            BufferedImage finalImage = new BufferedImage(doWithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);
            finalImage.getGraphics().drawImage(templateImage.getScaledInstance(doWithWidth, doWithHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

            //图片输出路径，以及图片名
            OutputStream outputStream = new FileOutputStream(outFileName);
            int index = outFileName.lastIndexOf(".");
            if (index < 1) {
                return 0;
            }
            ImageIO.write(finalImage, outFileName.substring(index + 1), outputStream);
            outputStream.flush();
            outputStream.close();
            finalImage.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

    /**
     * 循环读取文件夹中所有图片路径
     *
     * @param file        文件
     * @param inputPaths  拼接视频加水印前的输入地址
     * @param outputPaths 拼接视频加水印后的输出地址77
     * @param inputPath   视频输入地址
     * @param outputPath  视频输出地址
     * @return void
     * @author libiao
     */
    private void method(File file, List<String> inputPaths, List<String> outputPaths, String inputPath, String outputPath, String customPrefix, int customNum) {
        File[] fList = file.listFiles();
        int j = 13278;
        for (int i = 0; i < Objects.requireNonNull(fList).length; i++) {
            File f = fList[i];
            String imgName = f.getName();
            String outImgName;
            int index = imgName.lastIndexOf(".");
            if (!StringUtils.isEmpty(customPrefix) && index > 0) {
                outImgName = customPrefix + customNum + imgName.substring(index);
                customNum++;
            } else {
                outImgName = imgName;
            }
            if (fList[i].isDirectory()) {
                method(f, inputPaths, outputPaths, inputPath, outputPath, customPrefix, customNum);
            } else {
                inputPaths.add(inputPath + "\\" + imgName);
                outputPaths.add(outputPath + "\\" + outImgName);
            }
            j++;
        }
    }
}

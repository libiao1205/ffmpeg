package com.example.demo.util.watermark;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author libiao
 * @date 2023/7/4
 */
public class ImgCompressImg {

    public void compressImg(String imgName, String outFileName) {

        try {
            File file = new File(imgName);
            long fileSize = file.length() / 1024;
            if (fileSize < 500) {
                return;
            }
            System.out.println(fileSize + "KB");
            //图片所在路径
            BufferedImage templateImage = ImageIO.read(file);

            //原始图片的长度和宽度
            int height = templateImage.getHeight();
            int width = templateImage.getWidth();

            //压缩之后的长度和宽度
            int doWithHeight = Math.min(height, 580);
            int dowithWidth = Math.min(width, 580);

            BufferedImage finalImage = new BufferedImage(dowithWidth, doWithHeight, BufferedImage.TYPE_INT_RGB);

            finalImage.getGraphics().drawImage(templateImage.getScaledInstance(dowithWidth, doWithHeight, java.awt.Image.SCALE_SMOOTH), 0, 0, null);

            //图片输出路径，以及图片名
            FileOutputStream fileOutputStream = new FileOutputStream(outFileName);
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fileOutputStream);
            encoder.encode(finalImage);
            fileOutputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ImgCompressImg imgCompressImg = new ImgCompressImg();
        List<String> inputPaths = new ArrayList<>();
        List<String> outputPaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        File file = new File("D:\\max_imgs");
        imgCompressImg.method(file, inputPaths, outputPaths, fileNames, "D:\\max_imgs", "D:\\out_imgs");
        for (int i = 0; i < inputPaths.size(); i++) {
            //imgCompressImg.compressImg(inputPaths.get(i), outputPaths.get(i));
            System.out.println(inputPaths.get(i));
        }
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
}

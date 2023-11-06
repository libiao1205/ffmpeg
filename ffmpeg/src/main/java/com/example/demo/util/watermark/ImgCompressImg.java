package com.example.demo.util.watermark;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author libiao
 * @date 2023/7/4
 */
public class ImgCompressImg {

    public static void main(String[] args) {
        ImgCompressImg imgCompressImg = new ImgCompressImg();
        List<String> inputPaths = new ArrayList<>();
        List<String> outputPaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        String inputPath = args[0];
        String outputPath = args[1];
        int height = Integer.parseInt(args[2]);
        int width = Integer.parseInt(args[3]);
        File file = new File(inputPath);
        imgCompressImg.method(file, inputPaths, outputPaths, fileNames, inputPath, outputPath);
        for (int i = 0; i < inputPaths.size(); i++) {
            System.out.println(inputPaths.get(i));
            imgCompressImg.compressImg(inputPaths.get(i), outputPaths.get(i), height, width);
        }
    }

    private void compressImg(String imgName, String outFileName, int h, int w) {

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
                System.out.println("图片后缀不正确，图片名=" + outputStream);
                return;
            }
            ImageIO.write(finalImage, outFileName.substring(index + 1), outputStream);
            outputStream.flush();
            outputStream.close();
            finalImage.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 循环读取文件夹中所有图片路径
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
        int j = 13278;
        for (int i = 0; i < Objects.requireNonNull(fList).length; i++) {
            File f = fList[i];
            //String imgName = "default_" + j + f.getName().substring(f.getName().lastIndexOf("."));
            String imgName = f.getName();
            if (fList[i].isDirectory()) {
                method(f, inputPaths, outputPaths, fileNames, inputPath, outputPath);
            } else {
                inputPaths.add(inputPath + "\\" + f.getName());
                outputPaths.add(outputPath + "\\" + imgName);
                fileNames.add(imgName);
            }
            j++;
        }
    }
}

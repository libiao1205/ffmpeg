package com.example.demo.util.getImg;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * @author libiao
 * @date 2022/1/23
 */
public class Test {

    public static void main(String[] args) {
        Test.fetchFrame();
    }

    public static void fetchFrame() {
        String img = System.currentTimeMillis() + ".jpg";
        //储存文件的路径
        File file2 = new File("D:\\video\\3c340d6\\7ae74d3.mp4");
        File file4 = new File("D:\\video\\" + img);

        try {
            File file1 = fetchFrame(file2, file4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static File fetchFrame(File file, File file4)
            throws Exception {
        long start = System.currentTimeMillis();
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
        ff.start();
        int lenght = ff.getLengthInFrames();
        int i = 0;
        Frame f = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定
            f = ff.grabFrame();
            if ((i > 50) && (f.image != null)) {
                break;
            }
            i++;
        }
        int owidth = f.imageWidth;
        int oheight = f.imageHeight;
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage fecthedImage = converter.getBufferedImage(f);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH),
                0, 0, null);
        //ff.flush();
        ImageIO.write(bi, "jpg", file4);
        ff.stop();
        return file4;
    }
}

package com.example.demo.util.watermark;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author libiao
 * @date 2023/7/4
 */
public class FileRename {

    public static void main(String[] args) {
        FileRename fileRename = new FileRename();
        File file = new File("C:\\Users\\Admin\\Desktop\\wang_pic(1)");
        List<String> inputPaths = new ArrayList<>();
        List<String> outputPaths = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        fileRename.method(file, inputPaths, outputPaths, fileNames, "C:\\Users\\Admin\\Desktop\\wang_pic(1)", "C:\\Users\\Admin\\Desktop\\new_user_img");
        for (int i = 0; i < inputPaths.size(); i++) {
            String inputPath = inputPaths.get(i);
            String outputPath = outputPaths.get(i);

            File oldFile = new File(inputPath);
            File newFile = new File(outputPath);
            oldFile.renameTo(newFile);
        }
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
                outputPaths.add(outputPath + "\\default_" + (10906 + i) + fList[i].getName().substring(fList[i].getName().lastIndexOf(".")));
                fileNames.add(fList[i].getName());
            }
        }
    }
}

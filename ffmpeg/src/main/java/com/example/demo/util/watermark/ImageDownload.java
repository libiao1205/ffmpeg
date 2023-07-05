package com.example.demo.util.watermark;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author libiao
 * @date 2023/7/4
 */
public class ImageDownload {

    private final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(50, 50, 1000 * 10, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(1), new ThreadFactoryInfo("customThread-"));

    static int count = 1;
    /**
     * 文件下载到指定路径
     *
     * @param urlString 链接
     * @param savePath  保存路径
     * @param filename  文件名
     * @throws Exception
     */
    public void download(String urlString, String savePath, String filename) {

        threadPoolExecutor.execute(() -> {
            count++;
            // 构造URL
            URL url = null;
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            // 打开连接
            URLConnection con = null;
            try {
                con = url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //设置请求超时为20s
            con.setConnectTimeout(8 * 1000);
            //文件路径不存在 则创建
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            //jdk 1.7 新特性自动关闭
            try (InputStream in = con.getInputStream();
                 OutputStream out = new FileOutputStream(sf.getPath() + "\\" + filename)) {
                //创建缓冲区
                byte[] buff = new byte[1024];
                int n;
                // 开始读取
                while ((n = in.read(buff)) >= 0) {
                    out.write(buff, 0, n);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            count--;
        });
    }

    public static void main(String[] args) throws Exception {
        /*String str = "https://img.trading.live/2753a7/611e36/d4f1d1bc81b0414cbb7111bd276d26d5.png";
        System.out.println(str.substring(str.lastIndexOf("/") + 1));*/
        ImageDownload imageDownload = new ImageDownload();
        File file = new File("C:\\Users\\Admin\\Desktop\\提名数据.xlsx");
        //解析excel
        FileInputStream is = new FileInputStream(file);

        //获取整个excel
        XSSFWorkbook wb = new XSSFWorkbook(is);

        // sheet下标从0开始
        XSSFSheet sheet = wb.getSheetAt(0);
        int rowNum = sheet.getPhysicalNumberOfRows();
        if (rowNum <= 1) {
            is.close();
            wb.close();
            System.out.println("行数据={}" + rowNum);
            return;
        }

        // 循环行，下标从0开始
        for (int j = 250; j < 280; j++) {

            Row row = sheet.getRow(j);
            if (row == null) {
                continue;
            }
            Cell cell = row.getCell(1);
            String str = "";

            if(!ObjectUtils.isEmpty(cell)){
                cell.setCellType(CellType.STRING);
                if (!StringUtils.isEmpty(cell.getStringCellValue())) {
                    str = cell.getStringCellValue().replace("huiketang.top", "trading.live");
                    String fileName = str.substring(str.lastIndexOf("/") + 1);
                    imageDownload.download(str, "D:\\imgs\\", fileName);
                }
            }
        }
        is.close();
        wb.close();
    }
}

package com.example.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.util.watermark.DataUtil;
import com.example.demo.util.watermark.ImgCompressImg;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/file")
@RestController
public class FileController {

    private final ImgCompressImg imgCompressImg;

    @GetMapping("/compressImg")
    public JSONObject compressImg(@RequestParam("inputPath") String inputPath,
                                  @RequestParam("outputPath") String outputPath,
                                  @RequestParam(value = "width", required = false) Integer width,
                                  @RequestParam(value = "height", required = false) Integer height,
                                  @RequestParam("deviceId") String deviceId,
                                  @RequestParam(value = "customPrefix", required = false) String customPrefix,
                                  @RequestParam(value = "customNum", required = false) Integer customNum){
        JSONObject params = new JSONObject();
        params.put("inputPath",inputPath);
        params.put("outputPath",outputPath);
        params.put("width",width);
        params.put("height",height);
        DataUtil.deviceMap.put(deviceId, params);
        imgCompressImg.start(inputPath,
                outputPath,
                ObjectUtils.isEmpty(width) ? 0 : width,
                ObjectUtils.isEmpty(height) ? 0 : height,
                customPrefix,
                ObjectUtils.isEmpty(customNum) ? 0 : customNum);
        JSONObject result = new JSONObject();
        result.put("c", 0);
        result.put("msg", "成功");
        return result;
    }

    @GetMapping("/getOutFilePath")
    public JSONObject getOutFilePath(@RequestParam("deviceId") String deviceId){
        List<String> outputPathList = DataUtil.outPathList.get(deviceId);
        JSONObject result = new JSONObject();
        result.put("c", 0);
        result.put("msg", "成功");
        result.put("data", outputPathList);
        return result;
    }

    @GetMapping("/getImage")
    public void getImage(HttpServletResponse response, @RequestParam("path") String path) throws IOException {
        response.setCharacterEncoding("UTF-8");

        String[] strArray = path.split("\\.");
        int suffixIndex = strArray.length - 1;
        if ("png".equalsIgnoreCase(strArray[suffixIndex])) {
            response.addHeader("Content-type", "image/png");
        }
        if ("gif".equalsIgnoreCase(strArray[suffixIndex])) {
            response.addHeader("Content-type", "image/gif;charset=UTF-8");
        } else {
            response.addHeader("Content-type", "image/jpeg;charset=UTF-8");
        }
        response.addHeader("cache-control", "max-age=2628000");
        File file = new File(path);
        InputStream inputStream = new FileInputStream(file);
        int len = 0;
        byte[] buffer = new byte[1024];
        OutputStream out = response.getOutputStream();
        while ((len = inputStream.read(buffer)) > 0) {
            out.write(buffer, 0, len);
        }
        inputStream.close();
    }

}
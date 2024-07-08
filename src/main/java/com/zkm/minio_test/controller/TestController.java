package com.zkm.minio_test.controller;

import com.jlefebure.spring.boot.minio.MinioException;
import com.jlefebure.spring.boot.minio.MinioService;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/files")
public class TestController {

    @Resource
    private MinioService minioService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            minioService.upload(Paths.get(file.getOriginalFilename()), file.getInputStream(), file.getContentType());
        } catch (Exception e) {
            return "Upload failed!";
        }
        return "Upload success!";
    }

    @GetMapping("/")
    public List<Item> testMinio() {
        return minioService.list();
    }

    @GetMapping("/{fileName:.+}")
    public void getObject(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        InputStream inputStream;
        try {
            inputStream = minioService.get(Paths.get(fileName));
        } catch (Exception e) {
            return;
        }

        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename=" + fileName);
        response.setContentType(URLConnection.guessContentTypeFromName(fileName));

        // Copy the stream to the response's output stream.
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }

    @DeleteMapping("/remove")
    public String remove(@RequestParam String fileName) {
        try {
            minioService.remove(Paths.get(fileName));
        } catch (MinioException e) {
            return "Remove failed!";
        }
        return "Remove success!";
    }
}

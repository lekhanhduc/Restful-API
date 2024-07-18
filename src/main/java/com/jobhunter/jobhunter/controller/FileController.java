package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {

    @Value("${upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;

    @PostMapping("/files")
    public String uploadFile(@RequestParam("file")MultipartFile file, @RequestParam("folder") String folder){
        //file.getOriginalFilename : lấy ra đường dẫn file, tên file
        try {
            fileService.createDirectory(baseURI + folder);
            fileService.store(file, folder);
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        }
        return file.getOriginalFilename() + " " + folder;
    }
}

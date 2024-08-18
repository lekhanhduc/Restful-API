package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.dto.response.UploadFileDTO;
import com.jobhunter.jobhunter.exception.StogareException;
import com.jobhunter.jobhunter.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class FileController {

    @Value("${upload-file.base-uri}")
    private String baseURI;

    private final FileService fileService;

    @PostMapping("/files")
    public ResponseEntity<UploadFileDTO> uploadFile(@RequestParam(name = "file", required = false)MultipartFile file,
                                                    @RequestParam(name = "folder") String folder)
            throws URISyntaxException, IOException, StogareException {
        // check validation, Empty ?
        if(file == null || file.isEmpty()){
            throw new StogareException("File is Empty, Please upload ");
        }
       // file size (dung luong file) ? ==> đã config tại file properties

        // check định dạng file

        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "png", "doc", "docx");

        assert fileName != null;
        String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase(); // Ngắt chuỗi để lấy ra tên đuôi file

        if (!allowedExtensions.contains(fileExtension)) { // so sánh đuôi file có nằm trong allowedExtensions hay không
            throw new StogareException("File format not supported. Allowed formats: " + String.join(", ", allowedExtensions.toString()));
        }

        /*
        Hoặc boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endWish(item));
        if(!isValid){
             throw new StogareException("File format not supported. Allowed formats: " + String.join(", ", allowedExtensions));
        * */

            fileService.createDirectory(baseURI + folder);
            String uploadFile = fileService.store(file, folder);

            UploadFileDTO response = new UploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(response);
    }
}

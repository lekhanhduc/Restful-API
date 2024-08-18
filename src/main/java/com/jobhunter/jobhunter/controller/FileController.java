package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.dto.response.UploadFileDTO;
import com.jobhunter.jobhunter.exception.StogareException;
import com.jobhunter.jobhunter.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
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
    public ResponseEntity<UploadFileDTO> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file,
                                                    @RequestParam(name = "folder") String folder)
            throws URISyntaxException, IOException, StogareException {
        // check validation, Empty ?
        if (file == null || file.isEmpty()) {
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

    @GetMapping("/files")
    public ResponseEntity<Resource> downloadFile(@RequestParam("folder") String folder,
                                                 @RequestParam("fileName") String fileName)
            throws StogareException, URISyntaxException, FileNotFoundException {

        // Kiểm tra nếu fileName hoặc folder là null
        if (fileName == null || folder == null) {
            throw new StogareException("File or Folder is Empty");
        }

        // Xây dựng đường dẫn file
        String baseDir = baseURI.replace("file:///", "");
        Path filePath = Paths.get(baseDir, folder, fileName);

        // Kiểm tra file có tồn tại hay không
        File file = filePath.toFile();
        if (!file.exists()) {
            throw new StogareException("File with name = " + fileName + " not found");
        }

        // Lấy resource của file từ service
        InputStreamResource resource = fileService.getResource(fileName, folder);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}

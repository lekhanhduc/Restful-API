package com.jobhunter.jobhunter.service;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Value("${upload-file.base-uri}")
    private String baseURI;


    /**
     * new a folder if not exists
     * @param folder -> represents the directory you want to create
     * @throws URISyntaxException
     */
    public void createDirectory(String folder) throws URISyntaxException {
        /*
        URI: đường link thanh URL
        PAth: đường link trên máy tính của chúng ta
         */
        URI uri = new URI(folder);
        Path path = Paths.get(uri);
        File tmpDir = new File(path.toString());
        if(!tmpDir.isDirectory()){ // nếu thư mục chưa tồn tại thì tạo thư mục với đường dần path
            try {
                Files.createDirectory(tmpDir.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * save file
     * @param file
     * @param folder
     */
    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename(); // tạo 1 chuỗi kí tự đặc biệt tránh trường hợp trùng file
        URI uri = new URI(baseURI + folder + "/" +finalName); // đường dẫn lưu ảnh
        Path path = Paths.get(uri);
        try(InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }
        return finalName;
    }

    public long getFileLength(String fileName, String folder) throws URISyntaxException {

        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File tmpDir = new File(path.toString());
        if(!tmpDir.isDirectory() || tmpDir.exists()){
            return 0;
        }

        return tmpDir.length();
    }

    public InputStreamResource getResource(String fileName, String folder) throws URISyntaxException, FileNotFoundException {
        URI uri = new URI(baseURI + folder + "/" + fileName);
        Path path = Paths.get(uri);

        File file = new File(path.toString());

        return new InputStreamResource(new FileInputStream(file));
    }
}

//file.getOriginalFilename : lấy ra đường dẫn file, tên file


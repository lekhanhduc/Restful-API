package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.annotation.ApiMessage;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.response.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.model.CustomError;
import com.jobhunter.jobhunter.model.NotfoundException;
import com.jobhunter.jobhunter.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("users/{id}")
    @ApiMessage("Get User By Id")
    public ResponseEntity<?> getUserById(@PathVariable  Long id) {
        User user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok().body(user);
        }
//        return ResponseEntity.internalServerError()
//                .body(CustomError                 // Tự custom Error tùy thích
//                        .builder()                  // nhưng vấn đề giả sử có 100 method bắt lỗi như này, mình phải try-catch 100 chỗ
//                        .statusCode("500")
//                        .message("User Not Exists")
//                        .build());

            throw new NotfoundException(CustomError.builder()
                    .statusCode("404")
                    .message("User not found")
                    .build());
        }


    @PostMapping("/users")
    @ApiMessage("Create new User")
    public ResponseEntity<UserDTOResponse> creatUser(@RequestBody UserDTOCreate userDTOCreate){
//        User newUser =  userService.hanleSaveUser(user); // Json -> Java Object: từ dữ liệu ở json -> object java
//        return newUser; // Object -> Json : để hiện thị dữ liệu json ở postman

        return ResponseEntity.status(HttpStatus.CREATED).body(userService.hanleSaveUser(userDTOCreate));
    }

    @DeleteMapping("users/{id}")
    @ApiMessage("Delete User")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {  // return kiểu void nếu không cần truyền body
        User user = userService.getUserById(id);
        if (user != null) {
            userService.deleteUserById(id);
            return ResponseEntity.ok().body("Delete Success");
        }
        return ResponseEntity.noContent().build(); // noContent: 204
    }

    @PutMapping("/users")
    @ApiMessage("Update User")
    public ResponseEntity<UserDTOResponse> updateUser(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(user));
    }



    @GetMapping("/getAll")
    @ApiMessage("Get ALl User")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll(spec, pageable));
    }
}

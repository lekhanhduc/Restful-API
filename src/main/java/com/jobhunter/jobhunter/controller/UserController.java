package com.jobhunter.jobhunter.controller;


import com.jobhunter.jobhunter.annotation.ApiMessage;
import com.jobhunter.jobhunter.dto.pagination.ResultPaginationDTO;
import com.jobhunter.jobhunter.dto.request.UserDTOCreate;
import com.jobhunter.jobhunter.dto.response.DeleteDTOResponse;
import com.jobhunter.jobhunter.dto.response.UserDTOResponse;
import com.jobhunter.jobhunter.entity.User;
import com.jobhunter.jobhunter.model.ResourceNotFoundException;
import com.jobhunter.jobhunter.service.UserService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
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

//        return ResponseEntity.internalServerError()
//                .body(CustomError                 // Tự custom Error tùy thích
//                        .builder()                  // nhưng vấn đề giả sử có 100 method bắt lỗi như này, mình phải try-catch 100 chỗ
//                        .statusCode("500")
//                        .message("User Not Exists")
//                        .build());
        return ResponseEntity.ok().body(userService.getUserById(id));
    }



    @PostMapping("/users")
    public ResponseEntity<UserDTOResponse> createUser(@RequestBody @Valid UserDTOCreate userDTOCreate) {
        UserDTOResponse createdUser = userService.handleSaveUser(userDTOCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @DeleteMapping("users/{id}")
    @ApiMessage("Delete User")
    public ResponseEntity<DeleteDTOResponse> deleteById(@PathVariable Long id) {
        return ResponseEntity.ok().body(userService.deleteUserById(id));
    }

    @PutMapping("/users")
    @ApiMessage("Update User")
    public ResponseEntity<UserDTOResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserDTOCreate userDTOCreate) {
        try {
            UserDTOResponse updatedUser = userService.updateUser(id, userDTOCreate);
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/users")
    @ApiMessage("Get ALl User")
    public ResponseEntity<ResultPaginationDTO> getAllUser(@Filter Specification<User> spec, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll(spec, pageable));
    }
}

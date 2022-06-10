package com.bstar.banking.controller.admin;


import com.bstar.banking.model.request.DecentralizationDTO;
import com.bstar.banking.model.request.FilterUserDTO;
import com.bstar.banking.model.request.SignupRequest;
import com.bstar.banking.model.request.UserDTO;
import com.bstar.banking.model.response.RestResponse;
import com.bstar.banking.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@PreAuthorize("hasAuthority('0')")
@RestController
@RequestMapping("/api/v1/users")
public class UserAdminController {

    private final UserService userService;

    public UserAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get-one-user/{email}")
    public ResponseEntity<RestResponse<?>> getOneUser(@Valid @Email @NotBlank @PathVariable("email") String email) {
        return ResponseEntity.ok(userService.findOneUser(email));
    }

    @GetMapping("/get-all-user")
    public ResponseEntity<RestResponse<?>> getAllUserFiltered(@Valid FilterUserDTO userDTO) {
        return ResponseEntity.ok(userService.findAllUserFiltered(userDTO));
    }

    @PostMapping("/create-user")
    public ResponseEntity<RestResponse<?>> createUser(@Valid @RequestBody SignupRequest userDTO) {
        return ResponseEntity.ok(userService.userAdminCreate(userDTO));
    }


    @PutMapping("/update-user/{email}")
    public ResponseEntity<RestResponse<?>> userUpdateByEmail(@PathVariable(name = "email") String email,
                                                             @Valid @RequestBody UserDTO userDTO,
                                                             Authentication authentication) {
        return ResponseEntity.ok(userService.userAdminUpdate(email, userDTO, authentication));
    }

    @DeleteMapping("/delete-user/{email}")
    public ResponseEntity<RestResponse<?>> userDelete(@Valid @Email @NotBlank @PathVariable("email") String email) {
        return ResponseEntity.ok(userService.userAdminDisabled(email));
    }

    @DeleteMapping("/decentralization-user/{email}")
    public ResponseEntity<RestResponse<?>> userDecentralization(@Valid @RequestBody DecentralizationDTO dto,  Authentication authentication) {
        return ResponseEntity.ok(userService.userAdminDecentralization(dto, authentication));
    }


}

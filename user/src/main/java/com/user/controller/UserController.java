package com.user.controller;

import com.user.enums.ErrorCode;
import com.user.enums.StatusCode;
import com.user.model.User;
import com.user.service.UserService;
import com.user.util.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.user.util.Utils.showSuccessObj;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody Map<String,Object> requestBody) {

        String[] requiredFields = {"name", "email", "password", "orgId"};

        for (String field : requiredFields) {
            if (!requestBody.containsKey(field) || requestBody.get(field) == null || requestBody.get(field).toString().trim().isEmpty()) {
                return Utils.showError(null,
                        ErrorCode.REQUIRED_FIELDS_MISSING.message + field,
                        StatusCode.BADREQUEST.code,
                        ErrorCode.REQUIRED_FIELDS_MISSING);
            }
        }

        return showSuccessObj(userService.createUser(requestBody),"User created successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id) {
        return userService.getUser(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<User>> getUsersByOrg(@PathVariable String orgId) {
        return ResponseEntity.ok(userService.getUsersByOrg(orgId));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(
            @RequestHeader("USER-TOKEN") String token,
            @RequestHeader("ORG-ID") String orgId,
            @RequestBody Map<String, Object> updateRequest) {

        try {
            // Validate token belongs to org
            String userId = userService.getUserIdFromToken(token, orgId);
            if (userId == null) {
                return Utils.showError(null,ErrorCode.UNAUTHORIZED_ACCESS.message,
                        StatusCode.BADREQUEST.code, ErrorCode.UNAUTHORIZED_ACCESS);
            }

            // Proceed to update using userId from token
            User updatedUser = userService.updateUser(userId, updateRequest);

            return showSuccessObj(updatedUser, "User updated successfully");

        } catch (RuntimeException ex) {
            return Utils.showError(null, ex.getMessage(),
                    StatusCode.BADREQUEST.code, ErrorCode.INVALID_REQUEST);
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}

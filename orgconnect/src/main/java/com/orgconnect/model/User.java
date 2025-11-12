package com.orgconnect.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "users")
public class User {

    @Id
    private Long id;

    private String userId;

    private String orgId;

    private String username;

    private String email;

    private String password;

    private String role;

    private Boolean onlineStatus;

    private String profilePicUrl;

    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;
}


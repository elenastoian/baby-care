package com.baby.care.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 6, message = "Password should have at least 6 characters")
    private String password;
}

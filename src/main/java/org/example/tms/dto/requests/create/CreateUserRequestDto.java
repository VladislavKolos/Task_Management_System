package org.example.tms.dto.requests.create;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.model.enums.UserRole;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequestDto {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 256)
    @NotBlank
    private String password;

    @NotNull
    private UserRole role;
    private LocalDateTime createdAt;
}

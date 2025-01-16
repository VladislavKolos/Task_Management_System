package org.example.tms.dto.requests.update;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.model.enums.UserRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequestDto {

    @Email
    private String email;

    @Size(min = 8, max = 256)
    private String password;
    private UserRole role;
    private LocalDateTime createdAt;
}

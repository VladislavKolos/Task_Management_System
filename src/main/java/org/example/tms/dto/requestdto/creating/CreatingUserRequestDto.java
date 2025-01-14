package org.example.tms.dto.requestdto.creating;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.model.enums.UserRole;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatingUserRequestDto {

    @Email
    @NotNull
    private String email;

    @Size(min = 8, max = 256)
    @NotNull
    private String password;

    @NotNull
    private UserRole role;
    private LocalDateTime createdAt;
}

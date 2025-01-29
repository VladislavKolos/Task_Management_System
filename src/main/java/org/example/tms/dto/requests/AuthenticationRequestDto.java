package org.example.tms.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.validator.constraint.annotation.ValidAuthRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidAuthRequest
public class AuthenticationRequestDto {

    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 256)
    @NotBlank
    private String password;
}

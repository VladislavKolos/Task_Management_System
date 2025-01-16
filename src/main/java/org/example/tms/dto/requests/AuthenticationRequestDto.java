package org.example.tms.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.annotation.custom.ValidAuthRequest;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ValidAuthRequest
public class AuthenticationRequestDto {

    @Email
    @NotNull
    private String email;

    @Size(min = 8, max = 256)
    @NotNull
    private String password;
}

package org.example.tms.dto.responses;

import lombok.Builder;

@Builder
public record AuthenticationResponseDto(String token, String refreshToken) {
}

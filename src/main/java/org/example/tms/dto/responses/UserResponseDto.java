package org.example.tms.dto.responses;

import lombok.Builder;
import org.example.tms.model.enums.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserResponseDto(UUID id,
                              String email,
                              UserRole role,
                              LocalDateTime createdAt) {
}

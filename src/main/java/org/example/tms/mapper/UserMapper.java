package org.example.tms.mapper;

import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.model.User;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);
}

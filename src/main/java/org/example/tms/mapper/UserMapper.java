package org.example.tms.mapper;

import org.example.tms.dto.requests.create.CreateUserRequestDto;
import org.example.tms.dto.responses.UserResponseDto;
import org.example.tms.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface UserMapper {
    UserResponseDto toUserResponseDto(User user);

    @Mapping(target = "id", ignore = true)
    User toUserForCreate(CreateUserRequestDto userRequestDto);
}

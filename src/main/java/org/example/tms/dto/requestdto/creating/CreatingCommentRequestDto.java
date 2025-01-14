package org.example.tms.dto.requestdto.creating;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatingCommentRequestDto {

    @Size(max = 1000)
    @NotNull
    private String content;

    @NotNull
    private UUID taskId;

    @NotNull
    private UUID authorId;
}

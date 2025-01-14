package org.example.tms.dto.requestdto.updating;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatingCommentRequestDto {

    @Size(max = 1000)
    private String content;
    private UUID taskId;
    private UUID authorId;
}

package org.example.tms.dto.requests.update;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCommentRequestDto {

    @Size(max = 1000)
    private String content;
    private UUID taskId;
    private UUID authorId;
}

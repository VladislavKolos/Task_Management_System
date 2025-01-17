package org.example.tms.dto.requests.create;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.annotation.custom.TaskExists;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequestDto {

    @Size(max = 1000)
    @NotNull
    private String content;

    @NotNull
    @TaskExists
    private UUID taskId;
}

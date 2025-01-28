package org.example.tms.dto.requests.create;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.tms.validator.constraint.annotation.TaskExists;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequestDto {

    @Size(max = 1000)
    @NotBlank
    private String content;

    @NotNull
    @TaskExists
    private UUID taskId;
}

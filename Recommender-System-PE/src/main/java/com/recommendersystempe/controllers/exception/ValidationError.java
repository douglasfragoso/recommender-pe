package com.recommendersystempe.controllers.exception;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Schema(description = "Represents the ValidationError in the system")
public class ValidationError {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "America/Sao_Paulo")
    @Schema(description = "Date of the error", example = "2021-10-01T00:00:00Z", required = true)
    @NotNull(message = "The field date is required")
    private Instant timestamp;

    @Schema(description = "Status of the error", example = "404", required = true)
    @NotNull(message = "The field status is required")
    private Integer status;

    @Schema(description = "Error of the error", example = "Not Found", required = true)
    @NotBlank(message = "The field error is required")
    private String error;

    @Schema(description = "Message of the error", example = "The resource could not be found", required = true)
    @NotBlank(message = "The field message is required")
    private List<String> message = new ArrayList<>();

    @Schema(description = "Path of the error", example = "/users/1", required = true)
    @NotBlank(message = "The field path is required")
    private String path;
    
   

}

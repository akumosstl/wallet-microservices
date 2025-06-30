package io.github.akumosstl.commons.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.akumosstl.commons.type.Types;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ApiResponseDto {
    private int responseCode;
    private String responseMessage;
    private Object data;
}
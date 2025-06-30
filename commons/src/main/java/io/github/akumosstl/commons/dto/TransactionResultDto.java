package io.github.akumosstl.commons.dto;

import io.github.akumosstl.commons.type.Types;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TransactionResultDto {

    private String type;

    private int code;

    private String object;

    private LocalDateTime date;

}

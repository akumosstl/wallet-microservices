package io.github.akumosstl.account.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class AccountRequestDto implements Serializable {
    private Long accountId;

    @NotNull(message = "Document id is required")
    private String documentId;

    @NotNull(message = "Document is required")
    private String document;

    @NotNull(message = "First Name is required")
    private String firstName;

    @NotNull(message = "Last Name is required")
    private String lastName;

    @NotNull(message = "email is required")
    private String email;

    @NotNull(message = "phone is required")
    private String phone;

    private String accountNo;
}

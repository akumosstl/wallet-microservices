package io.github.akumosstl.auth.payload.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RoleRequest {

    @NotBlank
    @ApiModelProperty(example = "ROLE_ADMIN, ROLE_USER", required = true, dataType = "String", notes = "Role Name in the format ROLE_<?>")
    private String name;
}

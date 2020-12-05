package com.mentor.club.model.authentication;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WrappedJwtToken {
    @JsonProperty
    @ApiModelProperty(notes = "The JWT generated", required = true)
    String token;
}

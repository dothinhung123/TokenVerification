package com.go.tokenverification.jwt;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class JwtToken {
    private String username;
}

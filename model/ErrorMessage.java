package com.go.tokenverification.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ErrorMessage {
    private OffsetDateTime offsetDateTime = OffsetDateTime.now();
    private String errorMessageDescription;
}

package com.go.tokenverification.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AcknowledgeResponse implements Serializable {
    private boolean isAcknowledge;
    private OffsetDateTime offsetDateTime = OffsetDateTime.now();
    private String message;
}


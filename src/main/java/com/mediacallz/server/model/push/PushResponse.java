package com.mediacallz.server.model.push;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PushResponse {

    private final int statusCode;
    private String reason;
}
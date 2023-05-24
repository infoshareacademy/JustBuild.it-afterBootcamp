package com.jaszczurki.justbuild_it_web_portal.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProlongRequest {

    private UUID id;
    private Integer days;
}

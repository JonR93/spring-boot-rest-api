package com.springboot.rest.api.server.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private Long id;
    private String sentFrom;
    private String sentTo;
    private String subject;
    private String body;
    private boolean wasSent;
    private Date sentDate;
}

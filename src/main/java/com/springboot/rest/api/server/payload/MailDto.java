package com.springboot.rest.api.server.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {
    private long id;
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String body;
    private boolean wasSent;
    private Date sentDate;
}
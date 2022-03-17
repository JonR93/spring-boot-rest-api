package com.springboot.rest.api.server.service;

import com.springboot.rest.api.server.entity.Mail;
import com.springboot.rest.api.server.payload.MailDto;
import com.springboot.rest.api.server.payload.MailsDto;

public interface EmailService {
    Mail sendEmail(Mail mail);
    MailsDto findAllMail(int pageNo, int pageSize, String sortBy, String sortDir);
    MailDto findMailById(long id);
    void deleteMail(long id);
}

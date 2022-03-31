package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.Mail;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
class MailRepositoryTest {

    @Autowired
    private MailRepository mailRepository;

    private Mail unsentMail;
    private Mail sentMail;

    @BeforeEach
    void setup(){
        unsentMail = Mail.builder()
                .subject("Testing")
                .body("Testing")
                .sendFrom("Someone@my.api.com")
                .sendTo("SomeoneElse@my.api.com")
                .wasSent(false)
                .build();

        sentMail = Mail.builder()
                .subject("Testing")
                .body("Message Sent")
                .sendFrom("Someone@my.api.com")
                .sendTo("SomeoneElse@my.api.com")
                .wasSent(true)
                .build();
    }

    @Test
    void saveMail(){
        Mail savedMail = mailRepository.save(unsentMail);
        Assertions.assertThat(savedMail).isNotNull();
        Assertions.assertThat(savedMail.getId()).isPositive();
    }

    @Test
    void findAll(){
        mailRepository.save(unsentMail);
        mailRepository.save(sentMail);
        List<Mail> foundMails = mailRepository.findAll();
        Assertions.assertThat(foundMails).hasSize(2);
    }
}

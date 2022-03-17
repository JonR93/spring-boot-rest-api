package com.springboot.rest.api.server.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mails")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String fromAddress;
    private String toAddress;
    private String subject;
    private String body;
    private boolean wasSent;
    private Date sentDate;
}

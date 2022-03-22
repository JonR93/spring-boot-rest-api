package com.springboot.rest.api.server.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "mails")
public class Mail extends AuditableEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String sendFrom;
    private String sendTo;
    private String subject;
    private String body;
    private boolean wasSent;
    private Date sentDate;
}

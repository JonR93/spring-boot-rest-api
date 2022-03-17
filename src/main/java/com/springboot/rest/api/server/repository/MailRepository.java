package com.springboot.rest.api.server.repository;

import com.springboot.rest.api.server.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {
}

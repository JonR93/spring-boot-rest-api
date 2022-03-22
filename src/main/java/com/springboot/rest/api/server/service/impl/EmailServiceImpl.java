package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Mail;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.MailDto;
import com.springboot.rest.api.server.payload.MailsDto;
import com.springboot.rest.api.server.repository.MailRepository;
import com.springboot.rest.api.server.service.EmailService;
import com.springboot.rest.api.server.utils.ObjectMapperUtil;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class EmailServiceImpl implements EmailService {

    private MailRepository mailRepository;
    private JavaMailSender mailSender;
    private ModelMapper mapper;

    @Async
    @Override
    public Mail sendEmail(Mail mail) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(mail.getBody(), true);
            helper.setTo(mail.getSendTo());
            helper.setSubject(mail.getSubject());
            helper.setFrom(mail.getSendFrom());
            mailSender.send(mimeMessage);
        } catch(MailAuthenticationException e){
            //todo: implement logging
            throw new IllegalStateException("Failed to send email.");
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email.");
        }
        mail.setSentDate(new Date());
        mail.setWasSent(true);
        return mailRepository.save(mail);
    }

    @Override
    public MailsDto findAllMail(int pageNo, int pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        // create Pageable instance
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Mail> allMail = mailRepository.findAll(pageable);

        // get content for page object
        List<Mail> listOfMails = allMail.getContent();

        List<MailDto> content = ObjectMapperUtil.mapAll(listOfMails,MailDto.class);

        MailsDto mailsDto = new MailsDto();
        mailsDto.setContent(content);
        mailsDto.setPageNo(allMail.getNumber());
        mailsDto.setPageSize(allMail.getSize());
        mailsDto.setTotalElements(allMail.getTotalElements());
        mailsDto.setTotalPages(allMail.getTotalPages());
        mailsDto.setLast(allMail.isLast());

        return mailsDto;
    }

    @Override
    public MailDto findMailById(long id) {
        Mail mail = mailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mail", "id", id));
        return ObjectMapperUtil.map(mail,MailDto.class);
    }

    @Override
    public void deleteMail(long id) {
        Mail mail = mailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mail", "id", id));
        mailRepository.delete(mail);
    }
}

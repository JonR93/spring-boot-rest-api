package com.springboot.rest.api.server.service.impl;

import com.springboot.rest.api.server.entity.Mail;
import com.springboot.rest.api.server.exception.ResourceNotFoundException;
import com.springboot.rest.api.server.payload.MailDto;
import com.springboot.rest.api.server.payload.MailsDto;
import com.springboot.rest.api.server.repository.MailRepository;
import com.springboot.rest.api.server.service.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmailServiceImpl implements EmailService {

    private MailRepository mailRepository;

    private ModelMapper mapper;
    
    public EmailServiceImpl(ModelMapper mapper, MailRepository mailRepository){
        this.mailRepository = mailRepository;
        this.mapper = mapper;
    }

    @Override
    public Mail sendEmail(Mail mail) {
        //TODO: implement method
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

        List<MailDto> content = listOfMails.stream().map(this::mapToDTO).collect(Collectors.toList());

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
        return mapToDTO(mail);
    }

    @Override
    public void deleteMail(long id) {
        Mail mail = mailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Mail", "id", id));
        mailRepository.delete(mail);
    }

    // convert Entity into DTO
    private MailDto mapToDTO(Mail mail){
        return mapper.map(mail, MailDto.class);
    }

    // convert DTO to entity
    private Mail mapToEntity(MailDto mailDto){
        return mapper.map(mailDto, Mail.class);
    }
}

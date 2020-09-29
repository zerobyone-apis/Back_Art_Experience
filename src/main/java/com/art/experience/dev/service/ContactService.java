package com.art.experience.dev.service;

import com.art.experience.dev.data.ContactRepository;
import com.art.experience.dev.data.UserRepository;
import com.art.experience.dev.model.Contact;
import com.art.experience.dev.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Objects;

@Service
public class ContactService {

    private final ContactRepository repository;
    private final SendMailService mailService;
    private final UserRepository userRepository;
    private String to;

    @Autowired
    public ContactService(final ContactRepository repository,
                          final SendMailService mailService,
                          final UserRepository userRepository,
                          @Value("${mail.contact.recipient.to}") final String to) {
        this.repository = repository;
        this.mailService = mailService;
        this.userRepository = userRepository;
        this.to = to;
    }

    public void sentMessage(final Contact contact) {

        // If the contact contain UserID
        if (Objects.nonNull(contact.getUserId())) {
            User user = userRepository.findById(contact.getUserId()).get();
            contact.setUsername(user.getUsername());
            contact.setEmailFrom(user.getEmail());
        }

        // Recipient and Created Time
        contact.setEmailTo(to);
        contact.setCreatedOn(Instant.now());

        StringBuilder descriptionMessage = new StringBuilder();
        descriptionMessage
                .append("Name: ").append(contact.getFullName() + "\n")
                .append("Phone: ").append(contact.getCelPhone() + "\n")
                .append("Email: ").append(contact.getEmailFrom() + "\n")
                .append("Message: ").append("\n" + contact.getDescription() + "\n");

        mailService.contactEmail(descriptionMessage.toString(), contact.getSubject(), contact.getEmailFrom());
        repository.save(contact);
    }

}

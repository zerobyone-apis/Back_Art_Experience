package com.art.experience.dev.web;

import com.art.experience.dev.Configuration.RestCrossOriginController;
import com.art.experience.dev.model.Contact;
import com.art.experience.dev.service.ContactService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestCrossOriginController("/contact")
public class ContactRestController {

    private static final Logger LOGGER = LogManager.getLogger(ContactRestController.class);
    private ContactService contactService;

    @Autowired
    public ContactRestController(final ContactService contactService) {
        this.contactService = contactService;
    }

    @PatchMapping("/v1")
    @ResponseStatus(HttpStatus.OK)
    public void createContact(@RequestBody final Contact message) {
        LOGGER.info("*******************************************");
        LOGGER.info("\n\n Contact Message: " +
                " |\n Full Name: ", message.getFullName() +
                " |\n Contact Email: ", message.getEmailFrom() +
                " |\n Cel Phone: " + message.getCelPhone() +
                " |\n Subject: " + message.getSubject());
        LOGGER.info("*******************************************");
        contactService.sentMessage(message);
    }


}

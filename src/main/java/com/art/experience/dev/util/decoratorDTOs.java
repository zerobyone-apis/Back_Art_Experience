package com.art.experience.dev.util;

import com.art.experience.dev.model.Barber;
import com.art.experience.dev.model.DTO.DTOBarberResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Base64;
import java.util.Optional;

public final class decoratorDTOs {

    private static final Logger LOGGER = LogManager.getLogger(decoratorDTOs.class);

    public static DTOBarberResponse utilDeoratorPatternBarber(Optional<Barber> barberResult) {
        LOGGER.info("Starting filtering. . . . ");
        DTOBarberResponse barberSecure = new DTOBarberResponse();
        if (barberResult.isPresent()) {
            //todo: SET DTO Barber
            LOGGER.info("filtering. . . . ");
            Barber brb = barberResult.get();
            barberSecure.setBarber(brb.getActive());
            barberSecure.setAdmin(brb.getAdmin());
            barberSecure.setBarberId(brb.getBarberId());
            barberSecure.setUserId(brb.getUserId());
            barberSecure.setName(brb.getName());
            barberSecure.setBarberDescription(brb.getBarberDescription());
            barberSecure.setFacebook(brb.getFacebook());
            barberSecure.setInstagram(brb.getInstagram());
            barberSecure.setUrlProfileImage(brb.getUrlProfileImage());
            barberSecure.setPrestige(brb.getPrestige());

            barberSecure.setLocalName(brb.getLocalName());
            barberSecure.setUsername(brb.getUsername());
            barberSecure.setEmail(brb.getEmail());
        }
        LOGGER.info("Returning. . . . " + barberSecure.getBarberId());
        return barberSecure;
    }

    public static String encode(String encodeText){
        return Base64.getEncoder().encodeToString(encodeText.getBytes());
    }

    public static String decode(String decodeText){
        return new String (Base64.getMimeDecoder().decode(decodeText));
    }
}

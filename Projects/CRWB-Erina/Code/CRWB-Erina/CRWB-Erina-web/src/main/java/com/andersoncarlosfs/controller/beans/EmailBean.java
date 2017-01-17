package com.andersoncarlosfs.controller.beans;

import com.andersoncarlosfs.controller.services.EmailService;
import com.andersoncarlosfs.model.AbstractController;
import com.andersoncarlosfs.model.AbstractConverter;
import com.andersoncarlosfs.model.daos.EmailDAO;
import com.andersoncarlosfs.model.entities.Email;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.xml.bind.JAXBException;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@Named
@SessionScoped
public class EmailBean extends AbstractController<EmailService, EmailDAO, Email, java.lang.Long> {

    /**
     *
     */
    @FacesConverter(forClass = Email.class)
    public static class EmailConverter extends AbstractConverter<EmailDAO, Email, java.lang.Long> implements Converter {

        /**
         *
         * @throws javax.xml.bind.JAXBException
         * @param value
         * @return
         */
        @Override
        protected java.lang.Long getKey(String value) throws JAXBException {
            return new java.lang.Long(value);
        }

    }

}

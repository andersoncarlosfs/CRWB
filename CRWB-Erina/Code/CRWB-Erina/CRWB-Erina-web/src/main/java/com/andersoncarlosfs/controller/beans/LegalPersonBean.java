package com.andersoncarlosfs.controller.beans;

import com.andersoncarlosfs.controller.services.LegalPersonService;
import com.andersoncarlosfs.model.AbstractController;
import com.andersoncarlosfs.model.AbstractConverter;
import com.andersoncarlosfs.model.daos.LegalPersonDAO;
import com.andersoncarlosfs.model.entities.LegalPerson;
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
public class LegalPersonBean extends AbstractController<LegalPersonService, LegalPersonDAO, LegalPerson, java.lang.Integer> {

    /**
     *
     */
    @FacesConverter(forClass = LegalPerson.class)
    public static class LegalPersonConverter extends AbstractConverter<LegalPersonDAO, LegalPerson, java.lang.Integer> implements Converter {

        /**
         *
         * @throws javax.xml.bind.JAXBException
         * @param value
         * @return
         */
        @Override
        protected java.lang.Integer getKey(String value) throws JAXBException {
            return new java.lang.Integer(value);
        }

    }

}

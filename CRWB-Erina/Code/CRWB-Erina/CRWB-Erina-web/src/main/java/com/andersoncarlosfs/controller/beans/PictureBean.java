package com.andersoncarlosfs.controller.beans;

import com.andersoncarlosfs.controller.services.PictureService;
import com.andersoncarlosfs.model.AbstractController;
import com.andersoncarlosfs.model.AbstractConverter;
import com.andersoncarlosfs.model.daos.PictureDAO;
import com.andersoncarlosfs.model.entities.Picture;
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
public class PictureBean extends AbstractController<PictureService, PictureDAO, Picture, java.lang.Long> {

    /**
     *
     */
    @FacesConverter(forClass = Picture.class)
    public static class PictureConverter extends AbstractConverter<PictureDAO, Picture, java.lang.Long> implements Converter {

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

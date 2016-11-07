package com.andersoncarlosfs.controller.beans;

import com.andersoncarlosfs.controller.services.UserService;
import com.andersoncarlosfs.model.AbstractController;
import com.andersoncarlosfs.model.AbstractConverter;
import com.andersoncarlosfs.model.daos.UserDAO;
import com.andersoncarlosfs.model.entities.User;
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
public class UserBean extends AbstractController<UserService, UserDAO, User, java.lang.Long> {

    /**
     *
     */
    @FacesConverter(forClass = User.class)
    public static class UserConverter extends AbstractConverter<UserDAO, User, java.lang.Long> implements Converter {

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

package com.andersoncarlosfs.controller.beans;

import com.andersoncarlosfs.controller.services.GroupService;
import com.andersoncarlosfs.model.AbstractController;
import com.andersoncarlosfs.model.AbstractConverter;

import com.andersoncarlosfs.model.daos.GroupDAO;
import com.andersoncarlosfs.model.entities.Group;
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
public class GroupBean extends AbstractController<GroupService, GroupDAO, Group, java.lang.Integer> {

    /**
     *
     */
    @FacesConverter(forClass = Group.class)
    public static class GroupConverter extends AbstractConverter<GroupDAO, Group, java.lang.Integer> implements Converter {

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

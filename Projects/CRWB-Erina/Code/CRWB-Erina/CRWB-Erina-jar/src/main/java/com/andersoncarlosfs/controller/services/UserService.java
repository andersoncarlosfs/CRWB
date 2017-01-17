/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.controller.services;

import com.andersoncarlosfs.model.daos.UserDAO;
import com.andersoncarlosfs.model.AbstractService;
import com.andersoncarlosfs.model.entities.User;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@SessionScoped
public class UserService extends AbstractService<UserDAO, User, Long> {

    @Inject
    private UserDAO dao;

    /**
     *
     * @see com.andersoncarlosfs.model.AbstractService#getDAO()
     * @return the dao
     */
    @Override
    public UserDAO getDAO() {
        return dao;
    }

}

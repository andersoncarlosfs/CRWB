/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.controller.services;

import com.andersoncarlosfs.model.daos.GroupDAO;
import com.andersoncarlosfs.model.AbstractService;
import com.andersoncarlosfs.model.entities.Group;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@SessionScoped
public class GroupService extends AbstractService<GroupDAO, Group, Integer> {

    @Inject
    private GroupDAO dao;

    /**
     *
     * @see com.andersoncarlosfs.model.AbstractService#getDAO()
     * @return the dao
     */
    @Override
    public GroupDAO getDAO() {
        return dao;
    }

}

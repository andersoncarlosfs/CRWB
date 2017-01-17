/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.persistence;

import com.andersoncarlosfs.model.AbstractPersistenceTest;
import com.andersoncarlosfs.model.entities.User;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
public class UserPersistenceTest extends AbstractPersistenceTest<User, Long> {

    private User entity = new User();

    @Override
    public Class getClasse() {
        return User.class;
    }

    @Override
    public User getObject() {
        return entity;
    }

    @Override
    public void modifyObject() {
    }

    /**
     *
     * @see com.andersoncarlosfs.persistence.AbstractPersistenceTest#testPersistence()
     * @throws java.lang.Exception
     */
    @Override
    public void testPersistence() throws Exception {

    }

}

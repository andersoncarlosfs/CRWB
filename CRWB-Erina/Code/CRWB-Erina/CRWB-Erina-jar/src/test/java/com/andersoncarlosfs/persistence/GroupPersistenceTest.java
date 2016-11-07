/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.persistence;

import com.andersoncarlosfs.model.AbstractPersistenceTest;
import com.andersoncarlosfs.model.entities.Group;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
public class GroupPersistenceTest extends AbstractPersistenceTest<Group, Integer> {

    private Group entity = new Group("Test");

    @Override
    public Class getClasse() {
        return Group.class;
    }

    @Override
    public Group getObject() {
        return entity;
    }

    @Override
    public void modifyObject() {
        entity.setName("Modified");
    }

}

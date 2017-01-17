/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.entities;

import com.andersoncarlosfs.model.AbstractEntityTest;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
public class GroupEntityTest extends AbstractEntityTest<Group, Integer> {

    private Group group = new Group("Test");

    @Override
    public Group getObject() {
        return group;
    }

    @Override
    public Group getCloneModified() throws CloneNotSupportedException {
        Group modified = super.getCloneModified();
        modified.setName("Modified");
        return modified;
    }

}

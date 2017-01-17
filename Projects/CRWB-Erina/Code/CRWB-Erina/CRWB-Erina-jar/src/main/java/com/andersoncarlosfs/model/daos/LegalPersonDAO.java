/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.daos;

import com.andersoncarlosfs.model.AbstractDAO;
import com.andersoncarlosfs.model.entities.LegalPerson;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
@RequestScoped
public class LegalPersonDAO extends AbstractDAO<LegalPerson, Integer> {

    /**
     *
     * @see AbstractDAO#getClasse()
     * @return
     */
    @Override
    public Class<LegalPerson> getClasse() {
        return LegalPerson.class;
    }

}

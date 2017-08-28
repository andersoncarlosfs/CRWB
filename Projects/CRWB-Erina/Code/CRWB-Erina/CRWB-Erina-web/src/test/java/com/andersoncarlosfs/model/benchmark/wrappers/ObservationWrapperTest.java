/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.benchmark.wrappers;

import com.andersoncarlosfs.model.benchmark.AbstractWrapperTest;
import com.andersoncarlosfs.model.entities.Picture;
import java.util.Date;

/**
 *
 * @author Anderson Carlos Ferreira da Silva
 */
public class ObservationWrapperTest extends AbstractWrapperTest<Observation, Long> {

    private final Observation object = new Observation(new com.andersoncarlosfs.model.entities.Observation("Text", new Date(), new Picture()));

    @Override
    public Observation getObject() {
        return object;
    }

    @Override
    public Observation getCloneModified() throws CloneNotSupportedException {
        Observation modified = super.getCloneModified();
        modified.setText("Modified");
        return modified;
    }

}

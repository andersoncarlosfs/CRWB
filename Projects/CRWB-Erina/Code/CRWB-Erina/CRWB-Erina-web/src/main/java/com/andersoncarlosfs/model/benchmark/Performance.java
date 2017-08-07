/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.benchmark;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anderson
 */
@XmlRootElement
public class Performance {
    
    private Date clientProcessing;
    private Date clientRequest;
    private Date serverProcessing;
    private Date serverResponse;
    
}

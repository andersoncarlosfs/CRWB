/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.andersoncarlosfs.model.entities;

import java.io.Serializable;
import java.math.BigInteger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author anderson
 */
@Entity
@Table(name = "users_roles", catalog = "CRWB", schema = "public")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UsersRoles.findAll", query = "SELECT u FROM UsersRoles u")
    , @NamedQuery(name = "UsersRoles.findByIdUser", query = "SELECT u FROM UsersRoles u WHERE u.idUser = :idUser")
    , @NamedQuery(name = "UsersRoles.findByRole", query = "SELECT u FROM UsersRoles u WHERE u.role = :role")})
public class UsersRoles implements Serializable {

    private static final long serialVersionUID = 1L;
    @Column(name = "id_user")
    private BigInteger idUser;
    @Size(max = 2147483647)
    @Column(length = 2147483647)
    private String role;

    public UsersRoles() {
    }

    public BigInteger getIdUser() {
        return idUser;
    }

    public void setIdUser(BigInteger idUser) {
        this.idUser = idUser;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    
}

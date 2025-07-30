package com.nikhil.userauthentication.models;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class User extends Base
{
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

    @ManyToMany
    private List<Role> role;
}

package com.example.jeff.jeff23andme;

import java.io.Serializable;

/**
 * Created by jeff on 11/22/17.
 */

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;

    private String emailId;

    public Student() {

    }
    public Student(String name, String emailId) {
        this.name = name;
        this.emailId = emailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}

package com.sukritapp.smartshoppr.model;

/**
 * Created by hp on 28-Jun-17.
 */

public class ContactModel {
    private String name="";
    private String contact="";
    private String relation="";

    public ContactModel(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public String getContact() {
        return contact;
    }

    public String getRelation() {
        return relation;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}

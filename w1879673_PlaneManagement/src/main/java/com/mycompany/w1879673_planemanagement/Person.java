/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.w1879673_planemanagement;

import java.util.UUID;

/**
 *
 * @author NORTHDEALS
 */
public class Person {
    private String name;
    private String surname;
    private String email;
    private final String tk_id;  
    
    public Person(String name, String surname, String email){
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.tk_id = UUID.randomUUID().toString();
    }
    
    // Get and Setter for Name
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    
    // Getter and Setter for Surname
    public String getSurname(){
        return surname;
    }
    public void setSurname(String surname){
        this.surname = surname;
    }
    
    // Getter and Setter for Email
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getTicketID(){
        return tk_id;
    }
    
    public void showPerson(){
        
        System.out.println("tID: "+tk_id);
        System.out.println("Name: "+name+"\nSurname: "+surname+"\nEmail: "+email);
    }
    
    
    
}

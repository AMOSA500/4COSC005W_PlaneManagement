/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.w1879673_planemanagement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;


/**
 *
 * @author NORTHDEALS
 */
public class Ticket{
    private char row;
    private byte seat;
    private int price;
    private Person customer;
    static File filepath = new File (".\\src\\main\\resources\\ticket_files\\"); // file path

    
    public Ticket(char row, byte seat, int price, Person people){
        this.row = row;
        this.seat = seat;
        this.price = price;
        this.customer = people;
    }
    
    public Person getCustomer(){
        // Getter for Person object
        return customer;
    }
    public void showCustomer(){
        // Show Person information
        customer.showPerson();
    }
    
    public char getRow(){
        // Getter for row
        return row;
    }
    public void setRow(char row){
        // Setter for row
        this.row = row;
    }
    
    public byte getSeat(){
        // Getter for seat
        return seat;
    }
    public void setSeat(byte seat){
        // Setter for seat
        this.seat = seat;
    }
    
    public int getPrice(){
        // Getter for Price
        return price;
    }
    public void setPrice(int price){
        this.price = price;
    }
    
    public void ticketInformation(){
        // Display the customer ticket's information
        System.out.println("============Ticket Information:===============");
        System.out.println("Customer Details:");
        showCustomer(); // Call showCustomer() method
        System.out.println("Seat: " + row + seat);
        System.out.println("Price: " + price);
        
        
    }
    
    public static void save(Ticket ticket){
        /*
        * This method,
        * Write customer ticket information into a text file
        */
        // Variable declaration
        LocalDate dateTime = LocalDate.now(); // Get ticket sales date
        String filename = ticket.getRow()+""+ ticket.getSeat()+".txt"; // text file name
        String name = ticket.customer.getName()+" "+ticket.customer.getSurname();
        String seat = ticket.getRow()+""+ticket.getSeat();
        String txt_content = "Customer Details: \nDate: "+dateTime+
                            "\ntID: "+ ticket.customer.getTicketID()+
                            "\nName: "+name+
                            "\nSeat: "+seat+
                            "\nPrice: £"+ticket.getPrice(); // text content
        
        
        // Initialise a Bufferedwriter
        try{
            if(!filepath.exists()){
                filepath.mkdirs();
            }
            File file = new File(filepath+"\\"+filename);
            if(file.exists()){
                file.delete();
            }
            
            boolean isFile = file.createNewFile();
            if(isFile){
                try (FileWriter writeToFile = new FileWriter(file)) {
                    writeToFile.write(txt_content);
                }
                System.out.println("[File Saved to: src/main/resources/ticket_files/"+file.getName()+"]");
                
            }
            
            
            
        }
        catch(IOException e){ // Error handling catch
            System.out.println("Error writing to file: "+e.getMessage());
      }
    }
    
    
}

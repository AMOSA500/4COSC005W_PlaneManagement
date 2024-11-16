/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
   - Reference:https://stackoverflow.com/questions/13942701/take-a-char-input-from-the-scanner
   - Reference: https://www.geeksforgeeks.org/check-if-a-value-is-present-in-an-array-in-java/
   - Reference: https://www.tutorialspoint.com/system-getproperty-in-java
   - Reference: https://docs.oracle.com/javase/8/docs/api/java/util/UUID.html
 */
package com.mycompany.w1879673_planemanagement;



import static com.mycompany.w1879673_planemanagement.Ticket.filepath;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author NORTHDEALS
 */
public class W1879673_PlaneManagement {
    // Initialise a ticket sold array
    private static Ticket[] soldTickets = new Ticket[52];
     // Variable declaration and initialisation
    public static int[][] seatList = new int[4][14];
    public static char[] rowsList = {'A','B','C','D'};
    private static int index;
    private static Scanner input = new Scanner(System.in);
    
    
    public static void main(String[] args) {
    // Initialise scanner and variable declaratino
    int[] tempData = new int[2];// tempData contains row and seat number
    
    
        // Menu Options
        // REF: https://ascii.co.uk/art/airplane
        System.out.println("""


                            |
                      --====|====--
                            |
                        .-\'''''-.
                      .'_________'.
                     /_#_|__|__|_#_\

,--------------------|    `-. .-'    |--------------------,
``""--..__    ___   ;       '       ;   ___    __..--""``
           `"-## ##.._#             #_..## ##-"`
             \\_//    '._       _.'    \\_//
              `"`        ``---``        `"`
      WELCOME TO THE PLANE MANAGEMENT APPLICATION
                           """);

        String menu_option = """
**********************************************************
*                       MENU OPTIONS                     *
**********************************************************
      1) Buy a seat
      2) Cancel a seat
      3) Find first available seat
      4) Show seating plan
      5) Print tickets information and total sales
      6) Search ticket
      0) Quit
**********************************************************
Please select an option:""";
           
        // Main Program
        boolean is_running = true;
        while (is_running) {

            // Validation and error handling
            try {
                // Program menu
                System.out.println(menu_option);
                byte user_option = input.nextByte();

                if (user_option >= 1 && user_option <= 6) {
                    // Control flow statement
                    switch (user_option) {
                        case 1 -> {
                            // Buy a seat
                            if (inputValidation(input,tempData)) { // Validate user input
                                boolean reservation = buy_seat((char) tempData[0], tempData[1]);
                                if (reservation) {
                                    // Variable declaration
                                    char row = (char) tempData[0]; // Row letter
                                    byte seat = (byte) tempData[1];//seat number
                                    
                                    // Collect Person information
                                    String[] customer_info = new String[3]; // temp customer information array
                                    String[] placeholder = {"first name", "surname", "email"}; // text label array
                                    for (byte x = 0; x < placeholder.length; x++) {
                                        System.out.print("Enter your " + placeholder[x] + ": ");
                                        customer_info[x] = input.next();
                                    }
                                    
                                    // Create Person object
                                    String firstName = customer_info[0] ;
                                    String surname = customer_info[1]; 
                                    String email = customer_info[2];
                                    Person customer = new Person(firstName, surname, email);
                                    
                                    // Ticket information
                                    int ticketPrice = setTicketPrice(seat);// tempData[1] is seat number
                                    Ticket ticket = new Ticket(row, seat,ticketPrice, customer); // tempData[0] is row letter
                                    ticket.ticketInformation();
                                    
                                    // Store ticket sold in an array
                                    int tk_index = indexTicketSold(row, seat); // Get array index based on row and seat
                                    soldTickets[tk_index] = ticket; //soldTickets is an array of Ticket
                                    
                                    // Write to text file
                                    Ticket.save(ticket);// Calling the file writer
                                            
                                    // Sleep mode
                                    sleepmode();
                                }
                            }
                        }
                        case 2 -> {
                            // Cancel a seat
                            if (inputValidation(input,tempData)) {
                                // Variable declaration
                                char row = (char) tempData[0]; // Row letter
                                byte seat = (byte) tempData[1];//seat number
                                cancel_seat(row, seat);
                                // Delete customer purchase details from soldTickets
                                int seatIndex = indexTicketSold(row, seat);
                                soldTickets[seatIndex] = null; // Set Ticket sold arrays to null
                                sleepmode();

                            }
                            break;
                        }
                        case 3 -> {
                            // Find first available seat                            =
                            String message = find_first_available();
                            System.out.println(message);
                            sleepmode();
                            break;
                        }
                        case 4 ->{ // Show seating plan
                            show_seating_plan();
                            sleepmode();
                            break;
                        }
                        case 5 ->{ // Print tickets information and total sales
                            print_tickets_info();
                            sleepmode();
                            break;
                        }
                        case 6 -> {
                            // Search ticket
                            
                            if (inputValidation(input,tempData)) {
                                // Variable declaration
                                char row = (char) tempData[0]; // Row letter
                                byte seat = (byte) tempData[1];//seat number
                                search_ticket(row, seat);
                                sleepmode();
                            }
                            break;
                        }
                    }

                } else if (user_option == 0) {
                    System.out.println("Goodbye! Closing booking now...");
                    input.close();
                    is_running = false; // set flag to false if user option is 0
                } else {
                    System.out.println("Enter an integer value [0 - 6]");
                }
            } catch (InputMismatchException e) {
                input.nextLine(); // Prevent infinite loop. Used ChatGPT to solve this section

            }
        }
    }
    
  
    /**
     *  Buy a seat method
     * @param row_letter
     * @param seat_number
     * @return 
     */
    public static boolean buy_seat(char row_letter, int seat_number){
        /*
        This method
        - Stores ticket sales based on row letter and seat number
        - It checks if the seat exist 
        - if seat is taken, it displays a message to the customer
        - Finally, it returns true if seat is available and false if seat is sold.
        */        
        //Set the index position of row letter
        for(int x = 0; x < rowsList.length; x++){ 
            if(rowsList[x] == row_letter){
                index = x; 
            }
        }
        // Validation - Check if seat is available
        if(seatList[index][seat_number-1]  == 0){
            // The seat is available
            seatList[index][seat_number-1] = 1;
            System.out.println("Congratulations! Seat Reserved: "+row_letter+""+seat_number);
            return true;

        }else{
            System.out.println(row_letter+""+seat_number+" - is NOT available");
            return false;
        }
    }
    
    
    public static void cancel_seat(char row_letter, int seat_number){
        /**
        *  Cancel seat reservation
        * @param row_letter
        * @param seat_number
        */
        // Get the index position of row letter
        index = Arrays.binarySearch(rowsList, row_letter);
        
        // Validation - Check if seat is available
        if (seatList[index][seat_number-1]  == 1) {
            // The seat is available
            seatList[index][seat_number-1] = 0;
            // Delete file from the directory
            String filename = Character.toUpperCase(row_letter)+""+seat_number+".txt"; // text file name
            File file = new File(filepath+"\\"+filename);
            System.out.println("Check File: "+file);
            boolean isExist = file.exists();
            if(isExist){
                file.delete();
                System.out.println("[File Deleted]");
            }
            System.out.println(row_letter+""+seat_number+" Seat now available");
        } else {
            System.out.println(row_letter+""+seat_number+" - seat NOT found");
        }
        
    }
    
    
    public static String find_first_available(){
        /**
        * Find first available seat and return the string
        * @return 
        */
        for(int i = 0; i < seatList.length; i++){ 
            for(int j = 0; j < (seatList[i].length); j++){
                if(seatList[i][j] == 0){
                    return "["+rowsList[i]+""+(j+1)+"] is the first available seat";
                }
            }
               
        }
        return "All seat have been TAKEN";
    }
    
   
    public static void show_seating_plan(){ 
        /**
        * Show Seating Plan
        * This method shows the seats that are available
        * 
        */
        
        // Show available seat on same row
        System.out.println("Available seats: ");
        for(int i = 0; i < seatList.length; i++){
            int bc;// Set the number of seats on b & c to 12
            if(i == 1 || i == 2){
                bc = 2;
            }else{
                bc = 0;
            }
            for(int j = 0; j < (seatList[i].length-bc); j++){
                if(seatList[i][j] == 0){
                    System.out.print("[O] ");
                }else{
                    System.out.print("[X] ");
                }
            }
            System.out.println("");
        }
        
        
    }
    
    
    
    public static void print_tickets_info(){
        /**
        * This method print the sales tickets and the sum total of tickets
        * Print Ticket Information
        */
        int total_sales = 0;
        for(int i = 0; i < seatList.length; i++){
            int bc;// Set the number of seats on b & c to 12
            if(i == 1 || i == 2){
                bc = 2;
            }else{
                bc = 0;
            }
            for(int j = 0; j < (seatList[i].length-bc); j++){
                if(seatList[i][j] == 0){
                    
                }else{
                    int getIndex = indexTicketSold(rowsList[i], j+1); // Total seats is 52
                    int price = setTicketPrice((byte) (j+1));
                    Ticket ticket = soldTickets[getIndex];
                    ticket.ticketInformation();
                    total_sales += price;
                }
            }
            
        }
        System.out.println("----------------------------------");
        System.out.println("Total Sales: "+ total_sales);
        System.out.println("----------------------------------");
        
    }
    
    
    public static void search_ticket(char row_letter, byte seat_number){
        /**
        * Search Ticket
        * This method search for sold and if not found, displays seat available
        * @param row_letter
        * @param seat_number 
        */
        // Get the index position of row letter
        index = Arrays.binarySearch(rowsList, row_letter);
        
        // Suggest available seat on same row
        System.out.println("Searching... ");
        if(seatList[index][seat_number-1] == 1){
            
            int setIndex = indexTicketSold(rowsList[index], seat_number); // Total seats is 52
            Ticket ticket = soldTickets[setIndex];
            ticket.ticketInformation();
                    
        }else{
            String seat = row_letter+""+seat_number;
            System.out.println(seat+": This seat is available");
        }
        
        
            
    }
    
    
    
    
    public static int setTicketPrice(byte seat){
        /**
        * Sets the Ticket Price
        * This method return the ticket price
        * @param seat
        * @return
        */
        // This method set the price for ticket sales
        int seat_price;
        if(seat >= 0 && seat <= 5){
            seat_price = 200;
        }else if(seat >= 6 && seat <= 9){
            seat_price = 150;
        }else{
            seat_price = 180;
        } 
        return seat_price;
    }
    
    
    
    public static boolean inputValidation(Scanner input, int[] tempData) {
        /**
        * User Input Validation
        * @param input
        * @param tempData
        * @return
        * This method 
        - Ask for row letter and check if it is valid
        - Ask for customer preferred seat number and validate it
        - Validate the available seats for row B & C (not greater than 12 seats)
        - Store row letter and seat number in a temporary data array (tempData[])
        - Finally, if validations is complete and true, the method returns true.
        */
        
        // Variable declaration
        char row_letter = 0;
        byte seat_number;
        boolean isError = false;// Reset the flag
        byte row_pos = -1;
        byte counter = 0;
        
        // Inner loop
        while(!isError){
            if(row_pos < 0){
                
                System.out.println("Enter row letter [A,B,C,D]");
                row_letter = input.next().charAt(0); // Get the first character of user input
                row_letter = Character.toUpperCase(row_letter); // Set char to uppercase
                // Check if row letter exist
                row_pos = (byte) Arrays.binarySearch(rowsList, row_letter);
                
            }
            
            if (row_pos >= 0) {
                // User seat number
                try{
                    System.out.println("Enter seat number [1 - 14]");
                    seat_number = input.nextByte();
                    // Validate row B and C with 12 seats
                    if ((row_pos == 1 || row_pos == 2) && (seat_number > 12)) { 
                        System.out.println("Row " + rowsList[row_pos] + " has only 12 seats");
                    } else if (seat_number >= 1 && seat_number <= 14) {
                        tempData[0] = rowsList[row_pos]; // if row letter is valid store it in a temp data array
                        tempData[1] = seat_number; // if seat number is valid
                        return true;
                    } else {
                        System.out.println("Error!!! Invalid seat number: [" + seat_number + "]");
                    }
                }catch(InputMismatchException e){
                    counter++;
                    if(counter >= 3){
                        System.out.println("Too many incorrect entries. Validation failed");
                        isError = true;
                    }
                    System.out.println("Invalid number entered");
                    input.nextLine();
                   
                    
                    
                }     
            } else{
                System.out.println("Error!!! Invalid row letter [" + row_letter + "]");
            }
        }
        return false;
    }
    
    
    
    public static int indexTicketSold(char row, int seat){
        /**
        * Set the array index to store ticket information in soldTickets[]
        * @param row
        * @param seat
        * @return
        */
        /*
        This method set the index position to store Person and ticket sold in an array of 52 blocks
        */
        byte a = 14;
        byte b = 12, c = 12;
        int row_pos = 0;
        switch (row) {
            case 'A' -> row_pos = seat;
            case 'B' -> row_pos =(a + seat);
            case 'C' -> row_pos =(a + b + seat);
            case 'D' -> row_pos =(a + b + c + seat);
            default -> {
            }
        }
        return row_pos;
        
    }
    
    private static void sleepmode(){
        // Wait for Enter key press to continue
        System.out.println("Press Enter to continue...");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine(); // Wait for Enter key press
    }
    

    
}

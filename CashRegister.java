// Homework 1: Sales Register Program
// Course: CIS357
// Due date: 7/8/2022
// Name: Erik Rokita
// GitHub: https://github.com/erikrokita/cis357-hw1-rokita
// Instructor: Il-Hyung Cho
// Program description: ...

import java.util.Scanner;
import java.io.*;

/** This class is where our main program begins. it asks the user a series of questions about a grocery list, and automatically computes totals and stores sales until the program is closed */
public class CashRegister {
    public static void main(String args[]) throws IOException{

        //setup scanner so user can input data into command line
        Scanner scanner = new Scanner(System.in);
        String input;

        //initialize misc data variables
        float totalSales = 0;
        int saleNum = 0;

        //initialize an array of item class objects to store our transactions
        Item[] inventory = ReadFile("itemlist.txt");
        Sales[] saleRecord = new Sales[25];     //limit ourselves to 25 sales because i can't use array lists to make it dynamic and i already programmed that custom logic once and it wasn't worth it

        //check if the inventory is empty. if it is, then either there are no entries in the inventory or the file does not exist. Exit the program from here
        if (inventory.length == 0){
            System.out.println("Inventory is empty! Either no items exist in the file or the file does not exist! Exiting.");
            scanner.close();
            return;
        }

        System.out.println("Welcome to rokita cash register system!");

        //loop main functionality of the program
        do{
            System.out.print("Begin a new sale? (Y/N): ");
            input = scanner.next(); //get input from user
            System.out.println("--------------------");

            saleRecord[saleNum] = new Sales();  //create a new sale

            //prompt the user if they wish to begin a new sale. this line will check for errors
            if (input.toLowerCase().equals("y") || input.toLowerCase().equals("n")){

                //if the user decides to not begin a new entry, break out of this loop and finish the program
                if (input.toLowerCase().equals("n")){
                    break;
                }

                boolean valid = true;

                //begin loop for adding products to our inventory
                do{
                    valid = true;   //reset
                    System.out.print("Enter Product Code: ");
                    input = scanner.next(); //get input from user

                    //check if it is a valid number
                    if (!CheckInput(input)){
                        System.out.println("Invalid input. Try again");
                        valid = false;
                        continue;
                    }


                    if (Integer.parseInt(input) != -1){
                        //check if the item exists in our array
                        Item item = null;
                        for (int i = 0; i < inventory.length; i ++){
                            if (inventory[i].GetItemCode() == Integer.parseInt(input)){
                                item = inventory[i];
                                break;
                            }
                        }

                        //if we found the item, continue. otherwise, give error message
                        if (item != null){
                            System.out.println("    Item name: " + item.GetItemName());

                            do{     //used for error checking
                                System.out.print("Enter quantity: ");
                                input = scanner.next(); //get input from user
    
                                //check if it is a valid number
                                if (!CheckInput(input)){
                                    System.out.println("Invalid input. Try again");
                                }
                            }while(!CheckInput(input));

                            System.out.println("    item total: " + item.GetUnitPrice() * Integer.parseInt(input));
                            System.out.println("");     //line seperator between products

                            //we add this information to our sales array, and we'll compute the rest of the sale when we've finished adding products
                            saleRecord[saleNum].AddItem(item, Integer.parseInt(input));
                        }else{
                            System.out.println("    Item not found! Please enter a valid code!");
                        }
                    }
                }while(!valid || Integer.parseInt(input) != -1);

                //complete checkout logic
                //convert our sale into a list of items
                int[][] itemList = GetItemQuantities(saleRecord[saleNum]);
                System.out.println("--------------------");
                System.out.println("Item List:");

                //display our item list
                for (int i = 0; i < itemList.length; i ++){
                    //if we hit our first empty value, we know the rest of the array is empty so we break
                    if (itemList[i][0] == 0){
                        break;
                    }
                    Item item = GetItemFromCode("itemlist.txt", itemList[i][0]);
                    System.out.println("    " + itemList[i][1] + " " + item.GetItemName() + " " + item.GetUnitPrice() * itemList[i][1]);
                }
                System.out.println("Subtotal: " + saleRecord[saleNum].GetSubTotal());
                System.out.println("Total with Tax (6%): " + String.format("%.2f", saleRecord[saleNum].GetTotalWithTax()));
                
                //ask for amount in money
                do{
                    System.out.print("Payment Amount: ");
                    input = scanner.next(); //get input from user

                    valid = true;   //reset

                    try{
                        Double.parseDouble(input);
                    }catch(Exception e){
                        System.out.print("Invalid input. Try again");
                        valid = false;
                        continue;
                    }

                    //if user entered a sufficient amount, continue. otherwise, retry
                    if (Float.parseFloat(input) < saleRecord[saleNum].GetTotalWithTax()){
                        System.out.println("Insufficient funds. Try again");
                    }
                }while (!valid || Float.parseFloat(input) < saleRecord[saleNum].GetTotalWithTax());

                System.out.println("Change: " + String.format("%.2f", (Float.parseFloat(input) - saleRecord[saleNum].GetTotalWithTax())));
                System.out.println("--------------------");

                saleNum ++;
                
            }else{
                System.out.println("Invalid entry!");
            }

        //this is set to true so that we always continue the program. We only exit this loop with a break statement at the beginning of this loop code
        }while(true);

        //get total sales made
        for (int i = 0; i < saleRecord.length; i ++){
            //if we hit our first empty value, we know the rest of the array is empty so we break
            if (saleRecord[i] == null){
                break;
            }
            totalSales += saleRecord[i].GetTotalWithTax();
        }

        //output the total sales made for the day, and exit program
        System.out.println("Total sales made today: " + String.format("%.2f", totalSales));
        System.out.println("Thank you for using this cash register system");

        scanner.close();
    }

    /** Function used to read the file of the supplied filename and places its contents into an array of Items. accepts the file name, returns all items from the file */
    public static Item[] ReadFile(String fileName) throws IOException{
        File file = new File(fileName);

        //check to make sure the file exists before proceeding. If not, return an empty array
        if (!file.exists()){
            return new Item[0];
        }

        Scanner fileReader = new Scanner(file);

        int lineNum = 0;    //this is exclusively used for our file line validation to keep track of what line we are in
        Item[] tempArr = new Item[25];   //this will temperarily hold our data and will be transfered and trimmed later

        while (fileReader.hasNextLine()){
            //splits the data into an array of strings for each occurance of a comma
            String[] data = fileReader.nextLine().split(",");
            
            //check the line we just read for any errors. If the line has an error, skip the line and read the next
            if (ValidateFile(data, lineNum) == false){
                lineNum ++; //increment line counter
                continue;
            }

            tempArr[lineNum] = new Item(Integer.parseInt(data[0]), data[1], Float.parseFloat(data[2]));

            lineNum ++; //increment line counter
        }

        int finalArrLength = 0; //holds how big the final array should be to perfectly fit our data

        //trim the temp array to proper length and return
        for (int i = 0; i < tempArr.length; i ++){
            //loop through our array and incriment for how many items are stored, not counting null slots in the array
            if (tempArr[i] != null){
                finalArrLength ++;
            }
        }

        //create our array of perfect size
        Item[] inventory = new Item[finalArrLength];

        //loop through the temp array again (i know it's painful) to transfer our data
        for (int i = 0; i < tempArr.length; i ++){
            //transfer our items to our final array, skipping all null spots
            if (tempArr[i] != null){
                inventory[i] = tempArr[i];
            }
        }

        //close scanner
        fileReader.close();

        return inventory;
    }

    /** Function used to error-check each line of the file as we read each line. accepts the file current line and the line number, returns whether the line is valid */
    public static boolean ValidateFile(String[] line, int lineNum){
        Boolean safe = true;

        //check each line to see if it follows our format. if not, mark line as unsafe so it can be skipped
        try{
            Integer.parseInt(line[0]);  //checks first entry for an integer value, our item code
            //we ignore the second entry because the name can be whatever string we want
            Float.parseFloat(line[2]);  //checks last entry for a float value, our price
        }catch(Exception ex){
            System.out.println("There was a problem reading line #" + lineNum + " in the supplied file!");
            safe = false;
        }

        return safe;
    }

    /**This function return a list that contains one copy of an item (via item code), along with its quantity and item total. used only at the end of a sale. accepts a sales object, returns a 2d array of items + their quantities */
    public static int[][] GetItemQuantities(Sales sale){
        int[][] listOfItems = new int[200][2];
        Item[] itemList = sale.GetItemList();   //this just make it easier to work with

        //initialize our listOfItems array with initial values
        for (int i = 0; i < listOfItems.length; i ++){
            listOfItems[i][0] = 0;
            listOfItems[i][1] = 0;
        }
        
        //begin searching through the array and storing the item code in the first array slot, and the quantity in the second slot
        for (int i = 0; i < itemList.length; i ++){
            //if we've hit our first null entry, we know the rest of the array is null, so we break
            if (itemList[i] == null){
                break;
            }

            boolean hasEntry = false;   //used to determin if we should add the item to our array

            //check if the item already exists in our current list. If not, add it. If so, incriment the quantity
            for (int j = 0; j < listOfItems.length; j ++){
                //if we've hit our first empty, we know the rest of the array is empty, so we break
                if (listOfItems[j][0] == 0){
                    break;
                }

                //if the item exists, incriment the quantity and set flag, then break
                if (itemList[i].GetItemCode() == listOfItems[j][0]){
                    listOfItems[j][1] ++;
                    hasEntry = true;
                    break;
                }
            }

            //check the flag, and decide whether to add the item to the array
            if (hasEntry == false){
                //search through the internal items array until we find an empty slot. then add item to the first empty slot
                int j = 0;
                while (listOfItems[j][0] != 0 && j < 199){
                    j ++;
                }

                listOfItems[j][0] = itemList[i].GetItemCode();
                listOfItems[j][1] ++;
            }
        }

        return listOfItems;
    }

    /**Returns the name of the item given just the code. returns the appropriate item. accepts the file name and the grocery item code, returns the appropriate item */
    public static Item GetItemFromCode(String fileName, int code) throws IOException{
        File file = new File(fileName);
        Item item = null;

        //we don't need to check if the file exists. if it does not exist, we would never execute this function in our main program
        Scanner fileReader = new Scanner(file);

        while (fileReader.hasNextLine()){
            //splits the data into an array of strings for each occurance of a comma, so we can get just the first entry
            String[] data = fileReader.nextLine().split(",");
            
            //if we have a match, break
            if (code == Integer.parseInt(data[0])){
                item = new Item(code, data[1], Float.parseFloat(data[2]));
                break;
            }
        }

        fileReader.close();

        return item;
    }

    /**all this does is check if a value entered is of a valid type or not and handles exceptions accordingly. accepts the user input. returns a bool based on if the input is valid */
    public static boolean CheckInput(String input){
        boolean valid = true;

        try{
            Integer.parseInt(input);
        }catch(Exception e){
            valid = false;
        }

        return valid;
    }
}

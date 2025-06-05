import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ConsoleKitchen {

    public static void main(String[] args) {
        Scanner hungry = new Scanner(System.in);  // Input scanner for user commands
        double total = 0.0;       // Total amount for the order
        double subTotal = 0.0;    // Subtotal before tax and fees
        String orderedItems = ""; // String to store ordered items
        int itemCount = 0;        // Track number of items in the cart

        boolean youHungry = false;  // Initialize variable
        boolean validInput = false; // tracks if input is valid

        // makes sue user inputs a valid boolean value
        while (!validInput) {
            try {
                System.out.println("Are you hungry? Type 'true' or 'false':");
                youHungry = hungry.nextBoolean(); // Check if user is hungry
                hungry.nextLine(); // Removes the leftover newline from the previous input so the next one works correctly.
                validInput = true; // If input is valid, break the loop
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input! Please enter 'true' or 'false'."); // Error message for invalid boolean input
                hungry.nextLine(); // Clear the invalid input
            }
        }

        if (youHungry) {  // Check if the user is hungry
            boolean continueOrdering = true;

            // Show menu until the user chooses to exit
            while (continueOrdering) {
                System.out.println("\nPlease choose one:");
                System.out.println("1. Place an order");
                System.out.println("2. View your order");
                System.out.println("3. Clear your order");
                System.out.println("4. Checkout your order");
                System.out.println("5. Exit");
                System.out.println("----------------------------------------");

                String choiceInput = hungry.nextLine(); // Read the user's choice
                int choice = 0;

                // This tries to change what the user typed into a number. If the user types something that isn't a number, it will show an error
                try {
                    choice = Integer.parseInt(choiceInput); 
                } catch (NumberFormatException e) {
                    System.out.println("Invalid choice, please select a valid option (1-5).");
                    continue; // Go back to the menu
                }

                switch (choice) {  // Handle the different case choices
                    case 1: // Show menu and take order
                        try {
                            File menuFile = new File("food.txt");  // Read menu file
                            Scanner fileScanner = new Scanner(menuFile);
                            System.out.println("****Here is our menu:****");
                            String menuText = ""; // Store menu contents

                            while (fileScanner.hasNextLine()) {  // Read each line from the menu file
                                String line = fileScanner.nextLine();  // Read a line from the file
                                menuText += line + "\n"; // Add valid menu items to display
                            }

                            System.out.println(menuText);  // Show the entire menu
                            fileScanner.close(); // Close the file after reading

                            // Ask what the user wants to order
                            System.out.println("Please type the exact name of the item you want to order:");
                            String menuChoice = hungry.nextLine();

                            fileScanner = new Scanner(menuFile); // Reopen the file to search for the item
                            boolean itemFound = false;

                            while (fileScanner.hasNextLine()) {  // Search for the item selected by user in the file
                                String line = fileScanner.nextLine();
                                String itemName = "";  // Initialize item name and price variables
                                String itemPriceText = "";
                                boolean pricePart = false;

                                for (int i = 0; i < line.length(); i++) {  // This will separate item name and price
                                    char currentChar = line.charAt(i); 
                                    if (!pricePart) {
                                        if (currentChar == ' ') {
                                            pricePart = true; // Start reading the price part
                                        } else {
                                            itemName += currentChar; // Add to item name
                                        }
                                    } else {
                                        if (currentChar != ' ') {
                                            itemPriceText += currentChar; // Add to price
                                        }
                                    }
                                }

                                double itemPrice = 0.0;  // Convert price in the .txt file to number
                                try {
                                    itemPrice = Double.parseDouble(itemPriceText); 
                                } catch (NumberFormatException e) {
                                    continue; // Skip lines without valid prices
                                }

                                boolean isMatch = itemName.equals(menuChoice); // Compare using .equals()

                                if (isMatch) {  // If item is found, take the quantity and add to order
                                    System.out.println("You selected " + itemName + " for $" + itemPrice);
                                    System.out.println("How many of this item would you like to add?");
                                    try {
                                        // this tries to read the quantity as a number, and if the user types something that’s not a number, it shows an error message instead of crashing.
                                        int quantity = Integer.parseInt(hungry.nextLine());  
                                        double itemTotal = itemPrice * quantity; // Calculate total for the item
                                        System.out.println("Added " + quantity + " x " + itemName + " for a total of $" + itemTotal);

                                        subTotal += itemTotal; // Update subtotal
                                        itemCount += quantity; // Update item count
                                        total = subTotal; // Set total before tax and fees
                                        orderedItems += quantity + " x " + itemName + " ($" + itemPrice + " each)\n";
                                        itemFound = true; // Item was found
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid quantity. Please enter a number.");
                                    }
                                    break; // Exit loop
                                }
                            }

                            fileScanner.close();
                            //If user doesnt pick something from the list it will print this
                            if (!itemFound) {
                                System.out.println("Sorry, the item you requested is not on the menu.");
                            }
                        } catch (FileNotFoundException e) {
                            System.out.println("Menu file not found. Exiting the program.");
                            return; // Exit the program immediately
                        }
                        break; // End of case 1

                    case 2: // View and shows current order that the user picks
                        if (orderedItems.length() == 0) { // Check if the order is empty
                            System.out.println("Your order is currently empty.");
                        } else {
                            System.out.println("*****Here is your current order*****");
                            System.out.println(orderedItems);
                            System.out.println("Total items: " + itemCount);
                            System.out.println("Subtotal so far: $" + subTotal);
                        }
                        break; // End of case 2

                    case 3: // Clear the order
                        System.out.println("Are you sure you would like to clear your cart? Type 'yes' or 'no'");
                        String confirmation = hungry.nextLine();
                        if (confirmation.equals("yes")) { // If user says yes, then it would clear the order
                            System.out.println("Ok, your cart has been cleared.");
                            // this turns everything back to 0
                            total = 0.0;
                            subTotal = 0.0;
                            itemCount = 0;
                            orderedItems = ""; // Clear order data
                        } else { // If user says no, keep the order unchanged and go back to the menu
                            System.out.println("Cart not changed. Going back to the menu.");
                        }
                        break; // End of case 3

                    case 4: // this is the checkout option
                        if (orderedItems.length() == 0) { // Check if the cart is empty
                            System.out.println("Your cart is empty. Please add items before checking out.");
                        } else {
                            System.out.println("Let's checkout your order:");
                            System.out.println(orderedItems);
                            
                            double tax = subTotal * 0.0725; // Calculate tax on order
                            total += tax;
                            
                            System.out.println("Subtotal: $" + subTotal);
                            System.out.println("Tax (7.25%): $" + tax); // Display formatted tax
                            
                            System.out.println("Would you like pickup or delivery? Type 'pickup' or 'delivery'");
                            String deliveryChoice = hungry.nextLine();
                            
                            if (deliveryChoice.equals("delivery")) { // If delivery, add $5 fee
                                System.out.println("Delivery fee: $5.00");
                                total += 5.0;

                             // Ask user for delivery address
                                System.out.println("Please enter your delivery address:");
                                // Read the delivery address input from the user
                                String address = hungry.nextLine();
                                // Keep asking for the address until the user provides a valid (non-empty) input
                                while (address.trim().isEmpty()) {
                                    System.out.println("Address cannot be empty. Please enter a valid address:");
                                    address = hungry.nextLine();
                                }
                                // Print the valid delivery address

                                System.out.println("Delivery to: " + address);
                                
                               // [EXTRA CREDIT: 10 points] Ask if user wants to add a tip
                                System.out.println("Would you like to add a tip? Type 'yes' or 'no'");
                                String tipResponse = hungry.nextLine();

                                if (tipResponse.equalsIgnoreCase("yes")) { // If the user wants to tip
                                    System.out.println("What percentage would you like to tip? (e.g., 15 for 15%)");
                                    String tipInput = hungry.nextLine();
                                    try {
                                        double tipPercentage = Double.parseDouble(tipInput); // Convert input to a number
                                        double tipAmount = (subTotal * tipPercentage) / 100; // Calculate tip amount
                                        total += tipAmount; // Add the tip to the total
                                        System.out.println("Tip added: $" + tipAmount); // Display the full tip amount without truncating
                                    } catch (NumberFormatException e) {
                                        System.out.println("Invalid tip percentage entered. No tip added.");
                                    }
                                }


                            }
        
                            // Ask for payment and ensure only digits are entered
                            System.out.println("Please enter your 16-digit card number to complete the order:");
                            String cardNumber = hungry.nextLine();

                            // Keep asking until the card number is exactly 16 digits and contains only numbers
                            boolean isValid = false;
                            while (!isValid) {
                                if (cardNumber.length() == 16) {  // Check if it has 16 characters
                                    isValid = true;  // Assume it's valid unless we find a non-digit
                                    for (int i = 0; i < cardNumber.length(); i++) { // Check each character
                                        char c = cardNumber.charAt(i);// Get the character at position 'i' in the cardNumber string
                                        if (c < '0' || c > '9') {  // If any character is not a digit
                                            isValid = false;
                                            break;  // Exit the loop because the input is not valid
                                        }
                                    }
                                }

                                if (!isValid) {  // If the number is not valid, ask for input again
                                    System.out.println("Invalid card number. Please enter a 16-digit number without spaces or letters:");
                                    cardNumber = hungry.nextLine();
                                }
                            }
                            // Confirm that the order has been placed
                            System.out.println("Thank you! Your order has been placed.");
                            System.out.println("Order summary:");// Display the order summary to the user
                            System.out.println("Total items: " + itemCount);// Show total number of items ordered
                            System.out.println("Final total: $" + total);// Show the final amount to be paid
                            System.out.println("Card number: " + cardNumber); // Display the card number used
                            
                            // Reset all order details after checkout
                            total = 0.0;
                            subTotal = 0.0;
                            orderedItems = "";
                            itemCount = 0;
                        }
                        break; // End of case 4

                    case 5: // This option exit the program
                        System.out.println("Thank you for eating at Console Kitchen! Goodbye!");
                        continueOrdering = false; // Exit the loop and end the program
                        break; // End of case 5

                    default: // Invalid choice
                        System.out.println("Invalid choice, please select a valid option (1-5).");
                        break; // End of default case
                }
            }
        } else {  // User is not hungry and chooses to exit
            System.out.println("Thank you for visiting Console Kitchen! Goodbye!");
        }

        hungry.close(); // Close the scanner at the end of the program
    }
}//ends everything}
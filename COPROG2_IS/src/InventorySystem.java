import java.io.*;
import java.util.*;

public class InventorySystem {

    private static final Scanner sc = new Scanner(System.in);

    private static final String ADMIN_FILE_PATH = "admin_credentials.txt";

    private static boolean verifyAdminCredentials (String username, String password) {

        try (BufferedReader fileReader = new BufferedReader(new FileReader(InventorySystem.ADMIN_FILE_PATH))) {
            String storedUsername = fileReader.readLine();
            String storedPassword = fileReader.readLine();
            return username.equals(storedUsername) && password.equals(storedPassword);
        } catch (IOException e) {
            System.out.println("An error occurred while verifying admin credentials: " + e.getMessage());
        }
        return false;
    }

    public static void displayAdminMenu () {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String choice;

            while (true) {
                System.out.println("===========================================================================");
                System.out.println("\t\tWelcome to Inventory Console System");
                System.out.println("===========================================================================");
                System.out.println("\t\t\t     Admin Menu");
                System.out.println("===========================================================================");
                System.out.println("|| A. Create User");
                System.out.println("|| B. View Inventory");
                System.out.println("|| C. Add Item");
                System.out.println("|| D. Add Item Quantity");
                System.out.println("|| E. Edit Item");
                System.out.println("|| F. Logout");
                System.out.println("|| G. Exit");

                System.out.print("Enter your choice: ");
                try {
                    choice = reader.readLine();
                } catch (IOException e) {
                    System.out.println("An error occurred while reading input: " + e.getMessage());
                    continue;
                }

                try {
                    switch (choice.toUpperCase()) {
                        case "A":
                            createCashierUser();
                            break;
                        case "B":
                            viewInventory();
                            break;
                        case "C":
                            addItem();
                            break;
                        case "D":
                            addItemQuantity();
                            break;
                        case "E":
                            editItem();
                            break;
                        case "F":
                            System.out.println();
                            System.out.println("Logging out...");
                            loginMenu();
                            System.exit(0);
                            break;
                        case "G":
                            System.out.println("Exiting program...");
                            System.exit(0);
                            break;
                        default:
                            System.out.println("Invalid choice. Please try again.");
                            break;
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("An unexpected error occurred: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while initializing input reader: " + e.getMessage());
        }
    }

    public static void goBackToMenu (BufferedReader reader) {
        try {
            while (true) {
                System.out.print("Do you want to go back to the Admin menu? (Y/N): ");
                String gobackMenu = reader.readLine();

                if (gobackMenu.equalsIgnoreCase("Y")) {
                    displayAdminMenu();
                    break;
                } else if (gobackMenu.equalsIgnoreCase("N")) {
                    System.out.println("Exiting program...");
                    System.exit(0);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    private static final String USER_FILE_PATH = "user_credentials.txt";

    private static void createCashierUser () {
        try {
            boolean saveMoreCashier = true;
            while (saveMoreCashier) {
                System.out.println("===========================================================================");
                System.out.println("\t\t\t     Create User");
                System.out.println("===========================================================================");
                System.out.print("|| Username: ");
                String cashierUser = sc.nextLine();
                System.out.print("|| Password: ");
                String cashierPass = sc.nextLine();

                // save cashier account data to a text file <cashier_accounts.txt>
                saveCashierUser(cashierUser, cashierPass);

                System.out.print("Do you want to add more cashier accounts? (Y/N): ");
                String choice = sc.nextLine();
                if (choice.equalsIgnoreCase("N")) {
                    System.out.println("===========================================================================");
                    System.out.println("\t\tUser account(s) saved successfully!");
                    System.out.println("===========================================================================");
                    System.out.print("Do you want to go back to the menu? (Y/N): ");
                    String gobackMenu = sc.nextLine();
                    if (gobackMenu.equalsIgnoreCase("Y")) {
                        displayAdminMenu();
                    } else if (gobackMenu.equalsIgnoreCase("N")) {
                        System.out.println("Exiting program...");
                        System.exit(0);
                    } else {
                        System.out.println("Invalid choice. Exiting...");
                    }
                    saveMoreCashier = false;
                }
            }
            sc.close();

        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // saveCashierUser - it saves the cashier user created by the admin
    private static void saveCashierUser (String cashierUser, String cashierPass) throws IOException {
        if (cashierUser.matches("[a-zA-Z0-9]+") && cashierUser.matches("[a-zA-Z0-9]+")) {

            if (isUsernameAlreadyExists(cashierUser)) {
                System.out.println("===========================================================================");
                System.out.println("\t   Username already exists. Cannot create account.");
                System.out.println("===========================================================================");
                return;
            }

            FileWriter fileWriter = null;
            try {
                fileWriter = new FileWriter("user_credentials.txt", true);
                fileWriter.write(cashierUser + "," + cashierUser + "\n");
            } finally {
                if (fileWriter != null) {
                    try {
                        fileWriter.close();
                    } catch (IOException e) {
                        System.out.println("An error occurred while closing the file: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("Cannot create account. Invalid username or password.");
        }
    }

    private static boolean isUsernameAlreadyExists (String username) throws IOException {
        try (BufferedReader fileReader = new BufferedReader(new FileReader("user_credentials.txt"))) {
            String line;
            while ((line = fileReader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equalsIgnoreCase(username)) {
                    return true;
                }
            }
        }
        return false;
    }

    // it detects if the user and pass of the cashier is correct
    private static boolean verifyCashierCredentials (String username, String password) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(InventorySystem.USER_FILE_PATH))) {
            String line;

            while ((line = fileReader.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equalsIgnoreCase(username) && userInfo[1].equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while verifying cashier credentials: " + e.getMessage());
        }

        return false;
    }

    public static void viewInventory () {
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory_list.txt"))) {
            System.out.println("===========================================================================");
            System.out.println("\t\t\t   Inventory List");
            System.out.println("===========================================================================");
            // Print headers
            System.out.printf("%-15s %-25s %-10s %-10s%n", "Product No.", "Item Name", "Quantity", "Price");
            System.out.println("---------------------------------------------------------------------------");

            String line;
            while ((line = reader.readLine()) != null) {
                StringTokenizer tokenizer = new StringTokenizer(line, ",");
                if (tokenizer.countTokens() < 4) {
                    System.out.println("Error parsing inventory data: Incomplete data");
                    continue;
                }

                try {
                    int productNo = Integer.parseInt(tokenizer.nextToken().trim());
                    String itemName = tokenizer.nextToken().trim();
                    int quantity = Integer.parseInt(tokenizer.nextToken().trim());
                    double price = Double.parseDouble(tokenizer.nextToken().trim());
                    System.out.printf("%-15s %-25s %-10s %-10s%n", productNo, itemName, quantity, price);
                } catch (NumberFormatException e) {
                    System.out.println("Error parsing inventory data: Invalid number format");
                }
            }
            System.out.println("===========================================================================");
        } catch (FileNotFoundException e) {
            System.out.println("Error opening the inventory file: File not found");
        } catch (IOException e) {
            System.out.println("Error reading the inventory file: " + e.getMessage());
        }
    }

    public static void addItem () {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            int productNo = 0; // Default product number

            // Read the current product number from the file, if available
            try (BufferedReader fileReader = new BufferedReader(new FileReader("inventory_list.txt"))) {
                String lastLine = null;
                String currentLine;
                while ((currentLine = fileReader.readLine()) != null) {
                    lastLine = currentLine;
                }

                if (lastLine != null) {
                    String[] parts = lastLine.split(",");
                    productNo = Integer.parseInt(parts[0].trim());
                }
            } catch (IOException e) {
                System.out.println("An error occurred while reading the file: " + e.getMessage());
            }

            boolean addItem = true;
            while (addItem) {
                System.out.println("===========================================================================");
                System.out.println("\t\t\t     Add Item");
                System.out.println("===========================================================================");

                // Increment the product number
                productNo++;

                System.out.println("Product Number: " + productNo);

                System.out.print("Item Name: ");
                String itemName = reader.readLine();

                System.out.print("Item Quantity: ");
                int itemQuantity = Integer.parseInt(reader.readLine());

                System.out.print("Item Price: ");
                double itemPrice = Double.parseDouble(reader.readLine());

                // Pass the product number as a parameter
                saveInventoryItem(productNo, itemName, itemQuantity, itemPrice);

                System.out.print("Do you want to add more items? (Y/N): ");
                String choice = reader.readLine();
                addItem = choice.equalsIgnoreCase("Y");
            }
            System.out.println("===========================================================================");
            System.out.println("\t\t\tItem added successfully!");
            System.out.println("===========================================================================");
            System.out.print("Do you want to go back to the menu? (Y/N): ");
            String goBackMenu = reader.readLine();

            if (goBackMenu.equalsIgnoreCase("Y")) {
                // Display the menu options here
                displayAdminMenu();
            } else if (goBackMenu.equalsIgnoreCase("N")) {
                System.out.println("Exiting program...");
                System.exit(0);
            } else {
                System.out.println("Invalid choice. Exiting...");
            }

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void saveInventoryItem (int productNo, String itemName, int itemQuantity, double itemPrice) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_list.txt", true))) {
            writer.write(productNo + ", " + itemName + "," + itemQuantity + "," + itemPrice);
            writer.newLine();
            System.out.println("===========================================================================");
            // System.out.println("Item saved successfully!");
        } catch (IOException e) {
            System.out.println("An error occurred while saving the item: " + e.getMessage());
        }
    }

    public static void addItemQuantity () {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean updateQuantity = true;
            while (updateQuantity) {
                System.out.println("===========================================================================");
                System.out.println("\t\t\tAdd Item Quantity");
                System.out.println("===========================================================================");
                System.out.print("Enter product no.: ");
                String productNo = reader.readLine();

                System.out.print("Additional Quantity: ");
                int additionalQuantity = Integer.parseInt(reader.readLine());

                updateInventoryItemQuantity(productNo, additionalQuantity);

                System.out.println("===========================================================================");
                System.out.print("Do you want to add more item's quantity? (Y/N): ");
                String choice = reader.readLine();
                updateQuantity = choice.equalsIgnoreCase("Y");
            }
            System.out.println("===========================================================================");
            System.out.println("\t\t\tItem quantity updated successfully!");
            System.out.println("===========================================================================");
            viewInventory();
            goBackToMenu(reader);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    public static void updateInventoryItemQuantity (String productNo, int additionalQuantity) {
        try {
            File inputFile = new File("inventory_list.txt");
            File tempFile = new File("temp_inventory_list.txt");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

            String line;
            while ((line = reader.readLine()) != null) {
                String[] productInfo = line.split(",");
                if (productInfo.length == 4 && productInfo[0].trim().equalsIgnoreCase(productNo)) {
                    String itemName = productInfo[1].trim();
                    int currentQuantity = Integer.parseInt(productInfo[2].trim());
                    double price = Double.parseDouble(productInfo[3].trim());

                    int updatedQuantity = currentQuantity + additionalQuantity;

                    writer.write(productNo + "," + itemName + "," + updatedQuantity + "," + price);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }

            reader.close();
            writer.close();

            inputFile.delete();
            tempFile.renameTo(inputFile);

        } catch (IOException e) {
            System.out.println("An error occurred while updating the item quantity: " + e.getMessage());
        }
    }

    public static void editItem () {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            boolean editItem = true;
            while (editItem) {

                System.out.println("===========================================================================");
                System.out.println("\t\t\tEdit Item");
                System.out.println("===========================================================================");

                System.out.print("Product No.: ");
                int productNo = Integer.parseInt(reader.readLine());

                System.out.print("New Item Name: ");
                String newItemName = reader.readLine();

                System.out.print("New Price: ");
                double newPrice = Double.parseDouble(reader.readLine());
                System.out.println("===========================================================================");

                updateEditedItem(productNo, newItemName, newPrice);

                System.out.print("Do you want to edit more items? (Y/N): ");
                String choice = reader.readLine();
                if (!choice.equalsIgnoreCase("Y")) {
                    editItem = false;
                }
            }
            System.out.println("===========================================================================");
            System.out.println("\t\t\tItem updated successfully!");
            viewInventory();
            goBackToMenu(reader);
        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }

    // updateEditedItem - it saves the edited name and quantity of the items
    public static void updateEditedItem (int productNo, String newItemName, double newPrice) {
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory_list.txt")); BufferedWriter writer = new BufferedWriter(new FileWriter("updated_inventory_list.txt"))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int existingProductNo = Integer.parseInt(parts[0].trim());

                if (existingProductNo == productNo) {
                    writer.write(productNo + "," + newItemName + "," + parts[2].trim() + "," + newPrice);
                } else {
                    writer.write(line);
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the item: " + e.getMessage());
        }

        // Delete the original inventory file
        File inventoryFile = new File("inventory_list.txt");
        inventoryFile.delete();

        // Rename the updated file to the original file name
        File updatedInventoryFile = new File("updated_inventory_list.txt");
        updatedInventoryFile.renameTo(inventoryFile);
    }

    // addItemQuantity - it saves the latest item quantity added by the admin
    public static void insertaddItemQuantity (String itemName, int additionalQuantity) {
        try (BufferedReader reader = new BufferedReader(new FileReader("inventory_list.txt"))) {
            List<String> lines = new ArrayList<>();
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String existingItemName = parts[0].trim();

                if (existingItemName.equalsIgnoreCase(itemName)) {
                    int currentQuantity = Integer.parseInt(parts[1].trim());
                    int newQuantity = currentQuantity + additionalQuantity;
                    line = itemName + "," + newQuantity;
                }
                lines.add(line);
            }

            // Write the updated inventory to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_list.txt"))) {
                for (String updatedLine : lines) {
                    writer.write(updatedLine);
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while updating the item quantity: " + e.getMessage());
        }
    }

    public static void displayCashierMenu () {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String choice;

            do {
                System.out.println("===========================================================================");
                System.out.println("\t\tWelcome to Inventory Console System");
                System.out.println("===========================================================================");
                System.out.println("\t\t\t     Cashier Menu");
                System.out.println("===========================================================================");
                System.out.println("|| A. Purchase Item");
                System.out.println("|| B. Logout");
                System.out.println("|| C. Exit");

                System.out.print("Enter your choice: ");
                choice = reader.readLine();

                switch (choice.toUpperCase()) {
                    case "A":
                        purchaseItem();
                        break;
                    case "B":
                        System.out.println("Logging out...");
                        loginMenu();
                        System.exit(0);
                        break;
                    case "C":
                        System.out.println("Exiting program...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } while (!choice.equalsIgnoreCase("B"));
            System.exit(0);
        } catch (IOException e) {
            System.out.println("An error occurred while reading input: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("An illegal argument was provided: " + e.getMessage());
        } catch (Throwable t) {
            System.out.println("An unexpected error occurred: " + t.getMessage());
        }
    }

    public static void purchaseItem () {
        BufferedReader reader = null; // Declare the BufferedReader outside the try block

        try {
            // Open the inventory file for reading
            reader = new BufferedReader(new FileReader("inventory_list.txt"));
            // list of items
            String[][] items = readInventoryFromFile(reader);

            // displaying the list of items
            System.out.println("===========================================================================");
            System.out.println("\t\t\t\t   List Item");
            System.out.println("===========================================================================");
            System.out.println("Product No.\tName\t\t\t\t Quantity\tPrice");
            System.out.println("===========================================================================");

            for (String[] item : items) {
                System.out.printf("%s\t\t%s\t\t\t\t\t%s\t\t%s%n", item[0], item[1], item[2], item[3]);
            }
            System.out.println("===========================================================================");

            System.out.print("Enter product no.: ");
            int productNo;
            try {
                productNo = sc.nextInt();
                if (productNo < 1 || productNo > items.length) {
                    System.err.println("Invalid product number. Please try again.");
                    return;
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input format. Please enter a valid number.");
                return;
            }

            System.out.print("Quantity: ");
            int quantity;
            try {
                quantity = sc.nextInt();
                if (quantity <= 0) {
                    System.err.println("Invalid quantity. Please enter a positive number.");
                    return;
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input format. Please enter a valid number.");
                return;
            }
            System.out.println("===========================================================================");

            // Check if the product number and quantity are within the valid range
            if (productNo < 1 || productNo > items.length || quantity <= 0) {
                System.err.println("Invalid product or quantity. Please check the input.");
                return;
            }

            // Calculate total, cash, and change
            double total = Double.parseDouble(items[productNo - 1][3]) * quantity;

            System.out.printf("%nTotal: %.2f%n", total);
            System.out.print("Cash: ");
            int cash;
            try {
                cash = sc.nextInt();
                if (cash < total) {
                    System.err.println("Insufficient cash. Please enter an amount equal to or greater than the total.");
                    return;
                }
            } catch (InputMismatchException e) {
                System.err.println("Invalid input format. Please enter a valid number.");
                return;
            }
            double change = cash - total;
            System.out.printf("Change: %.2f%n", change);

            int currentQuantity;
            try (BufferedReader fileReader = new BufferedReader(new FileReader("inventory_list.txt"))) {
                String[][] inventoryItems = readInventoryFromFile(fileReader);
                try {
                    currentQuantity = Integer.parseInt(inventoryItems[productNo - 1][2]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid quantity in inventory. Please check the inventory data.");
                    return;
                }
            } catch (IOException e) {
                System.err.println("Error reading the inventory file: " + e.getMessage());
                return;
            }

            int remainingQuantity = currentQuantity - quantity;
            items[productNo - 1][2] = String.valueOf(remainingQuantity);
            System.out.println("===========================================================================");
            System.out.println("\t\t\tThank you for purchasing!");
            System.out.println("===========================================================================");
            // Update the inventory file with the deducted quantity
            updateInventoryFile("inventory_list.txt", items);

            System.out.print("Do you want to go back to the Cashier menu? (Y/N): ");
            String goBack = sc.next();

            if (goBack.equalsIgnoreCase("N")) {
                System.out.println("Exiting program...");
                System.exit(0);
            }

            System.out.println();
        } catch (InputMismatchException e) {
            System.err.println("Invalid input format. Please enter a valid number.");
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        } finally {
            try {
                if (reader != null) {
                    reader.close(); // Close the BufferedReader in the finally block
                }
            } catch (IOException e) {
                System.err.println("Failed to close the inventory file: " + e.getMessage());
            }
        }
    }

    public static void updateInventoryFile (String fileName, String[][] items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("inventory_list.txt"))) {
            for (String[] item : items) {
                writer.write(String.join(",", item));
                writer.newLine();
            }
            writer.flush(); // Flush the writer to ensure the changes are written to the file immediately
        } catch (IOException e) {
            System.err.println("Error updating inventory file: " + e.getMessage());
            // Add appropriate handling for the error.
        }
    }

    public static String[][] readInventoryFromFile (BufferedReader reader) {
        List<String[]> itemList = new ArrayList<>();
        int[] columnWidths = new int[4]; // Store the maximum width for each column

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productInfo = line.split(",");
                if (productInfo.length == 4) {
                    for (int i = 0; i < 4; i++) {
                        productInfo[i] = productInfo[i].trim();
                        columnWidths[i] = Math.max(columnWidths[i], productInfo[i].length());
                    }
                    itemList.add(productInfo);
                } else {
                    System.out.println("Invalid entry: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("An error occurred while reading the inventory: " + e.getMessage());
        }

        // convert the list to a 2D array and align the values
        String[][] items = new String[itemList.size()][4];
        for (int i = 0; i < itemList.size(); i++) {
            String[] productInfo = itemList.get(i);
            for (int j = 0; j < 4; j++) {
                items[i][j] = alignValue(productInfo[j], columnWidths[j]);
            }
        }
        return items;
    }

    private static String alignValue (String value, int width) {
        StringBuilder sb = new StringBuilder(value);
        while (sb.length() < width) {
            sb.append(" ");
        }
        return sb.toString();
    }

    public static void loginMenu () {
        // log in section will detect if admin or cashier login
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        boolean isLoggedIn = false;
        while (!isLoggedIn) {
            try  {
                System.out.println("===========================================================================");
                System.out.println("\t\t\t  Choose an Option");
                System.out.println("===========================================================================");
                System.out.println("|| A. Login");
                System.out.println("|| B. Exit");
                System.out.print("Enter your choice: ");
                String choice = reader.readLine();

                switch (choice.toUpperCase()) {
                    case "A":
                        System.out.println("===========================================================================");
                        System.out.println("\t\t\tEnter your Credentials");
                        System.out.println("===========================================================================");
                        System.out.print("|| Username: ");
                        String username = reader.readLine();

                        System.out.print("|| Password: ");
                        String password = reader.readLine();

                        // verify admin credentials
                        if (verifyAdminCredentials(username, password)) {
                            System.out.println("===========================================================================");
                            System.out.println("\t\t      Admin Login successfully!");
                            displayAdminMenu();
                            isLoggedIn = true;
                        }
                        // verify cashier credentials
                        else if (verifyCashierCredentials(username, password)) {
                            System.out.println("===========================================================================");
                            System.out.println("\t\t    Cashier Login successfully!");
                            displayCashierMenu();
                            isLoggedIn = true;
                        } else {
                            System.out.println("Invalid credentials. Please try again.");
                        }
                        break;

                    case "B":
                        System.out.println("Exiting program...");
                        isLoggedIn = true;
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } catch (IOException e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }

    }

    public static void main (String[] args) {
        loginMenu();
    }

}
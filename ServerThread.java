import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;
import java.util.Scanner;


public class ServerThread extends Thread {

	Socket myConnection;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
    boolean loggedIn = false;

    private static Map<String, User> users = new HashMap<>(); // Hashmap of users
    private static List<String> allTransactions = new ArrayList<>(); // List of transactions
	
	public ServerThread(Socket s)
	{
		myConnection = s;
	}
	
	public void run()
	{
		int cont = 0;

        String ppsNumber;
        double amount;
		
		Bank b = new Bank();
        User currentUser = null;

        //Read from file
        readUsersFromFile(b);
		
		try
		{
			out = new ObjectOutputStream(myConnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(myConnection.getInputStream());
		
			//Server Comms - See Requester.java for more info.
			do {
				
				//Main Menu - Select an option
                if(!loggedIn)
                    sendMessage("Please select an option\n 1: Register with the system. \n 2: Login. \n -1 to EXIT.");
                else
                    sendMessage("Please select an option\n 1: Register with the system. \n 2: Login. \n 3: Lodge money to the user account. \n 4: Retrieve all registered users. \n 5: Transfer money to another account \n 6: View all transactions \n 7: Update your password \n -1 to EXIT.");
				
                message = (String)in.readObject();
				cont = Integer.parseInt(message);
			
				switch (message) {
                    case "1":
                        registerUser(b); // Register
                        break;
                    case "2":
                        loginUser(b, currentUser); // Login
                        break;
                    case "3":
                        //lodgeMoney(b, currentUser); // Lodge money
                        // Lodge Money
                        sendMessage("Please enter ppsNumber for account you wish to lodge money to: ");
                        ppsNumber = (String)in.readObject();

                        sendMessage("Please enter amount you wish to lodge: ");
                        amount = Double.parseDouble((String)in.readObject());

                        sendMessage(b.lodgeMoney(ppsNumber, amount));
                        allTransactions.add("Lodged " + amount + " to account " + ppsNumber);
                        break;
                    case "4":
                        listUsers(b); // List users
                        break;
                    case "5":
                        transferMoney(b); // Transfer money
                        break;
                    case "6":
                        viewTransactions(currentUser); // View all transactions (for all users)
                        break;
                    case "7":
                        updatePassword(b); // Update password
                        break;
                    case "-1":
                        sendMessage("Closing connection."); // Close connection
                        break;
                    default:
                        sendMessage("Please enter a valid option."); // Invalid option
                        break;
                }
                
			}while(cont != -1);

            //Write to file
            writeUsersToFile();
			
			in.close();
			out.close();
		}
		catch(ClassNotFoundException classnot)
		{
			System.err.println("Data received in unknown format");
		}
		catch(IOException e)
		{
            e.printStackTrace();
		}
	}
    
    private void readUsersFromFile(Bank b) {
        try {
            File file = new File("users.txt");
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine()) {
                String[] line = sc.nextLine().split(" ");
                users.put(line[1], new User(line[0], line[1], line[2], line[3], line[4], Double.parseDouble(line[5])));
                b.addUser(line[0], line[1], line[2], line[3], line[4], Double.parseDouble(line[5]));
            }

            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeUsersToFile() {
        try {
            FileWriter writer = new FileWriter("users.txt");
            for (User u : users.values()) {
                writer.write(u.toString());
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void registerUser(Bank b) {
        sendMessage("Please enter name: ");
        String name = (String) readObject();
        sendMessage("Please enter ppsNumber: ");
        String ppsNumber = (String) readObject();
        sendMessage("Please enter email: ");
        String email = (String) readObject();
        sendMessage("Please enter password: ");
        String password = (String) readObject();
        sendMessage("Please enter address: ");
        String address = (String) readObject();
        sendMessage("Please enter balance: ");
        double balance = Double.parseDouble((String) readObject());

        b.addUser(name, ppsNumber, email, password, address, balance);
        users.put(ppsNumber, new User(name, ppsNumber, email, password, address, balance));
        sendMessage("User registered!");
    }

    private void loginUser(Bank b, User currentUser) {
        if (loggedIn) {
            sendMessage("You are already logged in!");
        } else {
            sendMessage("Please enter email for login: ");
            String loginEmail = (String) readObject();
            sendMessage("Please enter password for login: ");
            String loginPassword = (String) readObject();

            if (b.validateUser(loginEmail, loginPassword)) {
                loggedIn = true;

                // Set Current User
                for (User u : users.values()) {
                    if (u.email().equals(loginEmail) && u.password().equals(loginPassword)) {
                        currentUser = u;
                    }
                }
                sendMessage(currentUser.name + "Logged-In successfully!");
            } else {
                sendMessage("Login failed!");
                loggedIn = false;
            }
        }
    }

    private void lodgeMoney(Bank b, User currentUser) {
        try {
            sendMessage("Please enter amount you wish to lodge: ");
            double amount = Double.parseDouble((String)in.readObject());
            if (amount <= 0) {
                sendMessage("The amount must be positive.");
                return;
            }
            sendMessage(b.lodgeMoney(currentUser.ppsNumber, amount));
            allTransactions.add("Lodged " + amount + " to account " + currentUser.ppsNumber);
        } catch (NumberFormatException e) {
            sendMessage("Invalid amount.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void listUsers(Bank b) {
        sendMessage("Listing users...");
        sendMessage(b.listUsers());
    }

    private void transferMoney(Bank b) {
        sendMessage("Please enter ppsNumber for account you wish to transfer money from: ");
        String ppsNumber = (String) readObject(); 
        sendMessage("Please enter ppsNumber for account you wish to transfer money to: ");
        String transferPps = (String) readObject();
        sendMessage("Please enter email for account you wish to transfer money to: ");
        String transferEmail = (String) readObject();

        sendMessage("Please enter amount you wish to transfer: ");
        double amount = Double.parseDouble((String) readObject());

        sendMessage(b.transferMoney(ppsNumber, transferPps, transferEmail, amount));
        allTransactions.add("Transferred " + amount + " from account " + ppsNumber + " to account " + transferPps);
    }

    private void viewTransactions(User currentUser) {
        sendMessage("Listing transactions...");
        for (String s : allTransactions) {
            sendMessage(s);
        }
    }

    private void updatePassword(Bank b) {
        sendMessage("Please enter pps for account you wish to update: ");
        String ppsNumber = (String) readObject();
        sendMessage("Please enter new password: ");
        String password = (String) readObject();
        sendMessage(b.updatePassword(ppsNumber, password));
    }

    private Object readObject() {
        try {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	void sendMessage(String msg)
	{
		try{
			out.writeObject("server> "+msg);
			out.flush();
			System.out.println("server>" + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
}

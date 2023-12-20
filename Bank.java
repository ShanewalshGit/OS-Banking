//The Library method represents a database hosted on the server.
//Each method has the synchronized keyword, which means that each thread running them must take turns.
//This ensures that the Library object is consistent even as it's updated by many different threads.

import java.util.ArrayList;
import java.util.List;

public class Bank {
    List<User> users = new ArrayList<>();
	User u;

	public synchronized boolean checkPPSNumber(String ppsNumber) {
		for (User u : users) {
			if(u.ppsNumber().equals(ppsNumber))
				return false; // already exists
		}
		return true; // unique PPS
	}

	public synchronized boolean checkEmail(String email) {
		for (User u : users) {
			if(u.email().equals(email))
				return false; // already exists
		}
		return true; // unique email
	}
	
	//Add Users
	public synchronized void addUser(String name, String ppsNumber, String email, String password, String address, double balance) {
		u = new User(name, ppsNumber, email, password, address, balance);
		users.add(u);
	}

	//ValidateUser
	public synchronized boolean validateUser(String email, String password) {
		for (User u : users) {
			if(u.email().equals(email) && u.password().equals(password))
				return true;
		}
		return false;
	}

	//Lodge Money
	public synchronized String lodgeMoney(String i, double amount) {
		for (User u : users) {
			if(u.ppsNumber().equals(i)) {
				u.balance += amount;
				return "Money successfully lodged.";
			}
		}
		return "User not found!";
	}
	
	//Search Users
	public synchronized String searchUser(String i) {
		for (User u : users) {
			if(u.ppsNumber().equals(i))
				return u.toString();
		}
		
		return "User not found!\n";
	}

	//Transfer Money
	public synchronized String transferMoney(String i, String j, String e, double amount) {
		for (User u : users) {
			if(u.ppsNumber().equals(i)) {
				for (User b : users) {
					if(b.ppsNumber().equals(j) && b.email().equals(e)) {
						if(u.balance >= amount) {
							u.balance -= amount;
							b.balance += amount;
							return "Money successfully transferred.";
						}
						else
							return "Insufficient funds!";
					}
				}
				return "User not found!";
			}
		}
		return "User not found!";
	}

	public synchronized String updatePassword(String i, String newPassword) {
		for (User u : users) {
			if(u.ppsNumber().equals(i)) {
				u.password = newPassword;
				return "Password successfully updated.";
			}
		}
		return "User not found!";
	}
	
	//List Users
	public synchronized String listUsers() {
		String s = "";
		for (User u : users) {
			s += u.toString();
		}
		return s;
	}
	
	public synchronized String deleteUsers(String i) {
		for (User b : users) {
			if(b.ppsNumber() == i) {
				users.remove(b);
				return "User successfully deleted.";
			}
		}
		return "User not found!";
	}
}

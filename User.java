//This is a simple book object. It's used by Library.java to form a list of books.
//This can also be implemented as a record, but it's a class for now for those unfamiliar
//with records.


public class User {

	String name, ppsNumber, email, password, address;
	double balance;
	
	public User(String name, String ppsNumber, String email, String password, String address, double balance) {
		this.name = name;
		this.ppsNumber = ppsNumber;
		this.email = email;
		this.password = password;
		this.address = address;
		this.balance = balance;
	}

	// Overriden ToString
	@Override
	public String toString() {
		return name + " " + ppsNumber + " " + email + " " + password + " " + address + " " + balance + "\n";
	}

	// Return methods
	public String name() {
		return name;
	}

	public String ppsNumber() {
		return ppsNumber;
	}

	public String email() {
		return email;
	}

	public String password() {
		return password;
	}

	public String address() {
		return address;
	}

	public double balance() {
		return balance;
	}
}


import java.io.*;
import java.net.*;
import java.util.Scanner;
public class Requester{
	Socket requestSocket;
	ObjectOutputStream out;
 	ObjectInputStream in;
 	String message;
 	String response;
	boolean validate;
 	Scanner input;
	Requester(){
		
		input = new Scanner(System.in);
	}
	void run()
	{
		int cont = 0;
		double balance;
		

		try{
			//This bit's given to you by the Skeleton Code on the VLE.

			//1. creating a socket to connect to the server
			
			requestSocket = new Socket("127.0.0.1", 2004);
			System.out.println("Connected to localhost in port 2004");
			//2. get Input and Output streams
			out = new ObjectOutputStream(requestSocket.getOutputStream());
			out.flush();
			in = new ObjectInputStream(requestSocket.getInputStream());
			//3: Communicating with the server

			try 
			{
				do {

					//Reads in initial message and responds, selecting an option.
					message = (String)in.readObject();
					System.out.println(message);
					response = input.next();
					cont = Integer.parseInt(response);
					sendMessage(response);
					
					if(response.equalsIgnoreCase("1"))
					{
                        // Register
						message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); // name

                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); //ppsNumber

                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); //email

                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); //password

                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); //address

                        message = (String)in.readObject();
                        System.out.println(message);
                        balance = input.nextDouble(); //balance
                        sendMessage(""+balance);
                        
                        message = (String)in.readObject();
                        System.out.println(message);
					}
					else if(response.equalsIgnoreCase("2"))
					{
                        // Login
                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); // email

                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); // password

                        message = (String)in.readObject();
                        System.out.println(message);

                       
					}
                    else if(response.equalsIgnoreCase("3"))
                    {
						// Lodge Money
						message = (String)in.readObject();
						System.out.println(message);
						sendMessage(input.next()); // ppsNumber for validation

						message = (String)in.readObject();
						System.out.println(message);
						balance = input.nextDouble(); //balance
						sendMessage(""+balance);

						message = (String)in.readObject();
						System.out.println(message);
						
                    }

					else if(response.equalsIgnoreCase("4"))
					{
                        // List Users
						message = (String)in.readObject(); //Listing Users...
						System.out.println(message);

						message = (String)in.readObject(); //Receive Result
						System.out.println(message);
					}
                    else if(response.equalsIgnoreCase("5"))
                    {
                        // Transfer money
						message = (String)in.readObject();
						System.out.println(message);
						sendMessage(input.next()); // ppsNumber for validation

						message = (String)in.readObject();
						System.out.println(message);
						sendMessage(input.next()); // ppsNumber for validation

						message = (String)in.readObject();
						System.out.println(message);
						sendMessage(input.next()); // email for validation

						message = (String)in.readObject();
						System.out.println(message);
						balance = input.nextDouble(); //balances
						sendMessage(""+balance);

						message = (String)in.readObject();
						System.out.println(message);

                    }
                    else if(response.equalsIgnoreCase("6"))
                    {
						// View transactions
						message = (String)in.readObject();
						System.out.println(message); // Listing Transactions

						message = (String)in.readObject();
						System.out.println(message); // Receive Result
                    }
                    else if(response.equalsIgnoreCase("7"))
                    {
                        // Update Password
                        message = (String)in.readObject(); 
                        System.out.println(message);
                        sendMessage(input.next()); // ppsNumber for validation

                        message = (String)in.readObject();
                        System.out.println(message);
                        sendMessage(input.next()); // New Password

                        message = (String)in.readObject();
                        System.out.println(message);
                    }
					else if (!response.equalsIgnoreCase("-1"))
					{
						message = (String)in.readObject();
						System.out.println(message);
					}
				}while(cont != -1);
				message = (String)in.readObject(); //Ending message
				System.out.println(message);
			} 
			
			
			catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		}
		catch(UnknownHostException unknownHost){
			System.err.println("You are trying to connect to an unknown host!");
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
		finally{
			//4: Closing connection
			try{
				in.close();
				out.close();
				requestSocket.close();
			}
			catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	}
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	void sendBoolean(Boolean bool)
	{
		try{
			out.writeObject(bool);
			out.flush();
			System.out.println("validate> " + bool);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}

	public static void main(String args[])
	{
		Requester client = new Requester();
		client.run();
	}
}

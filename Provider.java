//The provider's been made a bit bare-bones by creating the ServerThread and moving the
//server portion of the communication over there.
//It makes things much cleaner.

import java.io.*;
import java.net.*;
public class Provider{
	
	
	public static void main(String args[])
	{
		ServerSocket providerSocket;
		try 
		{
			Bank bank = new Bank();

			providerSocket = new ServerSocket(2004, 10);
			
			while(true)
			{
			
				//2. Wait for connection
				System.out.println("Waiting for connection");
			
				Socket connection = providerSocket.accept();
				ServerThread T1 = new ServerThread(connection, bank);
				T1.start();
			} 
			
			//providerSocket.close();
		}
		
		catch (IOException e1) 
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
			
		
	}
	
}

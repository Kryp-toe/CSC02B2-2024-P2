package csc2b.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class WeatherServer implements Runnable
{
	private ServerSocket serverSocket = null;
	private Socket clientSocket = null;
	private PrintWriter outPrintWriter = null;
	private BufferedReader inBufferedReader = null;
	private int count = 0;
	private int anscount = 0;

	public WeatherServer(int portnumber)
	{
		try{
			//connect to server
			serverSocket = new ServerSocket(portnumber);
			System.out.println("Running on port: " + portnumber);

			//create client connection to server
			clientSocket = serverSocket.accept();

			//create input and output writers/reeaders
			outPrintWriter = new PrintWriter(clientSocket.getOutputStream(), true);
			inBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

			//display established connection
			outPrintWriter.println("Ready for incoming connections...");

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void beginSession()
	{
		try
		{
			//count query
			count ++;
			outPrintWriter.println("0" + count + " HELLO - you may make 4 requests and I’ll provide weather information");
			
			outPrintWriter.println("Type \"START\" to begin.");

			//get response from client
			String responseString;
			responseString = inBufferedReader.readLine();
			responseString = responseString.toUpperCase();

			//while the response is incorrect
			while(!(responseString.equals("START")))
			{
				//get correct response
				outPrintWriter.println("Type \"START\" to begin.");
				responseString = inBufferedReader.readLine();
				responseString = responseString.toUpperCase();
			}
			
			count++;
			//Get a request or end
			outPrintWriter.println("0"+ count + " REQUEST or DONE");
			
			//while there are less than 4 responses
			while (count != 4)
			{
				//check responses
				checkConnection();
				
				//get command
				responseString = inBufferedReader.readLine();
				responseString = responseString.toUpperCase();
				
				//add response count
				count ++;
				
				//display output based on responses
				switch (responseString)
				{
				case "REQUEST DURBAN":
					outPrintWriter.println("0"+ count + " Sunny and Warm in Durban");
					break;

				case "REQUEST JOBURG":
					outPrintWriter.println("0"+ count + " Clear Skies in Joburg");
					break;

				case "REQUEST CAPE TOWN":
					outPrintWriter.println("0"+ count + " Cool and Cloudy in Cape Town");
					break;

				case "DONE":
					closeConnection();
					break;

				default:
					if(responseString.contains("REQUEST "))
					{
						randResponse();
					}else
					{
						outPrintWriter.println("0"+ count + " INVALID RESPONSE");
					}
					break;
				}
				
				anscount++;
				checkConnection();
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void checkConnection()
	{
		//check if 4 responses were sent
		if(count == 4) {
			closeConnection();
		}
	}

	public void closeConnection()
	{
		//close the connection
		try
		{
			if( (clientSocket != null)||(serverSocket != null) )
			{
				count ++;
				outPrintWriter.println("0"+ count + " OK BYE - " + anscount + " Ansers Provided");
				clientSocket.close();
				serverSocket.close();
			}
		}catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void randResponse()
	{
		//print random string
		Random random = new Random();
		int rand = random.nextInt(3)+1;
		
		switch (rand)
		{
		case 1:
			outPrintWriter.println("0"+ count + " No data available");
			break;
		case 2:
			outPrintWriter.println("0"+ count + " Please try again later”");
			break;
		case 3:
			outPrintWriter.println("0"+ count + " Data outdated");
			break;
		}
	}

	@Override
	public void run()
	{
		beginSession();
	}
}
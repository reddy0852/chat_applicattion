package chat_app;

import java.io.*;
import java.net.*;
import java.util.Scanner; 

	public class Client implements Runnable {
		private Socket socket;
		private DataOutputStream output;
		private DataInputStream input;
		String receiver,name;
		@SuppressWarnings("null")
		Client(String host, int port,String name,String receiver){
			String ur_message = null;	
			this.receiver=receiver;
			this.name=name;
			try {
				socket = new Socket( host, port );
				System.out.println( "connected to "+socket );
				input = new DataInputStream( socket.getInputStream() );
				output = new DataOutputStream( socket.getOutputStream() );
                new Thread(this).start(); 
                
                @SuppressWarnings("resource")
				Scanner scan=new Scanner(System.in);
                while(true) {  
                	ur_message=scan.nextLine();
                	if(ur_message.equals("quit")) {
                		processMessage(name+" "+receiver+" "+ur_message);
                		System.out.println("you quited the communicaton");
                		break;
                		}
                	else
                		processMessage(name+" "+receiver+" "+ur_message);
                }
                } catch( IOException ie ) {
                		System.out.println( ie );
                }
			
			}
	private void processMessage( String message ) {
		try {
			output.writeUTF( message );
			} catch( IOException ie ) {
				System.out.println( ie );
			}
		}
		public void run() { 
			String[] word;
			String sender,to_receiver,content = null;
			try {
		while (true) {
			String message = input.readUTF();
		    if(message.equals("quited")) {
		    	input.close();
		    	output.close();
		    	socket.close();
		    	System.exit(0);
		    	}
		    else {
		    	word=message.split(" ");
		    	sender=word[0];
		    	to_receiver=word[1];
		    	content=message.substring(sender.length()+to_receiver.length()+2);
		    	if(to_receiver.equals(name)) 
		    		System.out.println(sender+":"+content);
		    }
			} 
		} catch( IOException ie ) {
				System.out.println( ie );
			}
		}
		public static void main(String args[]) {
			String host="localhost"; 
			int port=5055;//Integer.parseInt(args[1]);
			String name=args[0];
			String receiver=args[1];
			Client cl1=new Client(host,port,name,receiver);
		} 
	}

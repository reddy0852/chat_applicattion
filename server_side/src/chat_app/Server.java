   package chat_app;

	import java.io.*; 
	import java.net.*;
import java.util.Enumeration;
import java.util.Hashtable;
	public class Server {
		private ServerSocket ss;
		private Hashtable<Socket, DataOutputStream> outputStreams = new Hashtable<Socket, DataOutputStream>();
		public Server(int port) throws IOException{
		        ss = new ServerSocket(port); //listen to port 5055
				while(true) {
					System.out.println ("Listening on port 5055");
					Socket s = ss.accept();//wait for incoming client
					System.out.println( "Connection from "+s );
					DataOutputStream dout = new DataOutputStream
							( s.getOutputStream() );
					outputStreams.put( s, dout );
					new ServerThread( this, s );
					}
		}
		Enumeration<DataOutputStream> getOutputStreams() { 
			return outputStreams.elements();
			}
		void sendToAll( String message ) {
			synchronized( outputStreams ) {
				for(Enumeration<?> e = getOutputStreams(); e.hasMoreElements(); ) {
					DataOutputStream dout = (DataOutputStream)e.nextElement();
					try { 
						dout.writeUTF( message ); 
						} catch( IOException ie ) {
							System.out.println( ie ); 
							}
					}
				}
			}
		void removeConnection( Socket s) {
			synchronized( outputStreams ) {
				System.out.println( "Removing connection to "+s );
				outputStreams.remove( s );
				try { 
					s.close(); 
					} catch( IOException ie ) {
						System.out.println( "Error closing "+s );
						ie.printStackTrace(); 
					}
				}
			}
		static public void main( String args[] ) throws Exception {
			int port = 5055;
			 new Server( port );
			}
		}
 class ServerThread extends Thread {
		private Server server;
		private Socket socket;
		public ServerThread( Server server, Socket socket ) {
			this.server = server;
			this.socket = socket;
	        start();
		}
	   public void run() {
		try { 
			DataInputStream din = new DataInputStream(socket.getInputStream() ); 
			while (true) {	
				String message = din.readUTF();
				String[] word=message.split(" ");
		    	String sender=word[0];
		    	String receiver=word[1];
		    	String content=message.substring(sender.length()+
		    			receiver.length()+2);
		    	if(!content.equals("quit")) {
		    		System.out.println( "Sending "+content+""
		    				+ " from "+sender+" to "+receiver );
		    		server.sendToAll(message );
		    	}
		    	else {
		    		server.sendToAll("quited" );
		    		server.removeConnection( socket );
		    		break;
		    	}
				}
			} catch( IOException ie ) {
				System.out.println("close connection");
			} finally {
				server.removeConnection( socket );
		}
		}
	}


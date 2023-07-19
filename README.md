# Chat Application using CMD
This a console chat appplication using Java Socket. The Java Chat application you are going to build is a console application that is launched from the command line. There can be multiple clients connect to a server and they can chat to each other. The messega sent is seen only by the receiver. The clients provide thier user name, recevier name and the server ip on the command line when they start hte client side program. 


The server sends a list of currently online users to the new user. Every user is notified when a new user arrives and when a user has gone. Each message is prefixed with the username to keep track who sent the message.


In the server side program, multithreading concept has been used to handle each coming request concurrently .

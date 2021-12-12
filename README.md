# ID1212
Tasks and Project for the course Network Programming

## Task 1

Your task is to write a simple chat program consisting of a server and a client where the server forwards incoming messages from a client to all other clients. Specifically regarding threads, they should be as follows:

*ChatClient.java: Has two threads, one to listen for incoming messages from the server and one to send messages to the server.

*ChatServer.java: Has one thread to each of the clients currently connected but also one thread to listen for new incoming connections from new clients.
There is no requirement to handle user login or chatrooms. Plain and simple. However, these are requirements:

*Clients should be able to leave the chat without crashing the Server.

*If the server goes down the clients should give some notification of that instead of crashing.

**Extra assignment:** Find and download a (simple) network sniffer (suggestion Wireshark (Links to an external site.)), record some traffic from your chat and explain the TCP headers and flags (ACK/SYN/PSH/FIN). Compare with TCP-datagrams from your web browser.

## Task 2
Your task is to write a guessing game with sockets where the dialogue will be according to the following (when you connect with your webbrowser):

Requirements of the program: It should consist of at least two classes, a serverclass and a guessclass where the former handles the requests from and the responses to the server and the latter handles the gamelogic.

Note:

*Each new client connecting should lead to a new instance of the game with a new guessclass-object and a "Set-Cookie" field added in the http-response.

*There is no requirement to use separate threads (but you are allowed to).

*The browser creates an additional request for the bookmark-icon "favicon.ico" (browser dependant, test & try)

*A new browser window (but not tab) usually creates a new session (browser dependant, test & try)

*You should only use Java SE in the solution and no Java EE.

**Extra assignment:** Use the java.net.HttpURLConnection (Links to an external site.) class to simulate a browser and play the game 100 times and present the average number of guesses.

## Task 3
Your task is to write a quiz with the tomcat application server. The user enters username, password and email on a startpage before the quiz starts and each test has a default number of questions. The questions should be multiple choice checkboxes.

The design shall follow the MVC design pattern with

*HttpServlet(s) (as Controller)

*JavaBeans (as Model)

*jsp-pages (as View)

Netbeans/Tomcat/MySQL is the only environment we are supporting during labs. That does not mean we require you to use this combination but it's up to you to fix a working combination of IDE / application server / DB if you use another combination than stated above.


**Extra assignment:** Implement it with the Spring Framework (Spring MVC, not Spring Boot) with Thymeleaf. You still need to solve the mandatory task.

## Task 4
Your task is to write a program that connects to your @kth.se account, lists the contents and then retrieves the first mail from it. You are not allowed to use JavaMail ( never heard of it? good! ) but should instead do it "manually" according to the IMAP-protocol. You should also send one mail to yourself using the SMTP-protocol. The webmail has the following configuration:

Settings for receiving e-mail (incoming)

*Server: webmail.kth.se

*Port: 993

*Protocol: SSL/TLS

*Authentication: Normal password

Settings for sending e-mail (outgoing)

*Server: smtp.kth.se

*Port: 587

*Protocol: STARTTLS

*Authentication: Normal password

Note:

*In the first case (IMAP with SSL/TLS) you start with an encrypted session and in the second case (SMTP with STARTTLS) you switch to encryption during the session.

*Full documentation of IMAP can be found in rfc3501 (Links to an external site.) but to solve the task it will be sufficient to compare with an IMAP-session for example the one here:
https://en.wikipedia.org/wiki/Internet_Message_Access_Protocol (Links to an external site.)

*Usefull examples of SMTP-sessions can be found here:
https://www.samlogic.net/articles/smtp-commands-reference.htm (Links to an external site.)

*You do not need a certificate (you are acting client)

**Extra assignment:** Change the number guess game in Task2 so that it uses encryption and verify that a commercial (not your own hack) browser can connect (to https://localhost (Links to an external site.)) and play the game. Since it's encryption on the *server* you are setting up, you need to change the game to use encryption with a certificate (use keytool to create a self signed certificate).

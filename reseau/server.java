package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class server {
	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public static void main(String[] args) {
		try {
			ServerSocket serverSocket = new ServerSocket(5004);
			System.out.println("listening to port:5000");
			Socket clientSocket = serverSocket.accept();
			System.out.println(clientSocket+" connected.");
			dataInputStream = new DataInputStream(clientSocket.getInputStream());
			dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
			
			// get the input stream from the connected socket
			InputStream inputStream = clientSocket.getInputStream();
			
			DataInputStream dataInputStream = new DataInputStream(inputStream);

			// read the message from the socket
			String requete = dataInputStream.readUTF();

			switch (requete) {
				case "employe" : 
					sendFile("Z:\\Projet\\workspace\\TestReseau2\\employe.csv");
					break;
				case "conferencier" :
					sendFile("Z:\\Projet\\workspace\\TestReseau2\\conferencier.csv");
					break;
				case "exposition" : 
					sendFile("Z:\\Projet\\workspace\\TestReseau2\\exposition.csv");
					break;
				case "visite" :
					sendFile("Z:\\Projet\\workspace\\TestReseau2\\visite.csv");
					break;
				default : 
					// TODO gérer l'erreur
			}

			dataInputStream.close();
			dataOutputStream.close();
			clientSocket.close();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void sendFile(String path) throws Exception{
		int bytes = 0;
		File file = new File(path);
		FileInputStream fileInputStream = new FileInputStream(file);

		// send file size
		dataOutputStream.writeLong(file.length());  
		// break file into chunks
		byte[] buffer = new byte[4*1024];
		while ((bytes=fileInputStream.read(buffer))!=-1){
			dataOutputStream.write(buffer,0,bytes);
			dataOutputStream.flush();
		}
		fileInputStream.close();
	}
}
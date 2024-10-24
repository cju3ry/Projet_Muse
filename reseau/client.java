package test;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class client {
	private static DataOutputStream dataOutputStream = null;
	private static DataInputStream dataInputStream = null;

	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost",5004);
			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());
			Scanner entree = new Scanner(System.in);
			String requete;

			// get the output stream from the socket.
			OutputStream outputStream = socket.getOutputStream();
			// create a data output stream from the output stream so we can send data through it
			DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
			
			System.out.print("Entre le fichier que vous souhaitez : ");
			requete = entree.nextLine();

			// write the message we want to send
			dataOutputStream.writeUTF(requete);
			dataOutputStream.flush(); // send the message

			receiveFile("NewFile1.csv");

			dataInputStream.close();
			dataInputStream.close();
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private static void receiveFile(String fileName) throws Exception{
		int bytes = 0;
		FileOutputStream fileOutputStream = new FileOutputStream(fileName);

		long size = dataInputStream.readLong();     // read file size
		byte[] buffer = new byte[4*1024];
		while (size > 0 && (bytes = dataInputStream.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
			fileOutputStream.write(buffer,0,bytes);
			size -= bytes;      // read upto file size
		}
		fileOutputStream.close();
	}
}

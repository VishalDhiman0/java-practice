import java.net.ServerSocket;
import java.net.Socket;
// import java.time.LocalTime;
import java.io.*;
import java.util.Scanner;



public class Server{
    
    final static int PORT = 2222;
    public static void main(String[] args) {
            final ServerSocket serverSocket;
        try {
            /**Creating a Server Socket*/
            serverSocket = new ServerSocket(PORT);

            /**Waiting for Connection */
            Socket request = serverSocket.accept();

            /**User Prompt When Connection Established
             * Only Works On LocalHost(127.0.0.1)*/
            System.out.println("(+) Connected......" + request.getInetAddress());

            /**Input And Output Streams To Transfer Data Between Client And Server
             * dout - DataOutputStream
             * din - DataInputStream*/
            final DataOutputStream dout = new DataOutputStream(request.getOutputStream());
            final DataInputStream din = new DataInputStream(request.getInputStream());
    
            /**Starting Thread For Smooth Chatting */
            new SendS(dout).start();
            new ReceiveS(din).start();
            
        } catch (Exception e) {
                e.printStackTrace();
        }
        
        return;
    }   
    }
    
/**Thread For Sending Data to Client */
class SendS extends Thread {
    DataOutputStream d;
    final Scanner scanner =  new Scanner(System.in);

    public SendS(DataOutputStream d) {
        this.d = d;
    }

    public void run() {
        try {
            while (true) {
                final String s = scanner.nextLine();
                d.writeUTF(s);
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        finally{
            scanner.close();
        }
    }
}


/**Thread For Receiving Data From Client */
class ReceiveS extends Thread {
    DataInputStream d;

    public ReceiveS(DataInputStream d) {
        this.d = d;
    }

    public void run() {
        try {
            String s = d.readUTF().toString();
            while (!(s == "logout" || s == "logout")) { 
                System.out.println("From Client: " + s);
                s = d.readUTF().toString();
            }
        } catch (final Exception e) {
                e.printStackTrace();
        }
    }
}
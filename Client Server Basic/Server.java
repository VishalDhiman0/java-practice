import java.net.ServerSocket;
import java.net.Socket;
// import java.time.LocalTime;
import java.io.*;
import java.util.Scanner;



public class Server{
    
    static boolean running = true;
    final static int PORT = 2223;
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

            /**Input And Output Streams To Transfer Data Between Server And Server
             * dout - DataOutputStream
             * din - DataInputStream*/
            final DataOutputStream dout = new DataOutputStream(request.getOutputStream());
            final DataInputStream din = new DataInputStream(request.getInputStream());
    
            /**Starting Thread For Smooth Chatting */
            new Send(dout).start();
            new Receive(din).start();
            
            // Thread.currentThread().join();
            // serverSocket.close();
            // request.close();
        } catch (Exception e) {
                e.printStackTrace();
        }
        
        return;
    }   
    }
/**Thread For Sending Data to Server */
class Send extends Thread {
    DataOutputStream d;
    final Scanner scanner =  new Scanner(System.in);

    public Send(DataOutputStream d) {
        this.d = d;
    }

    public void run() {
        try {
            while (Server.running) {
                String s = null;
                while(Server.running) {
                    if (scanner.hasNext()) {
                        s = scanner.nextLine();
                    }
                    if (s != null) break;
                }
                d.writeUTF(s);
                if (s.contains("logout")) Server.running = false;

            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
        finally{
            scanner.close();
        }
    }
}


/**Thread For Receiving Data From Server */
class Receive extends Thread {
    DataInputStream d;

    public Receive(DataInputStream d) {
        this.d = d;

    }

    public void run() {
        try {
            String s = d.readUTF().toString();
            while (Server.running) {
                System.out.println("From Server: " + s);
                s = d.readUTF().toString();
                if (s.contains("logout")) Server.running = false;
            }
        } catch (final Exception e) {
                e.printStackTrace();
        }
    }
}
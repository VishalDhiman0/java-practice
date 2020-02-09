import java.io.*;  
import java.net.*;  
import java.util.Scanner;

public class Client {  
    static boolean running = true;
public static void main(final String[] args) {
            final Socket s;
        try {
            /**Creating Socket To Establish Connection With Local Server */
            s= new Socket("127.0.0.1", 2223);

            /**Data Input And Data Output Streams For Transfering Data From Client To Server and vice-versa */
            final DataInputStream dis = new DataInputStream(s.getInputStream());
            final DataOutputStream dout = new DataOutputStream(s.getOutputStream());

            /**Starting Thread For Smooth Chatting */
            new Send(dout).start();
            new Receive(dis).start();

        } catch (final Exception e) {
            System.out.println(e);
        }
    }
}

/**Thread For Sending Data to Client */
class Send extends Thread {
    DataOutputStream d;
    final Scanner scanner =  new Scanner(System.in);

    public Send(DataOutputStream d) {
        this.d = d;
    }

    public void run() {
        try {
            while (Client.running) {
                String s = null;
                while(Client.running) {
                    if (scanner.hasNext()) {
                        s = scanner.nextLine();
                    }
                    if (s != null) break;
                }
                d.writeUTF(s);
                if (s.contains("logout")) Client.running = false;

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
class Receive extends Thread {
    DataInputStream d;

    public Receive(DataInputStream d) {
        this.d = d;

    }

    public void run() {
        try {
            String s = d.readUTF().toString();
            while (Client.running) {
                System.out.println("From Client: " + s);
                s = d.readUTF().toString();
                if (s.contains("logout")) Client.running = false;
            }
        } catch (final Exception e) {
                e.printStackTrace();
        }
    }
}
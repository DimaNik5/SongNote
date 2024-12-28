import java.io.IOException;
import java.util.Scanner;

public class Client implements TCPConnectionListener{

    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 8080;

    private final TCPConnection connection;

    public static void main(String[] args) {
        new Client();
    }

    public Client(){
        try {
            connection = new TCPConnection(this, IP_ADDRESS, PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);
        String l;
        while (true){
            l = scanner.nextLine() + "\r\n";
            connection.sendString(l);
        }

    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        System.out.println("Connection ready");
    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        System.out.println(value);
    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        System.out.println("Connection disconnect");
    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("Exception " + e);
    }
}

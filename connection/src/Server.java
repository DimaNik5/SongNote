import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

class Server implements TCPConnectionListener{

    public static void main(String[] args) {
        new Server();
    }

    private final List<TCPConnection> connection;

    Server(){
        connection = new ArrayList<>();
        System.out.println("Server running...");
        try {
            InetAddress me = InetAddress.getLocalHost();
            String dottedQuad = me.getHostAddress();
            System.out.println("My address is " + dottedQuad);
        } catch (UnknownHostException e) {
            System.out.println("I'm sorry. I don't know my own address.");
        }

        try(ServerSocket serverSocket = new ServerSocket(8080)){
            while (true){
                try{
                    new TCPConnection(this, serverSocket.accept());
                }catch (IOException e){
                    System.out.println("TCPConnection exception: " + e);
                }
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void onConnectionReady(TCPConnection tcpConnection) {
        connection.add(tcpConnection);
        sendToAllConnection("Client connected " + tcpConnection);
    }

    @Override
    public synchronized void onReceiveString(TCPConnection tcpConnection, String value) {
        sendToAllConnection(value);
    }

    @Override
    public synchronized void onDisconnect(TCPConnection tcpConnection) {
        connection.remove(tcpConnection);
        sendToAllConnection("Client disconnect " + tcpConnection);
    }

    @Override
    public synchronized void onException(TCPConnection tcpConnection, Exception e) {
        System.out.println("TCPConnection exception: " + e);
    }

    private void sendToAllConnection(String value){
        System.out.println(value);
        //connection.forEach(e -> e.sendString(value));
        for (TCPConnection tcpConnection : connection) {
            tcpConnection.sendString(value);
        }
    }
}

package focus.start.task6.server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientInfo {

    private final String userName;
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;
    private long lastCommunication;

    public ClientInfo(String userName, Socket clientSocket, PrintWriter out, BufferedReader in, long lastCommunication) {
        this.userName = userName;
        this.clientSocket = clientSocket;
        this.out = out;
        this.lastCommunication = lastCommunication;
        this.in = in;
    }

    public String getUserName() {
        return userName;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public PrintWriter getOut() {
        return out;
    }

    public synchronized void setLastCommunication(long lastCommunication) {
        this.lastCommunication = lastCommunication;
    }

    public synchronized long getLastCommunication() {
        return lastCommunication;
    }

    public BufferedReader getIn() {
        return in;
    }
}

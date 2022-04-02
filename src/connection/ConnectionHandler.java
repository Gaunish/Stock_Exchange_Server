package connection;

import java.io.*;
import java.net.*;
public class ConnectionHandler {
    private Socket socket;
    BufferedReader in;
    PrintWriter out;
    public ConnectionHandler(Socket socket, String side) throws IOException {
        this.socket = socket;
        if (side.equals("server")) {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } else if (side.equals("client")) {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } else {
            throw new IllegalArgumentException("unknown side");
        }
    }

    public String readXML() throws IOException {
        int len = Integer.parseInt(in.readLine());
        StringBuilder sb = new StringBuilder();
        while (len > 0) {
            char c = (char) in.read();
            sb.append(c);
            len--;
        }
        return sb.toString();
    }

    public void writeXML(String data) throws IOException {
        out.println(data.length());
        out.print(data);
        out.flush();

    }


}

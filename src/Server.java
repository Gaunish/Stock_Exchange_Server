import database.*;
import connection.*;
import xml.*;
import account.*;
import symbol.*;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.DriverManager;
import org.jdom2.*;
import connection.ConnectionHandler;
import org.jdom2.JDOMException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {

    ServerSocket serverSocket;
    ConnectionHandler connectionHandler;
    XMLParser xmlParser;
    XMLDeparser xmlDeparser;
    Socket socket;
    DB conn;

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void accept() throws IOException {
        socket = serverSocket.accept();
        connectionHandler = new ConnectionHandler(socket, "server");
        xmlParser = new XMLParser();
        xmlDeparser = new XMLDeparser();
    }

    public void serve() throws IOException, JDOMException {
        String xmlRequestData = connectionHandler.readXML();
        System.out.println(xmlRequestData);
        XMLRequest request = xmlParser.parse(xmlRequestData);

        DBExecute executor = new DBExecute(conn);
        ResultSet resultSet = executor.execute(request);
        // ResultSet resultSet = request.accept(executor);
    
        String xmlResponseData = xmlDeparser.deparse(resultSet);
        connectionHandler.writeXML(xmlResponseData);
        conn.MatchOrder();
    }

    public ResultSet mockExecute() {
        ResultSet rs = new ResultSet();
        rs.appendResult(new AccountCreateSuccess(1));
        rs.appendResult(new SymbolCreateSuccess("Google", 1));
        rs.appendResult(new AccountCreateError(1, "Account already exists"));
        rs.appendResult(new SymbolCreateError("Googl", 1, "Symbol Not Found"));
        return rs;
    }

    public void initDB(){
        this.conn = new DB();
        conn.connectDB();
        conn.clearTable();
        conn.createTable();
    }

    public void concurrentServe(int numOfThreads) throws IOException {
        // ExecutorService threadPool = Executors.newCachedThreadPool();
        ExecutorService threadPool = Executors.newFixedThreadPool(numOfThreads);
	//ExecutorService threadPool = Executors.newScheduledThreadPool(numOfThreads);
	while (true) {
            Socket socket = serverSocket.accept();
            ConnectionHandler connectionHandler = new ConnectionHandler(socket, "server");
            threadPool.execute(new Runnable() {
                @Override
                public void run() {
                    DB db = new DB();
		    db.connectDB();
                    DBExecute executor = new DBExecute(db);
                    XMLParser xmlParser = new XMLParser();
                    XMLDeparser xmlDeparser = new XMLDeparser();
                    while (true) {
                        try {
                            String xmlRequestData = connectionHandler.readXML();
                            XMLRequest request = xmlParser.parse(xmlRequestData);
                            ResultSet resultSet = executor.execute(request);
                            String xmlResponseData = xmlDeparser.deparse(resultSet);
                            connectionHandler.writeXML(xmlResponseData);
                            db.MatchOrder();
                        } catch (Exception e) {
                            break;
                        }
                    }
                    try {
                        socket.close();
			db.closeConnection();
                    } catch (IOException ignored) {

                    }
                }
            });
        }

    }

    public static void main(String[] args) throws IOException, JDOMException {
        System.out.println("Number of cores available: " + Runtime.getRuntime().availableProcessors());
	Server server = new Server(12345);
        server.initDB();
        server.concurrentServe(32);
       	// server.accept();
//        System.out.println("----Received Connection Request From Client----");
//
//        //Database init
//        //server.initDB();
//        while (true) {
//            try {
//                server.serve();
//            } catch (Exception e) {
//                break;
//            }
//        }

       	// server.accept();
        // System.out.println("----Received Connection Request From Client----");
    
        //Database init
        //server.initDB();
        // while (true) {
           // try {
             //   server.serve();
            // } catch (Exception e) {
              //  break;
           // }
        // }
        
        server.conn.closeConnection();
        
    }


}

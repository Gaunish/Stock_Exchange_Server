package testing;
import connection.*;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client2 {
    public static void main(String[] args) throws IOException {
//        if (args.length != 1) {
//            System.out.println("Usage: ./client server_address");
//            return;
//        }
        Socket socket = new Socket(args.length == 1 ?  args[0] : "localhost", 12345);
        ConnectionHandler connectionHandler = new ConnectionHandler(socket, "client");
        String xml = "<create>\n<account id=\"123\" balance=\"1000.1\"/>\n<account id=\"123\" balance=\"1000.1\"/>\n<symbol sym=\"google\">\n<account id=\"123\">100</account>\n</symbol><symbol sym=\"google\">\n<account id=\"123\">100</account>\n</symbol><symbol sym=\"fb\">\n<account id=\"123\">100</account>\n</symbol>\n</create>\n";
        String xml2 = "<create>\n<account id=\"1234\" balance=\"100023.1\"/>\n<account id=\"123\" balance=\"1000.1\"/>\n<symbol sym=\"google\">\n<account id=\"1234\">100</account>\n</symbol><symbol sym=\"google\">\n<account id=\"123\">100</account>\n</symbol><symbol sym=\"fb\">\n<account id=\"123\">100</account>\n</symbol>\n</create>\n";
        String xml3 = "<transactions id=\"123\">\n<query id=\"5\"/>\n<order sym=\"fb\" amount=\"-400\" limit=\"500.05\"/>\n<order sym=\"fb\" amount=\"10\" limit=\"500.05\"/>\n<order sym=\"apple\" amount=\"-20\" limit=\"200.21\"/>\n<order sym=\"google\" amount=\"-200\" limit=\"5\"/>\n<cancel id=\"10\"/>\n<cancel id=\"10\"/>\n</transactions>";
        String xml4 = "<transactions id=\"1234\">\n<order sym=\"apple\" amount=\"2\" limit=\"500.05\"/>\n<query id=\"2\"/>\n<query id=\"3\"/>\n<order sym=\"fb\" amount=\"-2\" limit=\"100.05\"/>\n<order sym=\"google\" amount=\"100\" limit=\"10.05\"/>\n</transactions>";
        String xml5 = "<transactions id=\"12345\">\n<cancel id=\"4\"/>\n<order sym=\"fb\" amount=\"2\" limit=\"500.05\"/>\n</transactions>";
        String xml7 = "<transactions id=\"123\">\n<cancel id=\"0\"/>\n</transactions>";

        connectionHandler.writeXML(xml);
        connectionHandler.writeXML(xml2);
        connectionHandler.writeXML(xml3);
        connectionHandler.writeXML(xml4);
        connectionHandler.writeXML(xml5);
        connectionHandler.writeXML(xml7);
        System.out.println(connectionHandler.readXML());
        System.out.println(connectionHandler.readXML());
        System.out.println();
        System.out.println(connectionHandler.readXML());
        System.out.println();
        System.out.println(connectionHandler.readXML());
        System.out.println();
        System.out.println(connectionHandler.readXML());
        System.out.println();
        System.out.println(connectionHandler.readXML());
        System.out.println();

        /*String xml6 = "<transactions id=\"1234\">\n<cancel id=\"3\"/>\n</transactions>";
        connectionHandler.writeXML(xml6);
        System.out.println(connectionHandler.readXML());
        System.out.println();*/

    }
}

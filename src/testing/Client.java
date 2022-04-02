package testing;
import connection.*;
import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Random;


public class Client {
    Random random = new Random();
    String[] syms = new String[]{"google", "apple", "amazon", "a", "b", "c", "d", "e", "f", "g"};
    String[] oneSym = new String[]{"google"};
    String[] twoSyms = new String[]{"google", "apple"};
    String[] threeSyms = new String[]{"google", "apple", "amazon"};
    ConnectionHandler connectionHandler;
    public Client(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }
    public String genSym(String[] syms) {
        return syms[random.nextInt(syms.length)];
    }

    public int genAmount(int exclusive) {
        // return [0, exclusive);
        if (exclusive == 0) return 0;
        return random.nextInt(exclusive);
    }

    public int genBalance(int exclusive) {
        return genAmount(exclusive);
    }

    public int genAccountId(int exclusive) {
        return genAmount(exclusive);
    }

    public int genLimit(int exclusive) {
        return genAmount(exclusive);
    }

    public int genOrderAmount(int exclusive) {
        return random.nextInt(2) == 0 ? -random.nextInt(exclusive) : random.nextInt(exclusive);
    }

    public String genOrder(String[] syms, int maxAmount, int maxLimit) {
        return "<order sym=\"" + genSym(syms) + "\" amount=\"" + maxAmount + "\" limit=\"" + maxLimit + "\"/>\n";
    }

    public String genAccount(int accountId, int balance) {
        return "<account id=\"" + accountId + "\" balance=\"" + balance + "\"/>\n";
    }

    public String genSymbol(String symbol, int accountId, int maxShares) {
        return "<symbol sym=\"" + symbol + "\">\n<account id=\"" + accountId + "\">" + maxShares + "</account>\n</symbol>\n";
    }

    public String genCreate(String[] syms, int maxAccountId, int balance, int maxShares) {
        String xml = "<create>\n";
        xml += genAccount(genAccountId(maxAccountId), balance);
        xml += genSymbol(genSym(syms), genAccountId(maxAccountId), maxShares);
        xml += "</create>";
        return xml;
    }

    public String genTransactionOrderOnly(String[] syms, int maxAccountId, int maxAmount, int maxLimit, int i) {
        String xml = "<transactions id = \"" + genAccountId(maxAccountId) + "\">\n";
        if (random.nextInt(2) == 0)
            xml += genOrder(syms, -genAmount(maxAmount), genLimit(3 * maxLimit));
        else
            xml += genOrder(syms, genAmount(maxAmount), genLimit(maxLimit));

        xml += "</transactions>";
        return xml;
    }

    public String genTransaction(String[] syms, int maxAccountId, int maxAmount, int maxLimit, int maxTransactionId) {
        String xml = "<transactions id = \"" + genAccountId(maxAccountId) + "\">\n";
        if (random.nextInt(2) == 0)
            xml += genOrder(syms, -genAmount(maxAmount), genLimit(3 * maxLimit));
        else
            xml += genOrder(syms, genAmount(maxAmount), genLimit(maxLimit));
        xml += "<query id = \"" + genAccountId(maxTransactionId) +"\"/>\n";
        xml += "<cancel id = \"" + genAccountId(maxTransactionId) +"\"/>\n";
        xml += "</transactions>";
        return xml;
    }

    public String genQuery(int maxTransactionId) {
        String xml = "<transactions id = \"0\">";
        for (int j = 0; j <= maxTransactionId; ++j)
            xml += "<query id = \"" + j +"\"/>\n";
        xml += "</transactions>";
        return xml;
    }
    static int no = 0;

    public void display(String req, String resp) throws IOException {
//    	System.out.println("---------Request " + (no++) + "---------");
//    	System.out.println(req);
//    	System.out.println("---------Response---------");
//    	System.out.println(resp);
//	    System.out.println();
	
    }

    public void genTest(int iterations, String[] syms, int maxAccountId, int balance, int maxShares, int maxAmount, int maxLimit) throws IOException {
        int nonCancel = iterations * 9 / 20;
        int cancel = iterations - 2 * nonCancel;
        for (int i = 0; i < nonCancel; ++i) {
            String xml = genCreate(syms, maxAccountId, balance, maxShares);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
        for (int i = 0; i < nonCancel; ++i) {
            String xml = genTransactionOrderOnly(syms, maxAccountId, maxAmount, maxLimit, i);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
        for (int i = 0; i < cancel; ++i) {
            String xml = genTransaction(syms, maxAccountId, maxAmount, maxLimit, nonCancel);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }

    }
    public void test0() throws IOException {
        for (int i = 0; i < 10; ++i) {
            String xml = genCreate(syms, 3, 10000, 1000);
	        connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
        for (int i = 0; i < 10; ++i) {
            String xml = genTransactionOrderOnly(syms, 3, 100, 20, i);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
        for (int i = 0; i < 10; ++i) {
            String xml = genTransaction(syms, 3, 100, 20, 10);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
    }


    public void test1() throws IOException {
        for (int i = 0; i < 100; ++i) {
            String xml = genCreate(syms,30, 10000, 1000);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
        for (int i = 0; i < 100; ++i) {
            String xml = genTransactionOrderOnly(syms,30, 100, 50, i);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
        for (int i = 0; i < 100; ++i) {
            String xml = genTransaction(syms,30, 100, 50, 100);
            connectionHandler.writeXML(xml);
            display(xml, connectionHandler.readXML());
        }
    }

    public static void concurrentTest(String serverAddress, int numOfThreads) throws IOException, InterruptedException {
        Thread[] threads = new Thread[numOfThreads];
        // Socket[] socket = new Socket[numOfThreads];
        Instant time = Instant.now();
        for (int i = 0; i < numOfThreads; ++i) {
            Socket socket = new Socket(serverAddress, 12345);
            ConnectionHandler connectionHandler = new ConnectionHandler(socket, "client");
            int finalI = i;
            threads[i] = new Thread(new Runnable() {
                @Override
                public void run() {
//                    Socket socket = null;
                    try {
                        // socket = new Socket(serverAddress, 12345);
                        // ConnectionHandler connectionHandler = new ConnectionHandler(socket, "client");
                        Client client = new Client(connectionHandler);
                        client.genTest(20, client.twoSyms,2, 1000000, 10000, 100, 10);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[i].start();

        }

//        for (int i = 0; i < numOfThreads; ++i) {
//            threads[i].start();
//        }

        for (int i = 0; i < numOfThreads; ++i) {
            threads[i].join();
        }

        Instant time_ = Instant.now();
        System.out.println("Elapsed time: " + (Duration.between(time, time_).toNanos() + 0.0)/1000000000 + " s");

    }
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: java Client [server_address] [number_of_threads]");
            return;
        }
        Client.concurrentTest(args[0], Integer.parseInt(args[1]));

        // Socket socket = new Socket(args[0], 12345);
        // ConnectionHandler connectionHandler = new ConnectionHandler(socket, "client");
        // long time = System.nanoTime();
        // Client client = new Client(connectionHandler);
        // client.test0();
        // client.test1();
        // client.genTest(100, 30, 10000, 1000, 100, 50);
        // long time_ = System.nanoTime();
        // System.out.println("Elapsed time: " + (time_ - time + 0.0)/1000000000 + " s");
//        String xml = "<create>\n<account id=\"123\" balance=\"1000.1\"/>\n<account id=\"123\" balance=\"1000.1\"/>\n<symbol sym=\"google\">\n<account id=\"123\">100</account>\n</symbol><symbol sym=\"google\">\n<account id=\"123\">100</account>\n</symbol><symbol sym=\"goog\">\n<account id=\"123\">100</account>\n</symbol>\n</create>\n";
//        String xml2 = "<create>\n<account id=\"1234\" balance=\"100023.1\"/>\n<account id=\"123\" balance=\"1000.1\"/>\n<symbol sym=\"google\">\n<account id=\"1234\">100</account>\n</symbol><symbol sym=\"google\">\n<account id=\"123\">100</account>\n</symbol><symbol sym=\"goog\">\n<account id=\"123\">100</account>\n</symbol>\n</create>\n";
//        String xml3 = "<transactions id=\"123\">\n<query id=\"5\"/>\n<order sym=\"apple\" amount=\"2\" limit=\"500.05\"/>\n<order sym=\"apple\" amount=\"-20\" limit=\"500.05\"/>\n<order sym=\"apple\" amount=\"2\" limit=\"500.05\"/>\n</transactions>";
//        String xml4 = "<transactions id=\"1234\">\n<order sym=\"apple\" amount=\"2\" limit=\"500.05\"/>\n<query id=\"2\"/>\n<query id=\"3\"/>\n<order sym=\"fb\" amount=\"-20\" limit=\"100.05\"/>\n</transactions>";
//        String xml5 = "<transactions id=\"12345\">\n<cancel id=\"4\"/>\n<order sym=\"apple\" amount=\"2\" limit=\"500.05\"/>\n</transactions>";
//        Client client = new Client();
//        connectionHandler.writeXML(xml);
//        connectionHandler.writeXML(xml2);
//        connectionHandler.writeXML(xml3);
//        connectionHandler.writeXML(xml4);
//        connectionHandler.writeXML(xml5);
//        System.out.println(connectionHandler.readXML());
//        System.out.println(connectionHandler.readXML());
//        System.out.println();
//        System.out.println(connectionHandler.readXML());
//        System.out.println();
//        System.out.println(connectionHandler.readXML());
//        System.out.println();
//        System.out.println(connectionHandler.readXML());
//        System.out.println();

    }


}


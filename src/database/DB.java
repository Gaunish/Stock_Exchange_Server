package database;
import account.*;
import transaction.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class DB{
    private Connection c;
    private int t_id;
    
    public DB(){
        c = null;
        t_id = 0;
    }

    public void connectDB(){
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://db:5432/postgres?sslmode=disable","postgres", "1234");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            return;
            //System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public String executeQuery(String query, String err){
        String out = "Success";
        try{
            Statement s = c.createStatement();
            String sql = query;
            s.executeUpdate(sql);
            s.close();
        }
        catch ( Exception e ) {
            out = err;
        }
        return out;
    } 

    public void executeStatement(String query){
        try{
            Statement s = c.createStatement();
            s.executeUpdate(query);
            s.close();
        }
        catch ( Exception e ) {
         //System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         return;
        }
    } 

    public ResultSet SelectStatement(String query){
        try{
            Statement s = c.createStatement();
            ResultSet rs = s.executeQuery(query);
            return rs;
        }
        catch (Exception e) {
            //System.err.println(e);
            return null;
        }
    }

    public void createTable(){
        String query1 = "CREATE TABLE ACCOUNT(ACCOUNT_NUM INT PRIMARY KEY NOT NULL, BALANCE DECIMAL(20, 2) NOT NULL DEFAULT 0);";
        executeStatement(query1);
        String query2 = "CREATE TABLE STOCK(ACCOUNT_NUM INT NOT NULL, SYMBOL VARCHAR(10) NOT NULL, NUMBER INT NOT NULL);";
        executeStatement(query2);
        String query3 = "CREATE TABLE OPEN_ORDER(ID INT PRIMARY KEY NOT NULL, ACCOUNT_NUM INT NOT NULL, SYMBOL VARCHAR(10) NOT NULL, AMOUNT INT NOT NULL, LMT DECIMAL(20, 2) NOT NULL, time TIMESTAMP NOT NULL);";
        executeStatement(query3);
        String query4 = "CREATE TABLE EXECUTE_ORDER(ID INT PRIMARY KEY NOT NULL, PARENT_ID INT NOT NULL, ACCOUNT_NUM INT NOT NULL, SYMBOL VARCHAR(10) NOT NULL, AMOUNT INT NOT NULL, PRICE DECIMAL(20, 2) NOT NULL, time TIMESTAMP NOT NULL);";
        executeStatement(query4);
        String query5 = "CREATE TABLE CANCEL_ORDER(ID INT PRIMARY KEY NOT NULL, PARENT_ID INT NOT NULL, ACCOUNT_NUM INT NOT NULL, SYMBOL VARCHAR(10) NOT NULL, AMOUNT INT NOT NULL, time TIMESTAMP NOT NULL);";
        executeStatement(query5);
 
    }

    public ResultSet getSymbol(int acc_no, String symbol){
        String get_query = "Select * from STOCK where ACCOUNT_NUM = "+acc_no+" AND SYMBOL = '" + symbol +"';";
        ResultSet rs = SelectStatement(get_query);
        return rs;
    }

    public String updateSymbol(int stock, int acc_no, String symbol){
        String query = "UPDATE STOCK SET NUMBER = " + stock + "WHERE ACCOUNT_NUM = " + acc_no + "AND SYMBOL ='" + symbol +"';";           
        String out = executeQuery(query, "Symbol update failed");
        return out;
    }

    public void removeSymbol(ResultSet sym, int amt){
        try{
            int no = sym.getInt("NUMBER");
            int acc_no = sym.getInt("ACCOUNT_NUM");
            String symbol = sym.getString("SYMBOL");

            if(amt == no){
                String query = "DELETE FROM STOCK WHERE ACCOUNT_NUM = " + acc_no + " AND SYMBOL = '" + symbol + "';";
                String out = executeQuery(query, "symbol deletion failed"); 
            }
            else{
                updateSymbol(no - amt, acc_no, symbol);
            }
        } 
        catch(Exception e){}
    }

    public String symbolInsert(String symbol, int acc_no, int no_stock){
        String get_query = "Select * from ACCOUNT where ACCOUNT_NUM = " + acc_no + ";";
        ResultSet rs_get = SelectStatement(get_query);
        try{
            if(!rs_get.next()){
                return "Account not found";
            }
        }
        catch(Exception e){ return "Account not found"; }
       
        ResultSet rs = getSymbol(acc_no, symbol);
        
        try{
            if(rs.next()){
                int no;
                int stock = no_stock + rs.getInt("NUMBER");
                return updateSymbol(stock, acc_no, symbol);
            }     
        }
        catch(Exception e){ }

        
        String query = query = "INSERT INTO STOCK(ACCOUNT_NUM, SYMBOL, NUMBER) VALUES(" + acc_no + ", '" + symbol + "', " + no_stock +");";
        String out = executeQuery(query, "Symbol insertion failed");
        return out;
    }

    public void clearTable(){
        String query1 = "DROP TABLE IF EXISTS STOCK;";
        executeStatement(query1);
        String query2 = "DROP TABLE IF EXISTS ACCOUNT;";
        executeStatement(query2);
        String query3 = "DROP TABLE IF EXISTS OPEN_ORDER;";
        executeStatement(query3);
        String query4 = "DROP TABLE IF EXISTS EXECUTE_ORDER;";
        executeStatement(query4);
        String query5 = "DROP TABLE IF EXISTS CANCEL_ORDER;";
        executeStatement(query5);
 
    }

    public void closeConnection(){
        try{
            c.close();
        }
        catch ( Exception e ) {
         //System.err.println( e.getClass().getName()+": "+ e.getMessage() );
         //System.exit(0);
         return;
        }
    }

    public int orderInsert(String symbol, int amount, double limit, int acc_no){
        DBopenOrder temp = new DBopenOrder();
        int out = -1;
        try{
            c.setAutoCommit(false);
            out = temp.orderInsert(this, symbol, amount, limit, acc_no, this.t_id);
            c.commit();
            c.setAutoCommit(true);
            if(out != -1){
                this.t_id += 1;
            }
        }
        catch(Exception e){ return -1; }
        return out;
    }

    public boolean del_open(int id){
        String del_query = "DELETE FROM OPEN_ORDER WHERE ID = " + id + ";";
        String out = executeQuery(del_query, "Open order deletion failed");
        if(!out.equals("Success")){
            return false;
        }
        return true;
    }
    
    public boolean update_bal_buy(int acc_no, double bal, int opt){
        ResultSet rs = getAccount(acc_no);
        if(rs == null){
            return false;
        }

        double curr_bal;
        try{
            curr_bal = rs.getFloat("BALANCE");
        }
        catch(Exception e){ return false; }
       
        double out_bal;
        if(opt == 0){
            out_bal = curr_bal + bal;
        }
        else{
            out_bal = curr_bal - bal;
        }

        String query = "UPDATE ACCOUNT SET balance = " + out_bal + " WHERE ACCOUNT_NUM = " + acc_no + ";";
        String out = executeQuery(query, "Balance update failed");
        if(!out.equals("Success")){
            return false;
        }
        return true;
    }

    public ResultSet getAccount(int acc_no){
        String get_query = "Select * from ACCOUNT WHERE ACCOUNT_NUM =" + acc_no + ";";
        ResultSet rs = SelectStatement(get_query);
        try{
            if(rs.next()){
                return rs;
            }
        }
        catch(Exception e){ }
        return null;
    }

    public int openOrderInsert(String symbol, int amount, double limit, int acc_no){
        DBopenOrder temp = new DBopenOrder();
        return temp.openOrderInsert(this, symbol, amount, limit, acc_no, this.t_id);
    }

    public void getOrder(QuerySuccess s, int id, int acc_no){
        DBTransaction temp = new DBTransaction();
        temp.getOrder(this, s, id, acc_no);
    }

    public void getCancelOrder(CancelSuccess s, int id, int acc_no){
        DBTransaction temp = new DBTransaction();
        temp.getCancelOrder(this, s, id, acc_no, c);
    }

    public void MatchOrder(){
        try{
            c.setAutoCommit(false);
            DBMatch temp = new DBMatch(c);
            temp.MatchOrder(this);
            c.setAutoCommit(true);
        }
        catch(Exception e){}
    }
}

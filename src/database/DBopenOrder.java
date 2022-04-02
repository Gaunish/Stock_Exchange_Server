package database;
import account.*;
import transaction.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;

public class DBopenOrder{
    public int orderInsert(DB conn, String symbol, int amount, double limit, int acc_no, int id){
        //Error checking
        if(amount == 0){
            return -1;
        }

        return openOrderInsert(conn, symbol, amount, limit, acc_no, id);
    }

    public int openOrderInsert(DB conn, String symbol, int amount, double limit, int acc_no, int id){
        ResultSet rs = conn.getAccount(acc_no);
        if(rs == null){
            return -1;
        }
        
        //Check if order is valid
        try{
            if(amount > 0){
                if(rs.getFloat("BALANCE") - (amount * limit) < 0.0){
                    return -1;
                }
                conn.update_bal_buy(acc_no, amount * limit, 1);
            }
            else{
                ResultSet sym = conn.getSymbol(acc_no, symbol);
                if(sym.next()){
                    int curr_amt = sym.getInt("NUMBER");
                    if(curr_amt < -1 * amount){
                        return -1;
                    }
                    conn.removeSymbol(sym, -1 * amount);
                }
                else{
                    return -1;
                }
            }   
        }
        catch(Exception e){ return -1; }
    
        Timestamp now = new Timestamp(System.currentTimeMillis());
        String query = "INSERT INTO OPEN_ORDER(ID, ACCOUNT_NUM, SYMBOL, AMOUNT, LMT, time) VALUES(" + id + ", "+ acc_no + ", '" + symbol + "', " + amount + ", " + limit + ", '"+ now +"');";
        String out = conn.executeQuery(query, "Open order insertion failed");
        if(out.equals("Success")){
            return id;
        }
        
        return -1;
    }
}
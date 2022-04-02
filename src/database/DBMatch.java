package database;
import account.*;
import transaction.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DBMatch{
    private Connection c;

    public DBMatch(Connection c){
        this.c = c;
    }

    public boolean update_open(DB conn, int id, int amount){
        String query = "UPDATE OPEN_ORDER SET AMOUNT = " + amount + " WHERE ID = " + id + ";";
        String out = conn.executeQuery(query, "Open order update failed");
        if(!out.equals("Success")){
            return false;
        }
        return true;
    }

    public boolean insert_exec(DB conn, ResultSet rs, int amount, int parent_id, double price, double buy_price){
        try
        {
            String symbol = rs.getString("SYMBOL");
            int acc_no = rs.getInt("ACCOUNT_NUM");

            //Update buyer, seller
            if(amount > 0){
                conn.symbolInsert(symbol, acc_no, amount);
                double diff = buy_price - price;
                if(diff > 0.0){
                    conn.update_bal_buy(acc_no, amount * diff, 0);
                }
            }
            else{
                conn.update_bal_buy(acc_no, (-1.0 * amount) * price, 0);
            }

            //Insert into execute order
            int insert_id = 0;
            String get_query2 = "Select ID from EXECUTE_ORDER ORDER BY ID DESC;";
            ResultSet rs2 = conn.SelectStatement(get_query2);
            if(rs2.next()){
                insert_id = rs2.getInt(1) + 1;
            }
            
            Timestamp now = new Timestamp(System.currentTimeMillis());
            String query = "INSERT INTO EXECUTE_ORDER(ID, PARENT_ID, ACCOUNT_NUM, SYMBOL, AMOUNT, PRICE, time) VALUES(" + insert_id + ", " + parent_id + ", " + acc_no + ", '" + symbol + "', " + amount + ", " + price + ", '"+ now +"');";

            String out = conn.executeQuery(query, "Exec order insertion failed");
            if(!out.equals("Success")){
                return false;
            }
            return true;
            
        }
        catch(Exception e){}
        return false;
    }

    public void MatchOrder(DB conn){
        ArrayList<String> symbols = new ArrayList<String>();
        String query = "SELECT DISTINCT SYMBOL FROM OPEN_ORDER;";
        try{
            ResultSet sym = conn.SelectStatement(query);
            while(sym.next()){
                symbols.add(sym.getString(1));
            }
        }
        catch(Exception e){return;}
        //System.out.println(symbols);
        
        for(String symbol : symbols){
            MatchSymbolOrder(conn, symbol);
        }
    }

    public void MatchSymbolOrder(DB conn, String symbol){
        String buy_orders = "Select * from OPEN_ORDER WHERE AMOUNT > 0 AND SYMBOL = '"+ symbol +"' ORDER BY AMOUNT DESC;";
        ResultSet buy = conn.SelectStatement(buy_orders);
            
        String sell_orders = "Select * from OPEN_ORDER WHERE AMOUNT < 0 AND SYMBOL = '"+ symbol +"' ORDER BY AMOUNT DESC;";
        ResultSet sell = conn.SelectStatement(sell_orders);

        try{
            buy.next();
            sell.next();
        }
        catch(Exception e){return;}

        while(true){
 
            try{            
                //System.out.println("IDS : " + buy.getInt("ID") + " " + sell.getInt("ID"));
               
                //Get the limits, amounts
                double buy_lmt = buy.getFloat("LMT");
                int buy_amt = buy.getInt("AMOUNT");
                int buy_id = buy.getInt("ID");
                int buy_acc = buy.getInt("ACCOUNT_NUM");
                long buy_time = buy.getTimestamp("time").getTime();

                double sell_lmt = sell.getFloat("LMT");
                int sell_amt = -1 * sell.getInt("AMOUNT");
                int sell_id = sell.getInt("ID");
                int sell_acc = sell.getInt("ACCOUNT_NUM");
                long sell_time = sell.getTimestamp("time").getTime();

                //Base case
                if(sell_lmt > buy_lmt){
                    break;
                }

                //Get the price to sell/buy
                double set_lmt = 0.0;
                if(buy_time < sell_time){
                    set_lmt = buy_lmt;
                }
                else{
                    set_lmt = sell_lmt;
                }

                //Check same acc_no
                if(buy_acc == sell_acc){
                    if(buy_time < sell_time){
                        sell.next();
                    }
                    else{
                        buy.next();
                    }
                    return;
                }

                if(sell_amt == buy_amt){
                    insert_exec(conn, buy, buy_amt, buy_id, set_lmt, buy_lmt);
                    insert_exec(conn, sell, -1 * sell_amt, sell_id, set_lmt, buy_lmt);
                    
                    conn.del_open(buy_id);
                    conn.del_open(sell_id);
                    
                    sell.next();
                    buy.next();
                }
                else if(sell_amt > buy_amt){
                    insert_exec(conn, buy, buy_amt, buy_id, set_lmt, buy_lmt);
                    insert_exec(conn, sell, -1 * (buy_amt), sell_id, set_lmt, buy_lmt);

                    conn.del_open(buy_id);
                    update_open(conn, sell_id, -1 * (sell_amt - buy_amt));

                    buy.next();
                }
                else{
                    insert_exec(conn, sell, -1 * sell_amt, buy_id, set_lmt, buy_lmt);
                    insert_exec(conn, buy, sell_amt, sell_id, set_lmt, buy_lmt);

                    conn.del_open(sell_id);
                    update_open(conn, buy_id, buy_amt - sell_amt);
                   
                    sell.next();
                } 
                c.commit();
            }
            catch(Exception e){break;}
        }
    }
}
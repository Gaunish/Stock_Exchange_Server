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

public class DBTransaction{

    private void getOpen(DB conn, List<Status> s, int id, int acc_no){
        String get_query = "Select * from OPEN_ORDER WHERE ID =" + id + " AND ACCOUNT_NUM = " + acc_no +";";
        ResultSet rs = conn.SelectStatement(get_query);
        try{
            if(rs.next()){
                s.add(new OpenStatus(rs.getInt("AMOUNT")));
            }
        }
        catch(Exception e){ }
    }

    private void getExecuted(DB conn, List<Status> s, int id, int acc_no){
        String get_query2 = "Select * from EXECUTE_ORDER WHERE PARENT_ID =" + id + " AND ACCOUNT_NUM = " + acc_no + ";";
        ResultSet rs2 = conn.SelectStatement(get_query2);
        try{
            while(rs2.next()){
                s.add(new ExecutedStatus(rs2.getInt("AMOUNT"), rs2.getFloat("PRICE"), rs2.getTimestamp("time").getTime()));
            }

        }
        catch(Exception e){ }
    }

    private void getCancelled(DB conn, List<Status> s, int id, int acc_no){
        String get_query3 = "Select * from CANCEL_ORDER WHERE PARENT_ID =" + id + " AND ACCOUNT_NUM = " + acc_no + ";";
        ResultSet rs3 = conn.SelectStatement(get_query3);
        try{
            if(rs3.next()){
                s.add(new CanceledStatus(rs3.getInt("AMOUNT"),rs3.getTimestamp("time").getTime()));
            }

        }
        catch(Exception e){ }   
    }


    public void getOrder(DB conn, QuerySuccess s, int id, int acc_no){
        List<Status> statuses = new ArrayList<Status>();
        getOpen(conn, statuses, id, acc_no);
        getExecuted(conn, statuses, id, acc_no);
        getCancelled(conn, statuses, id, acc_no);
        s.setStatuses(statuses);
    }

    public void cancelOrder(DB conn, CancelSuccess s, int id, int acc_no){
        String get_query = "Select * from OPEN_ORDER WHERE ID =" + id + " AND ACCOUNT_NUM = " + acc_no + ";";
        ResultSet rs = conn.SelectStatement(get_query);
        
        try
        {
            //Insert into cancel order
            int insert_id = 0;
            String get_query2 = "Select ID from CANCEL_ORDER ORDER BY ID DESC;";
            ResultSet rs2 = conn.SelectStatement(get_query2);
            if(rs2.next()){
                insert_id = rs2.getInt(1) + 1;
            }

            if(rs.next())
            {
                Timestamp now = new Timestamp(System.currentTimeMillis());
                String add_cancel = "INSERT INTO CANCEL_ORDER(ID, PARENT_ID, ACCOUNT_NUM, SYMBOL, AMOUNT, time) VALUES(" + insert_id + ", "+ id + ", " + rs.getInt("ACCOUNT_NUM") + ", '" + rs.getString("SYMBOL") + "', " + rs.getInt("AMOUNT") + ", '" + rs.getTimestamp("time") +"');";
                String out = conn.executeQuery(add_cancel, "Cancel order insertion failed");
               /* if(!out.equals("Success")){
                    System.out.println("ERROR : " + out);
                    return false;
                }*/

                double bal = rs.getInt("AMOUNT") * rs.getFloat("LMT");
                update_bal(conn, rs);
                /*if(!update_bal(conn, rs)){
                    return false;
                }*/

                //Delete from open order
                conn.del_open(id);
            }
        }
        catch(Exception e){ }
        
        //return false;
    }

    public void update_bal(DB conn, ResultSet rs){
        try{
            int amt = rs.getInt("AMOUNT");
            int acc_no = rs.getInt("ACCOUNT_NUM");
            if(amt > 0){
                double bal = rs.getInt("AMOUNT") * rs.getFloat("LMT");
                conn.update_bal_buy(acc_no, bal, 0);
            }
            else{
                String out = conn.symbolInsert(rs.getString("SYMBOL"), acc_no, -1 * amt);
                /*if(out.equals("Success")){
                    return;
                }*/
            }
        }
        catch(Exception e){}
    }


    public void getCancelOrder(DB conn, CancelSuccess s, int id, int acc_no, Connection c){
        try{
            c.setAutoCommit(false);
            cancelOrder(conn, s, id, acc_no);
            c.commit();
            c.setAutoCommit(true);
        }
        catch(Exception e){ return; }

        List<Status> statuses = new ArrayList<Status>();
        getExecuted(conn, statuses, id, acc_no);
        getCancelled(conn, statuses, id, acc_no);
        s.setStatuses(statuses);
    }

    //---------------------------------------------------------------------------------------------------

}
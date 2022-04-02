package transaction;

import symbol.Symbol;
import database.*;
import xml.*;

public class OrderRequest implements TransactionRequest{
    String symbol;
    public int amount;
    double limit;
    int acc_no;

    public OrderRequest(String symbol, int amount, double limit, int id) {
        this.symbol = symbol;
        this.amount = amount;
        this.limit = limit;
        this.acc_no = id;   
    }

    @Override
    public Result accept(DBExecute executor) {
        // return executor.visit(this);
        return null;
    }

    public void executeReq(ResultSet rs, DB conn){
        int id = conn.orderInsert(symbol, amount, limit, acc_no);
        //System.out.println("ID : " + id);
        if(id == -1){
            rs.appendResult(new OrderCreateError(symbol, amount, limit, "Order not inserted"));
        }
        else{
            rs.appendResult(new OrderCreateSuccess(symbol, amount, limit, id));
        }
    }
}

package transaction;

import database.*;
import xml.*;

public class CancelRequest implements TransactionRequest {
    int transactionId;
    int acc_no;

    public CancelRequest(int transactionId, int acc_no) {
        this.transactionId = transactionId;
        this.acc_no = acc_no;
    }

    public int getTransactionId() {
        return transactionId;
    }
    @Override
    public Result accept(DBExecute executor) {
        // return executor.visit(this);
        return null;
    }

    public void executeReq(ResultSet rs, DB conn){
        CancelSuccess s = new CancelSuccess(transactionId);
        conn.getCancelOrder(s, transactionId, acc_no);
        if(s.isEmpty()){
            rs.appendResult(new CancelError(transactionId, "Cancel/retrieval failed"));
        }
        else{
            rs.appendResult(s);
        }
    }

}

package transaction;

import database.*;
import xml.*;
import java.util.ArrayList;

public class QueryRequest implements TransactionRequest {
    int transactionId;
    int acc_no;

    public QueryRequest(int transactionId, int acc_no) {
        this.transactionId = transactionId;
        this.acc_no = acc_no;
    }

    @Override
    public Result accept(DBExecute executor) {
        // return executor.visit(this);
        return null;
    }

    public void executeReq(ResultSet rs, DB conn){
        QuerySuccess s = new QuerySuccess(transactionId);
        conn.getOrder(s, transactionId, acc_no);
        if(s.isEmpty()){
            rs.appendResult(new QueryError(transactionId, "Query retrieval failed"));
        }
        else{
            rs.appendResult(s);
        }

    }
}

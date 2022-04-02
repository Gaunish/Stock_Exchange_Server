package transaction;

import create.CreateRequest;
import database.DBExecute;
import xml.ResultSet;
import xml.XMLRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TransactionRequestSet implements XMLRequest {
    public static AtomicInteger transactionIdCounter = new AtomicInteger(0);
    int transactionId;
    List<TransactionRequest> requests;
    int accountId;

    public TransactionRequestSet(int accountId) {
        this.accountId = accountId;
        this.transactionId = transactionIdCounter.getAndIncrement();
        requests = new ArrayList<>();
    }
    
    public List<TransactionRequest> getRequests() {
        return requests;
    }

    public int getAccountId() {
        return accountId;
    }

    public void append(TransactionRequest request) {
        requests.add(request);
    }

    @Override
    public ResultSet accept(DBExecute executor) {
        // return executor.visit(this);
        return executor.execute(this);
    }
}

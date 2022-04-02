package database;
import create.*;
import account.*;
import xml.*;
import transaction.*;
import java.util.List;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBExecute{
    private DB connection;

    public DBExecute(DB conn){
        this.connection = conn;
    }

/*
    public ResultSet execute(XMLRequest request){
        List<CreateRequest> req = request.getRequests();
        ResultSet rs = new ResultSet();

        for(CreateRequest r : req){
            r.executeReq(rs, connection);
        }

        return rs;
    } 
*/
public ResultSet execute(XMLRequest request) {
    return request.accept(this);
}

public ResultSet execute(CreateRequestSet createRequestSet) {
    ResultSet rs = new ResultSet();
    for (CreateRequest request : createRequestSet.getRequests()) {
        request.executeReq(rs, this.connection);
    }

    return rs;
}

public ResultSet execute(TransactionRequestSet transactionRequestSet) {
    ResultSet rs = new ResultSet();
    for (TransactionRequest request : transactionRequestSet.getRequests()) {
        request.executeReq(rs, this.connection);
    }
    return rs;
}

}
  
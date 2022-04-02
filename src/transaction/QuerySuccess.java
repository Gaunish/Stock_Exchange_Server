package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;
import java.util.ArrayList;

import java.util.List;

public class QuerySuccess implements Result {
    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public QuerySuccess(int transactionId, List<Status> statuses) {
        this.statuses = statuses;
        this.transactionId = transactionId;
    }

    public void add(Status s){
        statuses.add(s);
    }

    public boolean isEmpty(){
        return statuses.size() == 0;
    }

    public QuerySuccess(int id){
        this.transactionId = id;
        this.statuses = new ArrayList<Status>();
    }

    List<Status> statuses;
    int transactionId;
    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }
}

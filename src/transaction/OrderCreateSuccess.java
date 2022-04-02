package transaction;

import org.jdom2.Element;
import symbol.Symbol;
import xml.Result;
import xml.XMLDeparser;

public class OrderCreateSuccess implements Result {
    String sym;
    int amount;
    double limit;
    int transactionId;

    public String getSym() {
        return sym;
    }

    public void setSym(String sym) {
        this.sym = sym;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public OrderCreateSuccess(String sym, int amount, double limit, int transactionId) {
        this.sym = sym;
        this.amount = amount;
        this.limit = limit;
        this.transactionId = transactionId;
    }

    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }
}

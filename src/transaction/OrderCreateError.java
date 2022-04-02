package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;

public class OrderCreateError implements Result {
    String sym;
    int amount;
    double limit;
    String errorMsg;

    public String getSym() {
        return sym;
    }

    public void setSym(String symbol) {
        this.sym = symbol;
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

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public OrderCreateError(String symbol, int amount, double limit, String errorMsg) {
        this.sym = symbol;
        this.amount = amount;
        this.limit = limit;
        this.errorMsg = errorMsg;
    }

    @Override
    public Element accept(XMLDeparser deparser) {

        return deparser.visit(this);
    }
}

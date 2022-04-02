package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;

public class ExecutedStatus implements Status {
    int shares;
    double price;
    long time;

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public ExecutedStatus(int shares, double price, long time) {
        this.shares = shares;
        this.price = price;
        this.time = time;
    }

    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }
}

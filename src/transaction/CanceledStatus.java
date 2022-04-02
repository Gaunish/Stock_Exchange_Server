package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;

public class CanceledStatus implements Status {
    int shares;
    long time;

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public CanceledStatus(int shares, long time) {
        this.shares = shares;
        this.time = time;
    }

    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }
}

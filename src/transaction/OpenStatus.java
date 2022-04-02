package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;

public class OpenStatus implements Status {
    int shares;

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public OpenStatus(int shares) {
        this.shares = shares;
    }

    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }
}

package account;
import org.jdom2.Element;
import xml.*;

public class AccountCreateSuccess implements Result {
    int id;
    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }

    public AccountCreateSuccess(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

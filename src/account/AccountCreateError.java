package account;
import org.jdom2.Element;
import xml.*;

public class AccountCreateError implements Result {
    int id;
    String errorMsg;
    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }

    public int getId() {
        return id;
    }

    public AccountCreateError(int id, String errorMsg) {
        this.id = id;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

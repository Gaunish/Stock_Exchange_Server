package symbol;
import org.jdom2.Element;
import xml.*;

public class SymbolCreateError implements Result {
    int id;
    String sym;
    String errorMsg;
    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }

    public SymbolCreateError(String sym, int id, String errorMsg) {
        this.id = id;
        this.sym = sym;
        this.errorMsg = errorMsg;
    }

    public int getId() {
        return id;
    }

    public String getSym() {
        return sym;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}

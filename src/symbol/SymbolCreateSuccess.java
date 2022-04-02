package symbol;
import org.jdom2.Element;
import xml.*;

public class SymbolCreateSuccess implements Result {
    int id;
    String sym;
    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }

    public int getId() {
        return id;
    }

    public SymbolCreateSuccess(String sym, int id) {
        this.id = id;
        this.sym = sym;
    }

    public String getSym() {
        return sym;
    }
}

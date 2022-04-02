package xml;
import org.jdom2.Element;

public interface Result {
    Element accept(XMLDeparser deparser);
}

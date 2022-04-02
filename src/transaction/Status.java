package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;

public interface Status {
    public Element accept(XMLDeparser deparser);
}

package xml;
import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import account.*;
import create.*;
import symbol.*;
import transaction.*;

public class XMLDeparser {
    XMLOutputter xmlOutputter = new XMLOutputter();
    public String deparse(ResultSet rs) {
        Element root = new Element("results");
        Document document = new Document(root);
        for (Result result : rs.getResults()) {
            root.addContent(result.accept(this));
        }
        return xmlOutputter.outputString(document);
    }
    public Element visit(AccountCreateSuccess accountCreateSuccess) {
        Element element = new Element("created");
        element.setAttribute("id", Integer.toString(accountCreateSuccess.getId()));
        return element;
    }

    public Element visit(AccountCreateError accountCreateError) {
        Element element = new Element("error");
        element.setAttribute("id", Integer.toString(accountCreateError.getId()));
        element.setText(accountCreateError.getErrorMsg());
        return element;
    }

    public Element visit(SymbolCreateSuccess symbolCreateSuccess) {
        Element element = new Element("created");
        element.setAttribute("sym", symbolCreateSuccess.getSym());
        element.setAttribute("id", Integer.toString(symbolCreateSuccess.getId()));
        return element;
    }

    public Element visit(SymbolCreateError symbolCreateError) {
        Element element = new Element("error");
        element.setAttribute("sym", symbolCreateError.getSym());
        element.setAttribute("id", Integer.toString(symbolCreateError.getId()));
        element.setText(symbolCreateError.getErrorMsg());
        return element;
    }

    public Element visit(OrderCreateSuccess orderCreateSuccess) {
        Element element = new Element("opened");
        element.setAttribute("sym", orderCreateSuccess.getSym());
        element.setAttribute("amount", Integer.toString(orderCreateSuccess.getAmount()));
        element.setAttribute("limit", Double.toString(orderCreateSuccess.getLimit()));
        element.setAttribute("id", Integer.toString(orderCreateSuccess.getTransactionId()));
        return element;
    }
    public Element visit(OrderCreateError orderCreateError) {
        Element element = new Element("error");
        element.setAttribute("sym", orderCreateError.getSym());
        element.setAttribute("amount", Integer.toString(orderCreateError.getAmount()));
        element.setAttribute("limit", Double.toString(orderCreateError.getLimit()));
        element.setText(orderCreateError.getErrorMsg());
        return element;
    }

    public Element visit(QuerySuccess querySuccess) {
        Element root = new Element("status");
        root.setAttribute("id", Integer.toString(querySuccess.getTransactionId()));
        for (Status status : querySuccess.getStatuses()) {
            root.addContent(status.accept(this));
        }
        return root;
    }

    public Element visit(OpenStatus openStatus) {
        Element element = new Element("open");
        element.setAttribute("shares", Integer.toString(openStatus.getShares()));
        return element;
    }

    public Element visit(CanceledStatus canceledStatus) {
        Element element = new Element("canceled");
        element.setAttribute("shares", Integer.toString(canceledStatus.getShares()));
        element.setAttribute("time", Long.toString(canceledStatus.getTime()));
        return element;
    }

    public Element visit(ExecutedStatus executedStatus) {
        Element element = new Element("executed");
        element.setAttribute("shares", Integer.toString(executedStatus.getShares()));
        element.setAttribute("price", Double.toString(executedStatus.getPrice()));
        element.setAttribute("time", Long.toString(executedStatus.getTime()));
        return element;
    }

    public Element visit(QueryError queryError) {
        Element element = new Element("error");
        element.setAttribute("id", Integer.toString(queryError.getTransactionId()));
        element.setText(queryError.getErrorMsg());
        return element;
    }

    public Element visit(CancelSuccess cancelSuccess) {
        Element root = new Element("canceled");
        root.setAttribute("id", Integer.toString(cancelSuccess.getTransactionId()));
        for (Status status : cancelSuccess.getStatuses()) {
            root.addContent(status.accept(this));
        }
        return root;
    }

    public Element visit(CancelError cancelError) {
        Element element = new Element("error");
        element.setAttribute("id", Integer.toString(cancelError.getTransactionId()));
        element.setText(cancelError.getErrorMsg());
        return element;
    }
}

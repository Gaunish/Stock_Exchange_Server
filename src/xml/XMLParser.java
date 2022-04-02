package xml;
import account.*;
import create.*;
import symbol.*;

import jdk.jshell.spi.ExecutionControl;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import transaction.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class XMLParser {
    SAXBuilder saxBuilder;

    public XMLParser(){
        this.saxBuilder = new SAXBuilder();
    }

    public XMLRequest parse(String data) throws IOException, JDOMException {
        Document document = saxBuilder.build(new StringReader(data));
        Element rootElement = document.getRootElement();
        if (rootElement.getName().equals("create")) {
            return parseCreate(rootElement);
        } else if (rootElement.getName().equals("transactions")) {
            int id = Integer.parseInt(rootElement.getAttributeValue("id"));
            return parseTransaction(rootElement, id);
        } else {
            throw new IllegalArgumentException("Unknown tag " + rootElement.getName());
        }
    }


    private CreateRequestSet parseCreate(Element root) {
        List<CreateRequest> createRequests = new ArrayList<>();
        List<Element> children = root.getChildren();
        for (Element child : children) {
            if (child.getName().equals("account")) {
                int id = Integer.parseInt(child.getAttributeValue("id"));
                double price = Double.parseDouble(child.getAttributeValue("balance"));
                Account account = new Account(id, price);
                CreateRequest request = new CreateAccountRequest(account);
                createRequests.add(request);
            } else if (child.getName().equals("symbol")) {
                Symbol symbol = new Symbol(child.getAttributeValue("sym"));
                List<Account> accounts = new ArrayList<>();
                List<Integer> shares = new ArrayList<>();
                for (Element grandChild : child.getChildren()) {
                    Account account = new Account(Integer.parseInt(grandChild.getAttributeValue("id")));
                    int share = Integer.parseInt(grandChild.getText());
                    accounts.add(account);
                    shares.add(share);
                }
                CreateRequest request = new CreateSymbolRequest(symbol, accounts, shares);
                createRequests.add(request);
            } else {
                throw new IllegalArgumentException("Unknown tag " + child.getName());
            }
        }
        return new CreateRequestSet(createRequests);
    }

    private TransactionRequestSet parseTransaction(Element root, int id) {
        TransactionRequestSet transactionRequestSet = new TransactionRequestSet(Integer.parseInt(root.getAttributeValue("id")));
        List<Element> children = root.getChildren();
        for (Element child : children) {
            if (child.getName().equals("order")) {
                String sym = child.getAttributeValue("sym");
                int amount = Integer.parseInt(child.getAttributeValue("amount"));
                double limit = Double.parseDouble(child.getAttributeValue("limit"));
                transactionRequestSet.append(new OrderRequest(sym, amount, limit, id));
            } else if (child.getName().equals("query")) {
                int transactionId = Integer.parseInt(child.getAttributeValue("id"));
                transactionRequestSet.append(new QueryRequest(transactionId, id));
            } else if (child.getName().equals("cancel")) {
                int transactionId = Integer.parseInt(child.getAttributeValue("id"));
                transactionRequestSet.append(new CancelRequest(transactionId, id));
            } else {
                throw new IllegalArgumentException("Unknown tag " + child.getName());
            }
        }
        return transactionRequestSet;
    }
}

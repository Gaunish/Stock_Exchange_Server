package transaction;

import org.jdom2.Element;
import xml.Result;
import xml.XMLDeparser;

public class CancelError implements Result {
    int transactionId;
    String errorMsg;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public CancelError(int transactionId, String errorMsg) {
        this.transactionId = transactionId;
        this.errorMsg = errorMsg;
    }

    @Override
    public Element accept(XMLDeparser deparser) {
        return deparser.visit(this);
    }
}

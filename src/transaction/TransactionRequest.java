package transaction;

import database.*;
import xml.*;

public interface TransactionRequest {
    public Result accept(DBExecute executor);
    public void executeReq(ResultSet rs, DB conn);
}

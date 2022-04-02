package create;
import xml.*;
import database.*;

import database.DBExecute;
import xml.Result;
import xml.XMLRequest;

public interface CreateRequest {
    public void executeReq(ResultSet rs, DB conn);
}

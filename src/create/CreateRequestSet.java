package create;
import java.util.List;

import database.DBExecute;
import xml.*;

public class CreateRequestSet implements XMLRequest {
    List<CreateRequest> requests;

    public List<CreateRequest> getRequests() {
        return requests;
    }

    public void setRequests(List<CreateRequest> requests) {
        this.requests = requests;
    }

    public CreateRequestSet(List<CreateRequest> requests) {
        this.requests = requests;
    }

    @Override
    public ResultSet accept(DBExecute executor) {
        // return executor.visit(this);
        return executor.execute(this);
    }
}

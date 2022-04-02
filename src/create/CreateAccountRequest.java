package create;
import account.*;
import xml.*;
import database.*;

public class CreateAccountRequest implements CreateRequest {
    Account account;

    public CreateAccountRequest(Account account) {
        this.account = account;
    }

    public Account getAccount() {
        return account;
    }

    public void executeReq(ResultSet rs, DB conn){
        String out = account.insertDB(conn);
        
        if(out.equals("Success")){
            rs.appendResult(new AccountCreateSuccess(account.get_no()));
        }
        else{
            rs.appendResult(new AccountCreateError(account.get_no(), out));
        }
    }

  /*  public void setAccount(Account account) {
        this.account = account;
    }*/
    
}

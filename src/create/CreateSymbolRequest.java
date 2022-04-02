package create;
import java.util.List;

import database.DBExecute;
import symbol.*;
import account.*;
import xml.*;
import database.*;

public class CreateSymbolRequest implements CreateRequest {
    Symbol symbol;
    List<Account> accounts;
    List<Integer> shares;


    public List<Integer> getShares() {
        return shares;
    }

  /*  public void setShares(List<Integer> shares) {
        this.shares = shares;
    }*/


    public Symbol getSymbol() {
        return symbol;
    }

/*
    public void setSymbol(Symbol symbol) {
        this.symbol = symbol;
    }
*/
    public List<Account> getAccounts() {
        return accounts;
    }

/*
    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
*/
    public void executeReq(ResultSet rs, DB conn){
        for(int i = 0; i < accounts.size(); i++){
            String out = insertDB(conn, i);
            if(out.equals("Success")){
                rs.appendResult(new SymbolCreateSuccess(symbol.getSym(), accounts.get(i).get_no()));
            }
            else{
                rs.appendResult(new SymbolCreateError(symbol.getSym(), accounts.get(i).get_no(), out));
            }
        }
    }

    public String insertDB(DB conn, int i){
        return conn.symbolInsert(symbol.getSym(), accounts.get(i).get_no(), shares.get(i));
    }

    public CreateSymbolRequest(Symbol symbol, List<Account> accounts, List<Integer> shares) {
        this.symbol = symbol;
        this.accounts = accounts;
        this.shares = shares;
    }

}

package account;
import database.*;

public class Account{
    private int account_num;
    private double balance;

    public Account(int account_num, double bal){
        this.account_num = account_num;
        this.balance = bal;
    }

    public Account(int id) {
        this.account_num = id;
        this.balance = -1.0;
    }

    public void set_balance(int bal){
        this.balance = bal;
    }

    public double get_balance(){
        return balance;
    }

    public int get_no(){
        return account_num;
    }

    //Method to check if balance is <0
    //after subtracting amt from balanace
    public boolean is_balance(double amt) throws IllegalArgumentException{
        if((balance - amt) < 0.0){
            throw new IllegalArgumentException("Deducting amount " + amt +" is greater than balance " + balance);
        }
        return true;
    }

    public void deduct(double amt) throws IllegalArgumentException{
        try{
            is_balance(amt);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return;
        }
        this.balance -= amt;
    }

    public void add(double amt){
        this.balance += amt;
    }

    public String insertDB(DB db){
        String query = "INSERT INTO ACCOUNT(ACCOUNT_NUM, BALANCE) VALUES(" + account_num + ", " + balance +");";
        String output = db.executeQuery(query, "Account Insertion failed");
        return output;
    }
}
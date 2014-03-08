package implementation.parser;

import java.util.Locale;
import implementation.utils.*;

public class Ntry
{
    private Date date;
    
    private String creditor;
    private String debitor;
    private String memo;
    private double amount = 0;
    private boolean isOutflow = true;
    
    //constructors
    public Ntry(Date date, String debitor, String creditor, String memo, double amount, boolean isOutflow)
    {
        this.date = date;
        this.debitor = rmEscapes(debitor);
        this.creditor = rmEscapes(creditor);
        this.memo = rmEscapes(memo);
        this.amount = amount;
        this.isOutflow = isOutflow;
    }
    
    public String rmEscapes(String s)
    {
        //remove commas
        return s.replace(',', ' ');
    }
    
    //output methods    
    private String dateToString()
    {
        //Format to DD/MM/YYYY
        return String.format("%02d/%02d/%04d", date.getDay(), date.getMonth(), date.getYear());
    }
    
    private String getFloatString(double f)
    {
        return String.format(Locale.ENGLISH, "%.2f", f);
    }
    
    public String toString()
    {
        //return formatted as:
        //OUTFLOW: MM/DD/YYYY,Sample Debitor,[optional category],Sample Memo for an outflow,100.00,
        //or
        //INFLOW: MM/DD/YYYY,Sample Creditor,[optional category],Sample memo for an inflow,,500.00
        
        String payee = isOutflow ? creditor : debitor; //payee should be either creditor or debitor, depending if outFlow or not
        
        String s = dateToString() + "," + payee + ",," + memo + ",";
        
        if (isOutflow)
        {
            s += getFloatString(amount) + ",";
        }
        else
        {
            s+= "," + getFloatString(amount);
        }
        
        return s;
    }
}

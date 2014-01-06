package implementation;

import java.util.Locale;

public class Ntry
{
    private int dateMonth;
    private int dateDay;
    private int dateYear;
    
    private String creditor;
    private String debitor;
    private String memo;
    private double amount = 0;
    private boolean isOutflow = true;
    
    //constructors
    public Ntry(String dateString, String debitor, String creditor, String memo, double amount, boolean isOutflow)
    {
        dateFromString(dateString);
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
    private void dateFromString(String s)
    {
        try
        {
            if (s.length() != 10)
            {
                throw new IllegalArgumentException();
            }
            
            //assume that s is formatted as YYYY-MM-DD
            dateYear = Integer.parseInt(s.substring(0, 4));
            dateMonth = Integer.parseInt(s.substring(5, 7));
            dateDay = Integer.parseInt(s.substring(8));
            
            if (dateYear < 0 || dateMonth < 0 || dateDay < 0)
            {
                throw new IllegalArgumentException();
            }
        }
        catch (Exception e)
        {
            System.out.println("\nWARNING: Illegal date parsed from xml-File. Setting date to 01/01/1970...");
            
            dateMonth = 1;
            dateDay = 1;
            dateYear = 1970;
        }
    }
    
    private String dateToString()
    {
        //Format to DD/MM/YYYY
        return String.format("%02d/%02d/%04d", dateDay, dateMonth, dateYear);
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

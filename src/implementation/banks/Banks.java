package implementation.banks;

import javax.swing.text.html.HTMLDocument.Iterator;

public enum Banks
{
    KREISSPARKASSE_KOELN, POSTBANK;
    
    public String abbreviation()
    {
        switch(this)
        {
            case KREISSPARKASSE_KOELN:
                return "ksk";
            case POSTBANK:
                return "pb";
            default:
                return "Not specified!";
        }        
    }
    
    public String toString()
    {
        switch(this)
        {
            case KREISSPARKASSE_KOELN:
                return "Kreissparkasse Köln";
            case POSTBANK:
                return "Postbank";
            default:
                return "Not specified!";
        }
    }
    
    /*public String[] getBanksList()
    {
        
    }*/
}

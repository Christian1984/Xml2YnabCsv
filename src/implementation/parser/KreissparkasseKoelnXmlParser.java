package implementation.parser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import implementation.utils.*;

public class KreissparkasseKoelnXmlParser extends XmlParser
{
    /*relevant nodes in postbank xml document:
     * Base Node ->     Umsatzliste
     * Entries ->           ..\Umsatz
     * 
     * relevant nodes inside each entry
     * Amount ->                ..\Betrag
     * outFlow ->               ==> if Amount < 0
     * date ->                  ..\Datum
     * debitor ->               ..\Name
     * creditor ->              ..\Name
     * memo ->                  ..\Verwendungszweck
     */  
    
    private static final String XML_KEY_NTRY = "Umsatz";
    private static final String XML_KEY_AMOUNT = "Betrag";    
    private static final String XML_KEY_BOOKINGDATE = "Datum";
    private static final String XML_KEY_NM = "Name";
    private static final String XML_KEY_MEMO = "Verwendungszweck";
    
    public KreissparkasseKoelnXmlParser(String xmlFileName) throws Exception
    {
        super(xmlFileName);
    }
    
    protected Ntry[] buildNtryArray(Document xmlDoc)
    {        
        NodeList nodes = xmlDoc.getElementsByTagName(XML_KEY_NTRY);
        
        Ntry[] ntryArray = new Ntry[nodes.getLength()];
        
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            Element element = (Element) node;
           
            String amount = getValue(new String[] {XML_KEY_AMOUNT}, element);
            boolean outFlow = amount.contains("-");
            
            //strip out minus and dots; replace german decimal-',' with '.'
            amount = amount.replace("-", "");
            amount = amount.replace(".", "");
            amount = amount.replace(',', '.');
            
            String dateString = getValue(new String[] {XML_KEY_BOOKINGDATE}, element);
            String name = getValue(new String[] {XML_KEY_NM}, element);
            String memo = getValue(new String[] {XML_KEY_MEMO}, element);
            
            String ntryString = "====================\nNtry:" + "\nAmount: " + amount + "\nOutFlow: " + outFlow +
                "\nDate: " + dateString + "\nName: " + name + "\nMemo: " + memo + "\n====================";            
            //System.out.println(ntryString);
            
            Date date = DateConversion.dateFromDD_MM_YYYY(dateString, this);
            
            try
            {
                Ntry ntry = new Ntry(date, name, name, memo, Double.parseDouble(amount), outFlow);
                ntryArray[i] = ntry;
                countNtry();
            }
            catch (Exception ex)
            {
                countError();
                System.out.println("ERROR: The following entry could not be added:\n" + ntryString + "\n" + ex.toString());
            }
        }
        
        return ntryArray;
    }
}

package implementation.parser;

import implementation.utils.Date;
import implementation.utils.DateConversion;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PostbankXmlParser extends XmlParser
{
    /*relevant nodes in postbank xml document:
     * Base Node ->     BkToCstmrAcctRpt\Rpt
     * Entries ->           ..\Ntry
     * 
     * relevant nodes inside each entry
     * Amount ->                ..\Amt
     * outFlow ->               ..\CdtDbtInd (inFlow: CRDT, outFlow: DBIT)
     * date ->                  ..\BookgDt\Dt (Format: YYYY-MM-DD)
     * debitor ->               ..\NtryDtls\TxDtls\RltdPties\Dbtr\Nm
     * creditor ->              ..\NtryDtls\TxDtls\RltdPties\Cdtr\Nm
     * memo ->                  ..\NtryDtls\TxDtls\RmtInf\Ustrd (multiline text)
     */  
    
    private static final String XML_KEY_NTRY = "Ntry";
    private static final String XML_KEY_AMOUNT = "Amt";
    private static final String XML_KEY_FLOW = "CdtDbtInd";
    
    private static final String XML_KEY_BOOKINGDATE = "BookgDt";
    private static final String XML_KEY_DATE = "Dt";
    
    private static final String XML_KEY_NTRYDTLS = "NtryDtls";
    private static final String XML_KEY_TXDTLS = "TxDtls";
    private static final String XML_KEY_RLTDPTIES = "RltdPties";
    private static final String XML_KEY_DBTR = "Dbtr";
    private static final String XML_KEY_CDTR = "Cdtr";
    private static final String XML_KEY_NM = "Nm";
    private static final String XML_KEY_RMTINF = "RmtInf";
    private static final String XML_KEY_USTRD = "Ustrd";
    
    //private static final String INFLOW_INDICATOR = "CRDT";
    private static final String OUTFLOW_INDICATOR = "DBIT";
    
    public PostbankXmlParser(String xmlFileName) throws Exception
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
            String flow = getValue(new String[] {XML_KEY_FLOW}, element);
            String dateString = getValue(new String[] {XML_KEY_BOOKINGDATE, XML_KEY_DATE}, element);
            String debitor = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RLTDPTIES, XML_KEY_DBTR, XML_KEY_NM}, element);
            String creditor = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RLTDPTIES, XML_KEY_CDTR, XML_KEY_NM}, element);
            String memo = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RMTINF, XML_KEY_USTRD}, element);
            
            String ntryString = "====================\nNtry:" + "\nAmount: " + amount + "\nFlow: " + flow +
                "\nDate: " + dateString + "\nDebitor: " + debitor + "\nCreditor: " + creditor + "\nMemo: " + memo + "\n====================";
            
            //System.out.println(ntryString);
            
            Date date = DateConversion.dateFromYYYY_MM_DD(dateString, this);
            
            try
            {
                Ntry ntry = new Ntry(date, debitor, creditor, memo, Double.parseDouble(amount), flow.equals(OUTFLOW_INDICATOR)?true:false);
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

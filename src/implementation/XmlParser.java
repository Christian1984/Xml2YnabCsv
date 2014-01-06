package implementation;

import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlParser
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
    
    //private String fileName;
    private Document xmlDoc;
    //private List<Ntry> ntryList;
    private Ntry[] ntryArray;
    
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
    
    
    //constructors
    public XmlParser(String xmlFileName)
    {
        //fileName = xmlFileName;
        xmlDoc = FileIO.getXmlDocFromFile(xmlFileName);
        //ntryList = buildNtryList();
        ntryArray = buildNtryArray();
    }
    
    //getters
    public Document getXmlDoc()
    {
        return xmlDoc;
    }
    
    //methods
    /*private LinkedList<Ntry> buildNtryList()
    {
        LinkedList<Ntry> list = new LinkedList<Ntry>();
        
        NodeList nodes = xmlDoc.getElementsByTagName(XML_KEY_NTRY);
        
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            Element element = (Element) node;
           
            String amount = getValue(new String[] {XML_KEY_AMOUNT}, element);
            String flow = getValue(new String[] {XML_KEY_FLOW}, element);
            String date = getValue(new String[] {XML_KEY_BOOKINGDATE, XML_KEY_DATE}, element);
            String debitor = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RLTDPTIES, XML_KEY_DBTR, XML_KEY_NM}, element);
            String creditor = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RLTDPTIES, XML_KEY_CDTR, XML_KEY_NM}, element);
            String memo = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RMTINF, XML_KEY_USTRD}, element);
            
            String ntryString = "====================\nNtry:" + "\nAmount: " + amount + "\nFlow: " + flow +
                "\nDate: " + date + "\nDebitor: " + debitor + "\nCreditor: " + creditor + "\nMemo: " + memo + "\n====================";
            
            //System.out.println(ntryString);
            
            try
            {
                Ntry ntry = new Ntry(date, debitor, creditor, memo, Double.parseDouble(amount), flow.equals(OUTFLOW_INDICATOR)?true:false);
                list.add(ntry);
            }
            catch (Exception ex)
            {
                System.out.println("ERROR: The following entry could not be added:\n" + ntryString + "\n" + ex.toString());
            }
        }
        
        return list;
    }*/
    
    private Ntry[] buildNtryArray()
    {
        LinkedList<Ntry> list = new LinkedList<Ntry>();
        
        NodeList nodes = xmlDoc.getElementsByTagName(XML_KEY_NTRY);
        
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Node node = nodes.item(i);
            Element element = (Element) node;
           
            String amount = getValue(new String[] {XML_KEY_AMOUNT}, element);
            String flow = getValue(new String[] {XML_KEY_FLOW}, element);
            String date = getValue(new String[] {XML_KEY_BOOKINGDATE, XML_KEY_DATE}, element);
            String debitor = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RLTDPTIES, XML_KEY_DBTR, XML_KEY_NM}, element);
            String creditor = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RLTDPTIES, XML_KEY_CDTR, XML_KEY_NM}, element);
            String memo = getValue(new String[] {XML_KEY_NTRYDTLS, XML_KEY_TXDTLS, XML_KEY_RMTINF, XML_KEY_USTRD}, element);
            
            String ntryString = "====================\nNtry:" + "\nAmount: " + amount + "\nFlow: " + flow +
                "\nDate: " + date + "\nDebitor: " + debitor + "\nCreditor: " + creditor + "\nMemo: " + memo + "\n====================";
            
            //System.out.println(ntryString);
            
            try
            {
                Ntry ntry = new Ntry(date, debitor, creditor, memo, Double.parseDouble(amount), flow.equals(OUTFLOW_INDICATOR)?true:false);
                list.add(ntry);
            }
            catch (Exception ex)
            {
                System.out.println("ERROR: The following entry could not be added:\n" + ntryString + "\n" + ex.toString());
            }
        }
        
        //return list.toArray(new Ntry[0]);
        return list.toArray(new Ntry[list.size()]);
    }
    
    /*private String getValue(String key, Element e)
    {
        Node node = e.getElementsByTagName(key).item(0).getChildNodes().item(0);
        //System.out.println(node.getNodeName());
        //System.out.println(node.getChildNodes().item(0));
        return node.getNodeValue();
    }*/
    
    private String getValue(String[] keyList, Element e)
    {
        try
        {
            Node node = e;
            
            for (String key : keyList)
            {
                node = ((Element) node).getElementsByTagName(key).item(0);//.getChildNodes().item(0);
                //System.out.println("Key: " + node.getNodeName());
                //System.out.println("Value: " + node.getChildNodes().item(0).getNodeValue());
            }
            
            return node.getChildNodes().item(0).getNodeValue();
        }
        catch (Exception ex)
        {
            System.out.println("ERROR: Key not found: " + ex.toString());
            return "Key not found!";
        }        
    }
    
    //output
    public String xmlDocToString()
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        
        try {
            transformer = tf.newTransformer();
            
            // below code to remove XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
            String output = writer.getBuffer().toString();
            return output;
        } catch (TransformerException e) {
            e.printStackTrace();
        }
         
        return null;
    }
    
    public String toString()
    {
        String s = "Date,Payee,Category,Memo,Outflow,Inflow\n";
        
        //for (Ntry ntry : ntryList)
        for (Ntry ntry : ntryArray)
        {
            s += ntry.toString() + "\n";
        }
        
        return s;
    }
}

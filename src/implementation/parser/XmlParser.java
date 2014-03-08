package implementation.parser;

import implementation.utils.FileIO;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public abstract class XmlParser
{
    
    
    //private String fileName;
    private Document xmlDoc;
    private String xmlFileName;
    private Ntry[] ntryArray;
    private int errorCount = 0;
    private int ntryCount = 0;
    
    private long startTime = System.currentTimeMillis();
    
    //abstract methods    
    protected abstract Ntry[] buildNtryArray(Document xmlDoc);    
    
    //constructors
    public XmlParser(String xmlFileName) throws Exception
    {
        this.xmlFileName = xmlFileName;
        xmlDoc = FileIO.getXmlDocFromFile(xmlFileName);
        ntryArray = buildNtryArray(xmlDoc);
    }
    
    //getters
    public Document getXmlDoc()
    {
        return xmlDoc;
    }
    
    public String statisticsInfo()
    {
        return "RESULT: Entries read from file: " + ntryCount 
            + "; Errors occured: " + errorCount
            + "; Total time consumed: " + (System.currentTimeMillis() - startTime) + " Milliseconds.";
    }
    
    protected String getValue(String[] keyList, Element e)
    {
        String currentKey = new String();
        
        try
        {
            Node node = e;
            
            for (String key : keyList)
            {
                currentKey = key;
                node = ((Element) node).getElementsByTagName(key).item(0);//.getChildNodes().item(0);
                //System.out.println("Key: " + node.getNodeName());
                //System.out.println("Value: " + node.getChildNodes().item(0).getNodeValue());
            }
            
            return node.getChildNodes().item(0).getNodeValue();
        }
        catch (Exception ex)
        {
            System.out.println("WARNING: Key not found: <" + currentKey + ">"
                + "\nException: " + ex.toString() + "\nReturning 'Key not found!' instead...\n");
            countError();
            
            return "Key not found!";
        }        
    }
    
    //methods
    public void countError()
    {
        errorCount++;
    }
    
    public void countNtry()
    {
        ntryCount++;
    }
    
    //output
    public String getResultString()
    {
        String s = new String();
        
        if (ntryCount > 0)
        {
            //success
            s =  "SUCCESS: File " + xmlFileName + ".csv successfully written to HDD!";
        }
        else
        {
            //warning: wrong bank?
            s = "WARNING: No Entries could be read from xml-file. Did you select the wrong bank?";
        }
        /*else if (errorCount > 0)
        {
            //warning: errors occured
            s = "WARNING: Errors occured.";
        }*/
        
        return s + "\nEntries read from file: " + ntryCount + "\nErrors and Warnings: " + errorCount;
    }
    
    public String toString()
    {
        String s = "Date,Payee,Category,Memo,Outflow,Inflow\n";
        
        for (Ntry ntry : ntryArray)
        {
            s += ntry.toString() + "\n";
        }
        
        return s;
    }
}

package debugging;

import implementation.parser.*;
import implementation.utils.FileIO;

public class Testcode
{
    public static void main(String[] args)
    {
        System.out.println("Debbuging and testing date:");
        
        /*Ntry ntry = new Ntry("2014-01-02", "Test-Debitor", "Test-Creditor", "Test-Memo", 155, true);
        System.out.println(ntry.toString());        

        ntry = new Ntry("20140-101-02", "Test-Debitor", "Test-Creditor", "Test-Memo", 155, true);
        System.out.println(ntry.toString());
        
        ntry = new Ntry("20140-1-02", "Test-Debitor", "Test-Creditor", "Test-Memo", 155, true);
        System.out.println(ntry.toString());
        
        ntry = new Ntry("20140:1-02", "Test-Debitor", "Test-Creditor", "Test-Memo", 155, true);
        System.out.println(ntry.toString());
        
        ntry = new Ntry("20140:1-02", "Test-Debitor", "Test-Creditor", "Test-Memo", 155, false);
        System.out.println(ntry.toString());
        
        
        
        System.out.println("\nTest on 'real' data!");
        
        ntry = new Ntry("2014-01-02", "Michael Embacher", "Christian Hoffmann",  "Referenz Seriennummer: 1B74", 4.95, true);
        System.out.println(ntry.toString());
        
        ntry = new Ntry("2014-01-02", "Christian Hoffmann", "HUK-COBURG VVAG", "BEITRAG F. 508/374838-Z", 700.81, false);
        System.out.println(ntry.toString());*/
        
        
        
        System.out.println("\nOpen xml doc!");
        String fileName = "sample.xml";
        XmlParser xmlParser = null;
        
        try 
        {
            xmlParser = new KreissparkasseKoelnXmlParser(fileName);
        }
        catch (Exception e)
        {
            System.out.println("ERROR: An Error occured opening the xml-document " + fileName + ":\n" + e.toString());
            System.exit(1);
        }

        System.out.println("\nContent of csv-File to be saved to HDD!");        
        System.out.println(xmlParser.toString());

        System.out.println("\nSaving csv-File to disc!");
        FileIO.writeCsvFile(fileName, xmlParser.toString());
    }
}

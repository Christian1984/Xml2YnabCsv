package implementation;

import implementation.parser.*;
import implementation.utils.FileIO;

public class KreissparkasseKoelnXml2YnabCsv
{
    public static void main(String[] args)
    {
        String fileName = "ksk_1395164906.xml"; //TODO: repair
        System.out.println("\nINFO: Reading xml-document named " + fileName + "!");
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

        System.out.println("\nINFO: Content of csv-File to be saved to HDD!");        
        System.out.println(xmlParser.toString());        
        System.out.println(xmlParser.statisticsInfo());

        System.out.println("\nINFO: Saving csv-File to disc!");
        FileIO.writeCsvFile(fileName, xmlParser.toString());
        
    }
}

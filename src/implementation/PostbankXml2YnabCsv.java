package implementation;

import implementation.parser.*;
import implementation.utils.FileIO;

public class PostbankXml2YnabCsv
{
    public static void main(String[] args)
    {
        String fileName = "in.xml"; //TODO: repair
        System.out.println("\nINFO: Reading xml-document named " + fileName + "!");
        XmlParser xmlParser = null;
        
        try 
        {
            xmlParser = new PostbankXmlParser(fileName);
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

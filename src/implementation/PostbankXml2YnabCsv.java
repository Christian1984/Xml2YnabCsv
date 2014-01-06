package implementation;

public class PostbankXml2YnabCsv
{
    public static void main(String[] args)
    {
        String fileName = "in.xml";
        System.out.println("\nINFO: Reading xml-document named " + fileName + "!");
        XmlParser xmlParser = new XmlParser(fileName);

        System.out.println("\nINFO: Content of csv-File to be saved to HDD!");        
        System.out.println(xmlParser.toString());

        System.out.println("\nINFO: Saving csv-File to disc!");
        FileIO.writeCsvFile(fileName, xmlParser.toString());
    }
}

package implementation;

import implementation.parser.*;
import implementation.banks.*;
import implementation.utils.FileIO;

public class Xml2YnabCsv
{
    public static void showHelp()
    {
        System.out.println("Please use command line parameters:");
        System.out.println(">>> java Xml2YnabCsv bank [filename]");
        System.out.println("Example: java Xml2YnabCsv pb C:\\MyXmls\\input.csv");
        System.out.println();
        System.out.println("Supported Banks:");

        for (Banks b : Banks.values())
        {
            System.out.println(String.format("Key: %5s -> Name: %s", b.abbreviation(), b.toString()));
        }

        System.out.println();
        System.out.println("Optional Parameters:");
        System.out.println("[filename] -> Specifies which file to convert. "
            + "File needs to be in .\\xml-subfolder." 
            + "\nTarget file will be written to .\\csv\\[filename].csv!"
            + "\nIf [filename] is not specified Xml2YnabCsv will look for a file named in.xml!");
        
        System.exit(1);
    }

    public static void main(String[] args)
    {
        String fileName = "in.xml";
        Banks bank = Banks.POSTBANK;
        
        // Command line: java Xml2YnabCsv bank [filename] [-v]
        if (args.length < 1)
        {
            showHelp();
        }
        else if (args.length > 0)
        {
            //figure out what bank the user wants...
            if (args[0].equals(Banks.POSTBANK.abbreviation()))
            {
                bank = Banks.POSTBANK;
            }
            else if (args[0].equals(Banks.KREISSPARKASSE_KOELN.abbreviation()))
            {
                bank = Banks.KREISSPARKASSE_KOELN;
            }
            else
            {
                System.out.println("Bank " + args[0] + " unknown. See below for supported banks!");
                showHelp();
            }
            
            //get filename
            if (args.length > 1)
            {
                fileName = args[1];
            }
        }
        

        System.out.println("\nINFO: Reading xml-document named " + fileName + " from bank " + bank.toString() + "!");
        
        XmlParser xmlParser = null;
        
        try
        {
            switch(bank)
            {
                case POSTBANK:
                    xmlParser = new PostbankXmlParser(fileName);
                    break;
                case KREISSPARKASSE_KOELN:
                    xmlParser = new KreissparkasseKoelnXmlParser(fileName);
                    break;
                default:
                    System.out.println("ERROR: No such bank!");
                    return;
            }
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

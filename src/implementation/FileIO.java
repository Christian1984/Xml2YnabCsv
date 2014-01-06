package implementation;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.FileSystemException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class FileIO
{
    public static Document getXmlDocFromFile(String xmlFileName)
    {
        try
        {
            File xmlFile = new File("xml\\" + xmlFileName);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            return doc;
        }
        catch (Exception e)
        {
            System.out.println("ERROR: An Error occured opening the xml-document " + xmlFileName + ":\n" + e.toString());
            System.exit(1);
            return null;
        }
    }
    
    public static boolean writeCsvFile(String fileName, String fileContent)
    {
        PrintWriter out = null;
        
        try
        {
            String fileExtension = "csv";            

            if (createFolder(fileExtension))
            {
                out = new PrintWriter(fileExtension + "\\" + fileName + "." + fileExtension);
                out.print(fileContent);

                System.out.println("INFO: File " + fileName + ".csv successfully written to HDD!");
                
                return true;
            }
            else
            {
                throw new FileSystemException("ERROR: Folder " + fileExtension + " could not be created!");
            }
            
        }
        catch (Exception ex)
        {
            System.out.println("ERROR: Could not write the file " + fileName + " to HDD!\n" + ex.toString());
            return false;
        }
        finally
        {
            if (out != null) out.close();
        }
    }
    
    public static boolean createFolder(String folderName) throws Exception
    {
        File dir = new File(folderName);
        boolean result = false;
        
        if (!dir.exists())
        {
            System.out.println("INFO: Creating folder " + folderName);
            result = dir.mkdir();
        }
        else
        {
            System.out.println("INFO: Folder " + folderName + " found.");
            result = true;
        }
            
        return result;
    }
}

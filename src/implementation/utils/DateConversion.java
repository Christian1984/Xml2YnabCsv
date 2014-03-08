package implementation.utils;

import implementation.parser.XmlParser;

public class DateConversion
{
    public static Date dateFromYYYY_MM_DD(String s, XmlParser xmlParser)
    {
        try
        {
            if (s.length() != 10)
            {
                throw new IllegalArgumentException();
            }
            
            //assume that s is formatted as YYYY-MM-DD (- can be replaced by . or whatever)
            int dateYear = Integer.parseInt(s.substring(0, 4));
            int dateMonth = Integer.parseInt(s.substring(5, 7));
            int dateDay = Integer.parseInt(s.substring(8));
            
            if (dateYear < 0 || dateMonth < 0 || dateDay < 0)
            {
                throw new IllegalArgumentException();
            }
            
            return new Date(dateDay, dateMonth, dateYear);
        }
        catch (Exception e)
        {
            System.out.println("\nWARNING: Illegal date parsed from xml-File.\n"
                + "Expecting format YYYY-MM-DD. Given was: " + s + "\n"
                + "Setting date to 01/01/1970...\n");
            
            xmlParser.countError();
            
            return new Date(1, 1, 1970);
        }
    }

    public static Date dateFromDD_MM_YYYY(String s, XmlParser xmlParser)
    {
        try
        {
            if (s.length() != 10)
            {
                throw new IllegalArgumentException();
            }
            
            //assume that s is formatted as DD/MM/YYYY (/ can be replaced by . or whatever)
            int dateDay = Integer.parseInt(s.substring(0, 2));
            int dateMonth = Integer.parseInt(s.substring(3, 5));
            int dateYear = Integer.parseInt(s.substring(6));
            
            if (dateYear < 0 || dateMonth < 0 || dateDay < 0)
            {
                throw new IllegalArgumentException();
            }
            
            return new Date(dateDay, dateMonth, dateYear);
        }
        catch (Exception e)
        {
            System.out.println("\nWARNING: Illegal date parsed from xml-File.\n"
                + "Expecting format DD-MM-YYYY. Given was: " + s + "\n"
                + "Setting date to 01/01/1970...\n");

            xmlParser.countError();
            
            return new Date(1, 1, 1970);
        }
    }
}

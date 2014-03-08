package gui;

import implementation.banks.Banks;
import implementation.parser.*;
import implementation.utils.FileIO;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.*;


public class Xml2YnabCsvGui extends JFrame implements ActionListener
{
    //Frame
    //JFrame frame;
    JPanel panel;
    
    //Selection Dialog
    JPanel settingsPanel;
    JPanel xmlFileSelectionPanel; 
    JTextField txtFileName;
    JButton btnSelectFile;
    
    JPanel bankSelectionPanel;
    JLabel lblSelectBank;
    JComboBox<Banks> cmbBank;
    
    //Start/Exit    
    JPanel startQuitPanel;
    JButton btnQuit;
    JButton btnStart;
    
    String xmlFileName;
    
    public Xml2YnabCsvGui()
    {
        //FileSelectionPanel
        btnSelectFile = new JButton("Select File...");
        btnSelectFile.addActionListener(this);
        txtFileName = new JTextField(20);

        xmlFileSelectionPanel = new JPanel();
        xmlFileSelectionPanel.setLayout(new java.awt.FlowLayout());
        xmlFileSelectionPanel.add(txtFileName);
        xmlFileSelectionPanel.add(btnSelectFile);
        
        //BankSelection
        lblSelectBank = new JLabel("Select your bank:");
        cmbBank = new JComboBox<Banks>(Banks.values());
        
        bankSelectionPanel = new JPanel();
        bankSelectionPanel.setLayout(new java.awt.FlowLayout());
        bankSelectionPanel.add(lblSelectBank);
        bankSelectionPanel.add(cmbBank);
        
        //SettingsPanel
        settingsPanel = new JPanel();
        settingsPanel.setLayout(new javax.swing.BoxLayout(settingsPanel, javax.swing.BoxLayout.Y_AXIS));
        settingsPanel.setBorder(BorderFactory.createTitledBorder("Settings"));
        settingsPanel.add(xmlFileSelectionPanel);
        settingsPanel.add(bankSelectionPanel);
      
        //StartQuitPanel
        btnStart = new JButton("Start Conversion...");
        btnStart.addActionListener(this);
        btnQuit = new JButton("Quit");
        btnQuit.addActionListener(this);
        
        startQuitPanel = new JPanel();
        startQuitPanel.setLayout(new java.awt.FlowLayout());        
        startQuitPanel.add(btnQuit);
        startQuitPanel.add(btnStart); 
        
        //main panel
        panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        panel.add(settingsPanel);
        panel.add(startQuitPanel);
        
        //frame
        this.setTitle("Xml2YnabCsv");
        this.setSize(400, 200);
        //this.setResizable(false);
        this.add(panel);
        this.setLocationRelativeTo(null); //show centered
        this.setVisible(true);
        
    }
    
    public void startConversion()
    {        
        if (xmlFileName != null)
        {            
            //figure out what bank the user wants...
            Banks bank = (Banks) cmbBank.getSelectedItem();
            System.out.println("INFO: Selected Bank -> " + bank);
            
            //get latest xmlfileName
            xmlFileName = txtFileName.getText();
            System.out.println("INFO: Filename -> " + xmlFileName);
            
            //create parser and start conversion
            XmlParser xmlParser = null;
            
            try
            {
                switch(bank)
                {
                    case POSTBANK:
                        xmlParser = new PostbankXmlParser(xmlFileName);
                        break;
                    case KREISSPARKASSE_KOELN:
                        xmlParser = new KreissparkasseKoelnXmlParser(xmlFileName);
                        break;
                    default:
                        System.out.println("ERROR: No such bank!");
                        return;
                }
            }
            catch (Exception e)
            {
                System.out.println("ERROR: An Error occured opening the xml-document " + xmlFileName + ":\n" + e.toString());
                JOptionPane.showMessageDialog(null, "ERROR: An Error occured opening the xml-document " + xmlFileName + ":\n" + e.toString());
                return;
            }  
            
            //save file
            System.out.println("\nINFO: Content of csv-File to be saved to HDD!");        
            System.out.println(xmlParser.toString());        
            System.out.println(xmlParser.statisticsInfo());

            System.out.println("\nINFO: Saving csv-File to disc!");
            if (FileIO.writeCsvFile(xmlFileName, xmlParser.toString()))
            {
                //report success
                //TODO: build success-msg based on errorCount and ntryCount etc.
                JOptionPane.showMessageDialog(null, xmlParser.getResultString());
            }
            else
            {
                //report error
                JOptionPane.showMessageDialog(null, "ERROR: Could not write file " + xmlFileName + ".csv to HDD!");
            }
        }
        else
        {
            System.out.println("Keine Datei ausgewählt...");
        } 
    }
    
    public String selectXmlFile()
    {
        //create file chooser
        JFileChooser fileChooser = new JFileChooser();
        
        //set filter
        FileFilter filter = new FileNameExtensionFilter("xml-File", "xml");
        //fileChooser.addChoosableFileFilter(filter); //not necessary
        fileChooser.setFileFilter(filter);
        
        //set initial path
        fileChooser.setCurrentDirectory(new File(".\\xml"));
        
        //result?
        int res = fileChooser.showOpenDialog(null);//(parent);
        
        if (res == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile().toString();            
        }
        else
        {
            return null;
        }
    }
    
    public static void main(String[] args)
    {
        Xml2YnabCsvGui gui = new Xml2YnabCsvGui();
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if (ae.getSource() == btnSelectFile)
        {
            //choose file
            xmlFileName = selectXmlFile();
            txtFileName.setText(xmlFileName);
        }
        else if(ae.getSource() == btnStart)
        {
            //start conversion
            startConversion();
        }
        else if(ae.getSource() == btnQuit)
        {
            //quit
            System.exit(0);
        }
    }
}

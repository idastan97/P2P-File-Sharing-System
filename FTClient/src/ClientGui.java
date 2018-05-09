import ftserver.FileInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class ClientGui extends JFrame implements ActionListener {


    private JButton search;  //Buttons
    private JButton dload;
    private JButton close;

    private Commands commands;

    private JList jl;   // List that will show found files
    private JLabel label; //Label "File Name

    public static JLabel errorLog;

    private JTextField searchField; // Two textfields: one is for typing a file name, the other is just to show the selected file
    DefaultListModel listModel; // Used to select items in the list of found files

    String str[]={"Info1","Info2","Info3","Info4","Info5"}; // Files information

    public ClientGui(Commands commands) {
        super("Example GUI");

        this.commands = commands;

        setLayout(null);
        setSize(700,600);

        label=new JLabel("File name:");
        label.setBounds(50,50, 80,20);
        add(label);

        errorLog = new JLabel("");
        errorLog.setBounds(130, 20, 220, 20);
        errorLog.setForeground(Color.red);
        add(errorLog);

        searchField = new JTextField();
        searchField.setBounds(130,50, 220,20);
        add(searchField);

        search = new JButton("Search");
        search.setBounds(360,50,80,20);
        search.addActionListener(this);
        add(search);

        listModel = new DefaultListModel();
        jl = new JList(listModel);

        JScrollPane listScroller = new JScrollPane(jl);
        listScroller.setBounds(50, 80,550,300);

        add(listScroller);

        dload=new JButton("Download");
        dload.setBounds(200,400,130,20);
        dload.addActionListener(this);
        add(dload);



        close=new JButton("Close");
        close.setBounds(360,470,80,20);
        close.addActionListener(this);
        add(close);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == search){
            String fileName = searchField.getText();
            try {
                List<FileInfo> resList =  commands.searchFile(fileName);
                listModel.clear();
                if(resList != null){
                    for(FileInfo fl: resList) {
                        listModel.addElement(fl);
                    }
                }
                //errorLog.setText("");
            }catch (Exception exc) {
                //errorLog.setText(exc.getMessage());
            }
        }
        else if(e.getSource() == dload){   //If download button is pressed get the selected value from the list and show it in text field
            try{
                commands.download((FileInfo) jl.getSelectedValue());
                //errorLog.setText("");
            }catch (Exception ex){
                //errorLog.setText(ex.getMessage());
            }
        }
        else if(e.getSource()==close){ //If close button is pressed exit
            commands.sendMessage("BYE");
            System.exit(0);
        }
    }
}

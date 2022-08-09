 package com.company;

 import javax.swing.*;
 import javax.swing.tree.DefaultTreeModel;
 import java.awt.*;
 import java.awt.event.ActionEvent;
 import java.awt.event.ActionListener;
 import java.awt.event.MouseEvent;
 import java.awt.event.MouseListener;
 import java.io.File;
 import java.io.IOException;
 import java.nio.file.Files;
 import java.nio.file.Paths;

 public class App extends JFrame {

    /*List of tools we are going to need on our frame*/
     JPanel topPanel, panel;
     JToolBar toolBar;
     JMenuBar menuBar, statusBar;
     JDesktopPane desktopPane;
     JComboBox comboBox;
     JTree tree;
     DefaultTreeModel treeModel;
     File copiedFile;
     JMenuItem rename, copy, delete, run, exit, paste;//Items going under fileMenu
     FileFrame ff;

    /*constructor to set the main features of the frame and declare the tools*/
    public App(){

        this.setTitle("File Manager");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        panel = new JPanel();
        topPanel = new JPanel();
        menuBar = new JMenuBar();
        toolBar = new JToolBar();
        statusBar = new JMenuBar();
        desktopPane = new JDesktopPane();
    }

    /*The launching method that organizes and links all our tools together*/
    public void go(){
        buildMenuBar();
        buildToolBar();
        buildStatusBar();

        panel.setLayout(new BorderLayout());
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(desktopPane, BorderLayout.CENTER);
        panel.add(statusBar, BorderLayout.SOUTH);
        topPanel.setLayout(new BorderLayout());
        topPanel.add(menuBar, BorderLayout.NORTH);
        topPanel.add(toolBar, BorderLayout.SOUTH);

        panel.addMouseListener(new MouseAction());

        ff = new FileFrame(comboBox.getSelectedItem().toString());
        desktopPane.add(ff);

        this.add(panel);

        this.setSize(1000, 700);

    }

    private void buildMenuBar() {
        JMenu fileMenu, treeMenu, windowMenu, helpMenu;//The menus that we are going to need on the menubar

        fileMenu = new JMenu("File");
        treeMenu = new JMenu("Tree");
        windowMenu = new JMenu("Window");
        helpMenu = new JMenu("Help");

        menuBar.add(fileMenu);
        menuBar.add(treeMenu);
        menuBar.add(windowMenu);
        menuBar.add(helpMenu);



        /* ******FILE MENU******* */
        rename = new JMenuItem("Rename");
        copy = new JMenuItem("Copy");
        delete = new JMenuItem("Delete");
        run = new JMenuItem("Run");
        exit = new JMenuItem("Exit");

        fileMenu.add(rename);
        fileMenu.add(copy);
        fileMenu.add(delete);
        fileMenu.add(run);
        fileMenu.add(exit);

        rename.addActionListener(new ButtonAction());
        copy.addActionListener(new ButtonAction());
        delete.addActionListener(new ButtonAction());
        run.addActionListener(new ButtonAction());
        exit.addActionListener(new ButtonAction());

        /* ******TREE MENU***** */
        JMenuItem expand, collapse;//Items going under treeMenu

        expand = new JMenuItem("Expand Branch");
        collapse = new JMenuItem("Collapse Branch");

        treeMenu.add(expand);
        treeMenu.add(collapse);

        expand.addActionListener(new ButtonAction());
        collapse.addActionListener(new ButtonAction());

        /* *******WINDOW MENU******* */
        JMenuItem newWindow, cascade;//Items going under windowMenu

        newWindow = new JMenuItem("New");
        cascade = new JMenuItem("Cascade");

        windowMenu.add(newWindow);
        windowMenu.add(cascade);

        newWindow.addActionListener(new ButtonAction());
        cascade.addActionListener(new ButtonAction());

        /* *******HELP MENU****** */
        JMenuItem help, about;//Items going under helpMenu

        help = new JMenuItem("Help");
        about = new JMenuItem("About");

        helpMenu.add(help);
        helpMenu.add(about);

        help.addActionListener(new ButtonAction());
        about.addActionListener(new ButtonAction());

    }

    private void buildToolBar(){
        /*items needed on the tool bar*/
        JButton details, simple;

        toolBar.setLayout(new FlowLayout());

        comboBox = new JComboBox();
        details = new JButton("Details");
        simple = new JButton("Simple");

        details.addActionListener(new ButtonAction());
        simple.addActionListener(new ButtonAction());

        /* Gets all drives and adds to combo box */
        File[] drives = DIRead.getAllDrives();
        for (File drive : drives) {

            comboBox.addItem(drive);

        }
        
        comboBox.setPreferredSize(new Dimension(75, 25));
        toolBar.add(comboBox);
        toolBar.add(details);
        toolBar.add(simple);

    }

    private void buildStatusBar(){
        JLabel currentDrive, freeSpace, usedSpace, totalSpace;

        currentDrive = new JLabel();
        freeSpace = new JLabel();
        usedSpace = new JLabel();
        totalSpace = new JLabel();

        statusBar.add(currentDrive);
        statusBar.add(freeSpace);
        statusBar.add(usedSpace);
        statusBar.add(totalSpace);


        File[] drives = DIRead.getAllDrives();
        File drive = drives[0];
        //drive = desktopPane.getSelectedFrame().getDrive();
        int fSpace = (int)(drive.getFreeSpace()/1024/1024/1024);
        int tSpace = (int)(drive.getTotalSpace()/1024/1024/1024);
        currentDrive.setText("Current Drive: " + drive.toString() + "   ");
        freeSpace.setText("Free Space: " + fSpace+ "GB   ");
        usedSpace.setText("Used Space: " + (tSpace - fSpace) + "GB   ");
        totalSpace.setText("Total Space: " + tSpace + "GB   ");

    }

    private class ButtonAction implements ActionListener {

         public void actionPerformed(ActionEvent e) {

             /* *******FILE MENU ACTIONS******* */
             if (e.getActionCommand().equals("Rename")){
                 RCDialog rcDialog = new RCDialog();
                 rcDialog.setTitle("Rename");
                 rcDialog.setVisible(true);
                 int confirm = rcDialog.getResponse();
                 if (confirm == 0) {
                     File file = new File(rcDialog.getInputText());
                     File newFile = new File(rcDialog.getText());
                     file.renameTo(newFile);
                 }
             }
             if (e.getActionCommand().equals("Copy")){
                 RCDialog rcDialog = new RCDialog();
                 rcDialog.setTitle("Copy");
                 rcDialog.setVisible(true);
                 int confirm = rcDialog.getResponse();
                 if (confirm == 0) {
                     try {
                         Files.copy(Paths.get(rcDialog.getInputText()), Paths.get(rcDialog.getText()));
                     } catch (IOException ioException) {
                         ioException.printStackTrace();
                     }
                 }
             }
             if (e.getActionCommand().equals("Delete")){
                 SimpleDialog simpleDialog = new SimpleDialog();
                 simpleDialog.setTitle("Copy");
                 simpleDialog.setVisible(true);
                 int confirm = simpleDialog.getResponse();
                 if (confirm == 0) {
                     File file = new File(simpleDialog.getText());
                     file.delete();
                 }
             }
             if (e.getActionCommand().equals("Run")){
                 /*
             }
                 Desktop desktop = Desktop.getDesktop();
                 try{
                        desktop.open(selectedFile());
                 }
                 catch (IOException ex){
                     System.out.println(ex.toString());

                  */
             }
             if (e.getActionCommand().equals("Exit"))
                 System.exit(0);
             if (e.getActionCommand().equals("Paste")){
                 //FileFrame.add(copiedFile)?
             }

             /* *******TREE MENU ACTIONS******* */
             //if (e.getActionCommand().equals("Expand Branch"))
             //if (e.getActionCommand().equals("Collapse Branch"))

             /* *******WINDOW MENU ACTIONS******* */
             if (e.getActionCommand().equals("New")){
                 FileFrame ff = new FileFrame(comboBox.getSelectedItem().toString());
                 ff.reshape(0, 100, ff.getWidth(), ff.getHeight());
                 desktopPane.add(ff);
             }
             if (e.getActionCommand().equals("Cascade")){
                 JInternalFrame [] frames = desktopPane.getAllFrames();
                 System.out.println(frames.length);
                 for (int i = 0; i < frames.length; i ++){
                     if (i == 0) {
                         frames[0].reshape(0,0, frames[0].getWidth(), frames[0].getHeight());
                     }
                     else if (! frames[i].isIcon()){
                         frames[i].reshape(frames[i-1].getX() + 30, frames[i-1].getY() + 28 , frames[i].getWidth(), frames[i].getHeight());
                         frames[i].moveToFront();
                     }
                 }
             }

             /* *******HELP MENU ACTIONS******* */
             if (e.getActionCommand().equals("Help")){
                 helpDialog helpDig = new helpDialog();
                 helpDig.setVisible(true);
             }
             if (e.getActionCommand().equals("About")){
                 aboutDialog about = new aboutDialog();
                 about.setVisible(true);
             }

             /* *******TOOLBAR ACTIONS***** */
             if (e.getActionCommand().equals("Details")) {
                 FileFrame ff = (FileFrame) desktopPane.getSelectedFrame();
                 ff.setNum(0);
                 repaint();
                 revalidate();
                 DIRead.readDirectory(comboBox.getSelectedItem().toString());

             }
             if (e.getActionCommand().equals("Simple")){
                 FileFrame ff = (FileFrame) desktopPane.getSelectedFrame();
                 ff.setNum(1);
                 repaint();
                 revalidate();
             }

         }
         
     }

     private class MouseAction implements MouseListener{

         @Override
         public void mouseClicked(MouseEvent e) {
             JPopupMenu popupMenu = new JPopupMenu();
             paste = new JMenuItem("Paste");
             paste.addActionListener(new ButtonAction());
             popupMenu.setVisible(true);
             popupMenu.add(rename);
             popupMenu.add(copy);
             popupMenu.add(paste);
             popupMenu.addSeparator();
             popupMenu.add(delete);
             if (e.getButton() == MouseEvent.BUTTON3){
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
             }
         }

         @Override
         public void mousePressed(MouseEvent e) {

         }

         @Override
         public void mouseReleased(MouseEvent e) {

         }

         @Override
         public void mouseEntered(MouseEvent e) {

         }

         @Override
         public void mouseExited(MouseEvent e) {

         }
     }

}//end class
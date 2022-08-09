package com.company;

import javax.swing.*;
import javax.swing.text.html.parser.Element;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FilePanel extends JPanel {

    private JList list = new JList();
    private DefaultListModel model = new DefaultListModel();
    private JScrollPane scrollPane = new JScrollPane();
    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private DecimalFormat df = new DecimalFormat("#, ###");
    private String path;
    FileFrame ff;

    JMenuItem rename, copy, delete;

    public FilePanel(FileFrame ff){
        this.ff = ff;
        this.setDropTarget(new MyDropTarget());
        scrollPane.setPreferredSize(new Dimension(500,500));
        list.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        list.setDragEnabled(true);
        list.setModel(model);
        list.addMouseListener(new MouseAction());
        add(list, BorderLayout.WEST);
        scrollPane.setViewportView(list);
        add(scrollPane);
    }

    public void fillList(File[] files) {
        model.clear();
        for (File file : files) {
            if (!file.isHidden() && file.isDirectory()) {
                String s = file.getName();
                model.addElement("\uD83D\uDCC1 " + s);
            }
        }
        for (File file : files) {
            if (!file.isHidden() && !file.isDirectory()) {
                String s = String.format("%-20s\u200E   %15s   %s", file.getName(), sdf.format(file.lastModified()), df.format(file.length()));
                model.addElement(s);
            }
        }
    }

    public void simpleList(File [] files){
        model.clear();
        for (File file : files) {
            if (!file.isHidden() && file.isDirectory()) {
                String s = file.getName();
                model.addElement("\uD83D\uDCC1 " + s);
            }
        }
        for (File file : files) {
            if (!file.isHidden() && !file.isDirectory()) {
                String s = file.getName();
                model.addElement(s);
            }
        }
    }

    public void setPath(String path) {
        this.path = path;
        ff.setTitle(path);
    }

    private class ButtonAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {

            int index = list.getSelectedIndex();
            if (index >= 0) {

                Object listItem = list.getModel().getElementAt(index);
                String fileName = listItem.toString().split("\u200E")[0];
                if (fileName.contains("\uD83D\uDCC1")) {
                    fileName = fileName.split("\uD83D\uDCC1")[1];
                }
                String filePath = path + "\\" + fileName;

                if (e.getActionCommand().equals("Rename")){
                    RCDialog rcDialog = new RCDialog();
                    rcDialog.setTitle("Rename");
                    rcDialog.setText(filePath);
                    rcDialog.setVisible(true);
                    int confirm = rcDialog.getResponse();
                    if (confirm == 0) {
                        String newFileName = rcDialog.getText();
                        File file = new File(filePath);
                        File newFile = new File(newFileName);
                        file.renameTo(newFile);
                        model.clear();
                        File[] files = DIRead.readDirectory(path);
                        fillList(files);
                    }
                }

                if (e.getActionCommand().equals("Copy")) {
                    RCDialog rcDialog = new RCDialog();
                    rcDialog.setTitle("Copy");
                    rcDialog.setText(filePath);
                    rcDialog.setVisible(true);
                    int confirm = rcDialog.getResponse();
                    if (confirm == 0) {
                        String newFilePath = rcDialog.getText();
                        try {
                            Files.copy(Paths.get(filePath.trim()), Paths.get(newFilePath));
                            model.clear();
                            File[] files = DIRead.readDirectory(path);
                            fillList(files);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }

                if (e.getActionCommand().equals("Delete")){
                    int confirm = JOptionPane.showConfirmDialog(null, "Delete " + filePath, "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirm == 0) {
                        File file = new File(filePath);
                        file.delete();
                        model.clear();
                        File[] files = DIRead.readDirectory(path);
                        fillList(files);
                    }
                }
            }

        }

    }

    private class MouseAction implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                int index = list.getSelectedIndex();
                if (index >= 0) {
                    Object listItem = list.getModel().getElementAt(index);
                    String fileName = listItem.toString().split("\u200E")[0];
                    if (fileName.contains("\uD83D\uDCC1")) {
                        fileName = fileName.split("\uD83D\uDCC1")[1];
                    }
                    String filePath = path + "\\" + fileName;
                    File file = new File(filePath);
                    if (!file.isDirectory()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            desktop.open(file);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                }
            }
            JPopupMenu popupMenu = new JPopupMenu();
            rename = new JMenuItem("Rename");
            copy = new JMenuItem("Copy");
            delete = new JMenuItem("Delete");
            rename.addActionListener(new ButtonAction());
            copy.addActionListener(new ButtonAction());
            delete.addActionListener(new ButtonAction());
            popupMenu.setVisible(true);
            popupMenu.add(rename);
            popupMenu.add(copy);
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

    /*************************************************************************
     * class MyDropTarget handles the dropping of files onto its owner
     * (whatever JList it is assigned to). As written, it can process two
     * types: strings and files (String, File). The String type is necessary
     * to process internal source drops from another FilePanel object. The
     * File type is necessary to process drops from external sources such
     * as Windows Explorer or IOS.
     *
     * Note: no code is provided that actually copies files to the target
     * directory. Also, you may need to adjust this code if your list model
     * is not the default model. JList assumes a toString operation is
     * defined for whatever class is used.
     */
    class MyDropTarget extends DropTarget {
        /**************************************************************************
         *
         * @param evt the event that caused this drop operation to be invoked
         */
        public void drop(DropTargetDropEvent evt){
            try {
                //types of events accepted
                evt.acceptDrop(DnDConstants.ACTION_COPY);
                //storage to hold the drop data for processing
                java.util.List result = new ArrayList();
                //what is being dropped? First, Strings are processed
                if(evt.getTransferable().isDataFlavorSupported(DataFlavor.stringFlavor)){
                    String temp = (String)evt.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    //String events are concatenated if more than one list item
                    //selected in the source. The strings are separated by
                    //newline characters. Use split to break the string into
                    //individual file names and store in String[]
                    String[] next = temp.split("\\n");
                    //add the strings to the listmodel
                    for(int i=0; i<next.length;i++)
                        model.addElement(next[i]);
                }
                else{ //then if not String, Files are assumed
                    result =(List)evt.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    //process the input
                    for(Object o : result){
//                        System.out.println(o.toString());
//                        model.addElement(o.toString());
                        File file = new File(o.toString());
                        Files.copy(Paths.get(o.toString()), Paths.get(path + "\\" + file.getName()));
                        String s = String.format("%-20s\u200E   %15s   %s", file.getName(), sdf.format(file.lastModified()), df.format(file.length()));
                        model.addElement(s);
                    }
                }
            }
            catch(Exception ex){
                ex.printStackTrace();
            }
        }

    }

}

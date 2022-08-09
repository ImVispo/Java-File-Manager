package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class RCDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextPane fromPane;
    private JTextPane toPane;
    private JLabel To;
    private JLabel From;
    private JLabel Dir;
    private int ok;

    public RCDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setSize(350,200);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        ok = 0;
        dispose();
    }

    private void onCancel() {
        ok = 1;
        dispose();
    }

    public void setText(String s){
        fromPane.setText(s);
    }

    public String getInputText() { return fromPane.getText(); };

    public String getText(){
        return toPane.getText();
    }

    public int getResponse(){return ok;}

    public void setLabel(String s){
        Dir.setText(Dir.getText() + s);
    }

    public static void main(String[] args) {
        RCDialog dialog = new RCDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }


}

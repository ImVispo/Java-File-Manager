package com.company;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class helpDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;

    public helpDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        this.setSize(500,300);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    public static void main(String[] args) {
        helpDialog dialog = new helpDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}

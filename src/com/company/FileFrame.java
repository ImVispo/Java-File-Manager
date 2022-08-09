package com.company;

import javax.swing.*;

public class FileFrame extends JInternalFrame {

    JSplitPane splitPane;
    DirPanel dirPanel;

        public FileFrame(String path){
            dirPanel = new DirPanel(path);
            FilePanel filePanel = new FilePanel(this);
            dirPanel.setFilePanel(filePanel);
            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, dirPanel, filePanel);
            this.setTitle(path);
            this.getContentPane().add(splitPane);
            this.setVisible(true);
            this.setSize(750, 550);
            this.setMaximizable(true);
            this.setIconifiable(true);
            this.setClosable(true);
        }

        public void setNum(int num){
            dirPanel.setSimple(num);
        }


}

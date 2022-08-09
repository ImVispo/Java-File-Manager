package com.company;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;

public class DirPanel extends JPanel {

    private FilePanel filePanel;
    private JScrollPane scrollPane = new JScrollPane();
    private JTree tree;
    private DefaultTreeModel treeModel;
    public int simple = 0;

    public void setFilePanel(FilePanel fp) {
        filePanel = fp;
    }

    public DirPanel(String path){
        scrollPane.setPreferredSize(new Dimension(200,500));
        tree = new JTree();
        tree.addTreeSelectionListener(new MyTreeSelectionListener());
        buildTree(path);
        scrollPane.setViewportView(tree);
        add(scrollPane);
    }

    public void buildTree(String path) {
        File[] files = DIRead.readDirectory(path);
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(path);
        treeModel = new DefaultTreeModel(root);
        for (File file : files) {
            if (!file.isHidden()) {
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(file.getName());
                root.add(node);
                if (file.isDirectory() && !file.isHidden() && Files.isReadable(file.toPath())) {
                    File[] subFiles = DIRead.readDirectory(file.getAbsolutePath());
                    for (File subFile : subFiles) {
                        DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(subFile.getName());
                        node.add(subNode);
                    }
                }
            }
        }
        tree.setModel(treeModel);
    }

    class MyTreeSelectionListener implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            TreePath path = e.getPath();
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
            node.removeAllChildren();
            String strPath = path.toString();
            String[] strPathArr = strPath.substring(1, strPath.length() - 1).split(", ");
            strPath = String.join("\\", strPathArr);
            File[] files = DIRead.readDirectory(strPath);
            if (files != null) {
                for (File file : files) {
                    if (!file.isHidden()) {
                        DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(file.getName());
                        node.add(subNode);
                        if (file.isDirectory() && !file.isHidden() && Files.isReadable(file.toPath())) {
                            File[] subFiles = DIRead.readDirectory(file.getAbsolutePath());
                            for (File subFile : subFiles) {
                                DefaultMutableTreeNode subNodeTwo = new DefaultMutableTreeNode(subFile.getName());
                                subNode.add(subNodeTwo);
                            }
                        }
                    }
                }
                filePanel.setPath(strPath);
                if (simple == 0)
                    filePanel.fillList(files);
                else
                    filePanel.simpleList(files);
            }
         }
    }

    public void setSimple(int num){
        simple = num;
    }
}

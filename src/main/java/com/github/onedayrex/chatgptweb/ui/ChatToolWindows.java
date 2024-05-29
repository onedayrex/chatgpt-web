package com.github.onedayrex.chatgptweb.ui;

import com.intellij.ui.components.JBTextArea;

import javax.swing.*;
import java.awt.*;

public class ChatToolWindows extends JFrame {
    private JScrollPane jScrollPane;
    private JPanel panel;
    private JTextArea questionText;

    public JScrollPane getJScrollPane() {
        return jScrollPane;
    }

    public JPanel getPanel() {
        return panel;
    }

    public JTextArea getQuestionText() {
        return questionText;
    }

    public ChatToolWindows() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        jScrollPane = new JScrollPane();
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        questionText = new JTextArea();
        questionText.setBorder(BorderFactory.createTitledBorder("Input"));
        textPanel.add(questionText, BorderLayout.CENTER);

        panel.add(jScrollPane, BorderLayout.CENTER);
        panel.add(textPanel, BorderLayout.SOUTH);
    }
}

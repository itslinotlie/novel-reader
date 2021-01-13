package main;

import objects.Novel;
import tools.Design;
import tools.TextAreaLimit;
import tools.WebScraping;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class DisplayNovel implements MouseListener {
    private JFrame frame; //testing purposes, delete once more GUI is complete
    private JPanel content = new JPanel();

    private Novel novel;

    private JButton back, next, go;

    private JTextArea text, skip;
    private JScrollPane scroll;

//    static DisplayNovel x;

    public DisplayNovel(JFrame frame, Novel novel) {
        this.frame = frame;
        this.novel = novel;
        setupPanel();
        setupContent();
        setupFrame(); //testing purposes, delete once more GUI is complete
        refreshScreen();
    }
    //testing purposes, delete once more GUI is complete
    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Design.WIDTH, Design.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    //display novel text to reader in JScrollPane
    //to allow for continuous scrolling
    private void setupContent() {
        //JTextArea to display chapter content
        text = new JTextArea(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        text.setBackground(Design.novelBackground);
        text.setForeground(Design.novelText);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setFont(Design.novelTextFont);
        content.add(text);

        //JScrollPane to allow for continuous scrolling
        scroll = new JScrollPane(text);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        scroll.setBorder(BorderFactory.createCompoundBorder( //compound borders are sick
                BorderFactory.createLineBorder(Design.novelButtonBackground, 2, true),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createRaisedBevelBorder(),
                        BorderFactory.createLoweredBevelBorder()
                )
        ));
        content.add(scroll);
    }
    //sets up the panels (header, footer, side margins, etc.)
    private void setupPanel() {
        //panel to display everything
        content.setBackground(Color.GREEN);
        content.setBounds(0, 0, Design.WIDTH, Design.HEIGHT);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //text area to skip to a chapter
        skip = new JTextArea();
        skip.setBackground(Design.novelButtonBackground);
        skip.setForeground(Design.novelButtonForeground);
        skip.setFont(Design.buttonTextFont);
        skip.setDocument(new TextAreaLimit());
        skip.setText("1234");
        skip.setBounds(200, 30, 60, 40);

        //navigation buttons
        go = new JButton("GO");
        go.setFont(Design.buttonTextFont);
        go.setBounds(275, 30, 100, 40);
        go.setBackground(Design.novelButtonBackground);
        go.setForeground(Design.novelButtonForeground);
        go.addMouseListener(this);
        go.addActionListener(e -> { //logic to check if you can skip to a chapter
            skip.setText(skip.getText().trim());
            int targetChapter = WebScraping.isInteger(skip.getText())? Integer.parseInt(skip.getText()):-1;
            if(targetChapter<novel.getChapterRange()[0] || novel.getChapterRange()[1]<targetChapter) {
                JOptionPane.showMessageDialog(content, skip.getText()+" is an invalid chapter");
            } else {
                novel.setLastReadChapter(targetChapter);
                refreshScreen();
            }
        });

        back = new JButton("< Back");
        back.setFont(Design.buttonTextFont);
        back.setBackground(Design.novelButtonBackground);
        back.setForeground(Design.novelButtonForeground);
        back.setBounds(125, 30, 150, 40);
        back.addMouseListener(this);
        back.addActionListener(e -> {
            novel.setLastReadChapter(novel.getLastReadChapter()-1);
            refreshScreen();
        });

        next = new JButton("Next >");
        next.setFont(Design.buttonTextFont);
        next.setBackground(Design.novelButtonBackground);
        next.setForeground(Design.novelButtonForeground);
        next.setBounds(325, 30, 150, 40);
        next.addMouseListener(this);
        next.addActionListener(e -> {
            novel.setLastReadChapter(novel.getLastReadChapter()+1);
            refreshScreen();
        });

        //header
        JPanel top = new JPanel();
        top.setBackground(Design.novelBackground);
        top.setPreferredSize(new Dimension(Design.WIDTH, 100));
        top.setLayout(null);
        top.add(skip);
        top.add(go);

        //footer
        JPanel bot = new JPanel();
        bot.setBackground(Design.novelBackground);
        bot.setPreferredSize(new Dimension(Design.WIDTH, 100));
        bot.setLayout(null);
        bot.add(next);
        bot.add(back);

        //side margins (left and right)
        JPanel left = new JPanel();
        left.setBackground(Design.novelBackground);
        left.setPreferredSize(new Dimension(10, Design.HEIGHT));

        JPanel right = new JPanel();
        right.setBackground(Design.novelBackground);
        right.setPreferredSize(new Dimension(10, Design.HEIGHT));

        content.add(top, BorderLayout.NORTH);
        content.add(bot, BorderLayout.SOUTH);
        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.EAST);
    }

    public void refreshScreen() {
        //setting JFrame title
        frame.setTitle(WebScraping.getChapterName(novel, novel.getLastReadChapter()).equals(WebScraping.errorMessage)?
                "Invalid Chapter":WebScraping.getChapterName(novel, novel.getLastReadChapter()));
        //changing display text
        text.setText(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        //brining JScrollPane to the top
        text.setSelectionStart(0);
        text.setSelectionEnd(0);
        //checking if back/next buttons will work
        back.setEnabled(novel.getLastReadChapter()!=novel.getChapterRange()[0]);
        next.setEnabled(novel.getLastReadChapter()!=novel.getChapterRange()[1]);
    }

    public static void main(String[] args) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);
        System.out.println(novel);

        new DisplayNovel(new JFrame(), novel);
//        x.refreshScreen();
    }

    public JPanel getPanel() {
        return content;
    }

    //button hover styling
    @Override
    public void mouseEntered(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
        button.setBackground(Design.novelButtonBackgroundDark);
    }
    @Override
    public void mouseExited(MouseEvent e) {
        JButton button = (JButton) e.getSource();
        button.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
        button.setBackground(Design.novelButtonBackground);
    }
    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}
}

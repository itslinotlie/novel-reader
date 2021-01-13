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

public class LightNovelTest extends JFrame /*JPanel*/ implements MouseListener {
    private int WIDTH = 600, HEIGHT = 800;
    private Novel novel;

    private JButton back, next, go;

    private JTextArea text, skip;
    private JScrollPane scroll;

//    private JFrame frame = new JFrame();

    static LightNovelTest x;

    public LightNovelTest(Novel novel) {
        this.novel = novel;
        setupPanel();
        setupLabel();
        setupFrame();
    }
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setBounds(0, 0, WIDTH, HEIGHT);
        setVisible(true);
        setLayout(new BorderLayout());

//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(WIDTH, HEIGHT);
//        frame.setResizable(false);
//        frame.setVisible(true);
//        frame.add(this);
    }
    private void setupPanel() {
        //navigation buttons
        skip = new JTextArea();
        skip.setBackground(Design.novelButtonBackground);
        skip.setForeground(Design.novelButtonForeground);
        skip.setFont(Design.buttonTextFont);
        skip.setDocument(new TextAreaLimit());
        skip.setText("1234");
        skip.setBounds(200, 30, 60, 40);

        go = new JButton("GO");
        go.setFont(Design.buttonTextFont);
        go.setBounds(275, 30, 100, 40);
        go.setBackground(Design.novelButtonBackground);
        go.setForeground(Design.novelButtonForeground);
        go.addMouseListener(this);
        go.addActionListener(e -> {
            skip.setText(skip.getText().trim());
            int targetChapter = WebScraping.isInteger(skip.getText())? Integer.parseInt(skip.getText()):-1;
            if(targetChapter<novel.getChapterRange()[0] || novel.getChapterRange()[1]<targetChapter) {
                JOptionPane.showMessageDialog(this, skip.getText()+"is an invalid chapter");
            } else {
                System.out.println(WebScraping.getChapterUrl(novel, novel.getLastReadChapter()));
                novel.setLastReadChapter(targetChapter);
                x.refreshScreen();
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
            x.refreshScreen();
        });

        next = new JButton("Next >");
        next.setFont(Design.buttonTextFont);
        next.setBackground(Design.novelButtonBackground);
        next.setForeground(Design.novelButtonForeground);
        next.setBounds(325, 30, 150, 40);
        next.addMouseListener(this);
        next.addActionListener(e -> {
            novel.setLastReadChapter(novel.getLastReadChapter()+1);
            x.refreshScreen();
        });

        //header
        JPanel top = new JPanel();
        top.setBackground(Design.novelBackground);
        top.setPreferredSize(new Dimension(WIDTH, 100));
        top.setLayout(null);
        top.add(skip);
        top.add(go);

        //footer
        JPanel bot = new JPanel();
        bot.setBackground(Design.novelBackground);
        bot.setPreferredSize(new Dimension(WIDTH, 100));
        bot.setLayout(null);
        bot.add(next);
        bot.add(back);

        //side margins (left and right)
        JPanel left = new JPanel();
        left.setBackground(Design.novelBackground);
        left.setPreferredSize(new Dimension(10, HEIGHT));

        JPanel right = new JPanel();
        right.setBackground(Design.novelBackground);
        right.setPreferredSize(new Dimension(10, HEIGHT));

        add(top, BorderLayout.NORTH);
        add(bot, BorderLayout.SOUTH);
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.EAST);
    }
    private void setupLabel() {
        //JTextArea to display chapter content
        text = new JTextArea(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        text.setBackground(Design.novelBackground);
        text.setForeground(Design.novelText);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setFont(Design.novelTextFont);
        add(text);

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
        add(scroll);
    }
    public void refreshScreen() {
        //setting JFrame title
        /*frame.*/setTitle(WebScraping.getChapterName(novel, novel.getLastReadChapter()).equals(WebScraping.errorMessage)?
                "Invalid Chapter":WebScraping.getChapterName(novel, novel.getLastReadChapter()));
        //changing display text
        text.setText(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        //brining JScrollPane to the top
        text.setSelectionStart(0);
        text.setSelectionEnd(0);
        //checking if back/next buttons will work
        back.setEnabled(novel.getLastReadChapter()!=novel.getChapterRange()[0]);
        next.setEnabled(novel.getLastReadChapter()!=novel.getChapterRange()[1]);
        repaint();
    }

    public static void main(String[] args) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);
        System.out.println(novel);

        x = new LightNovelTest(novel);
        x.refreshScreen();

//        Scanner in  = new Scanner(System.in);
//        int x = in.nextInt();
//        while(x!=-1) {
//            if(x==1) x.set
//        }
    }


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

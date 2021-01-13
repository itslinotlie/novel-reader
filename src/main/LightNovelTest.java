package main;

import objects.Novel;
import tools.Design;
import tools.WebScraping;

import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

import javax.swing.*;
import javax.swing.border.BevelBorder;

public class LightNovelTest extends JFrame implements MouseListener {
    private int WIDTH = 600, HEIGHT = 800;
    private Novel novel;

    private JButton back, next;

    private JTextArea text;
    private JScrollPane scroll;

    static LightNovelTest x;

    public LightNovelTest(Novel novel) {
        this.novel = novel;
        setupPanel();
        setupLabel();
        setupListeners();
        setupFrame();
    }
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setVisible(true);
        setLayout(new BorderLayout());
    }
    private void setupPanel() {
        //navigation buttons
        back = new JButton("< Back");
        back.setFont(Design.buttonTextFont);
        back.setBackground(Design.novelButtonBackground);
        back.setForeground(Design.novelButtonForeground);
        back.setBounds(125, 40, 100, 40);
        back.addMouseListener(this);
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novel.setLastReadChapter(novel.getLastReadChapter()-1);
                x.refreshScreen();
            }
        });

        next = new JButton("Next >");
        next.setFont(Design.buttonTextFont);
        next.setBackground(Design.novelButtonBackground);
        next.setForeground(Design.novelButtonForeground);
        next.setBounds(325, 40, 100, 40);
        next.addMouseListener(this);
        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                novel.setLastReadChapter(novel.getLastReadChapter()+1);
                x.refreshScreen();
            }
        });

        //header
        JPanel top = new JPanel();
        top.setBackground(Design.novelBackground);
        top.setPreferredSize(new Dimension(WIDTH, 100));
        top.setLayout(null);

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
        text = new JTextArea(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        text.setBackground(Design.novelBackground);
        text.setForeground(Design.novelText);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setFont(Design.novelTextFont);
//        text.setOpaque(false);
        add(text);

        scroll = new JScrollPane(text);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        add(scroll);
    }
    public void refreshScreen() {
        //setting JFrame title
        setTitle(WebScraping.getChapterName(novel, novel.getLastReadChapter()));
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

//        for(int i=1;i<=novel.getChapterRange()[1];i++) {
//            System.out.println(WebScraping.getChapterName(novel, novel.getLastReadChapter()+1));
//            novel.setLastReadChapter(i);
//
//            x.refreshScreen();
////            try {
////                Thread.sleep(1);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }
        Scanner in = new Scanner(System.in);
        int chapterCount = 1;
        while(chapterCount!=-1) {
            System.out.println(WebScraping.getChapterUrl(novel, novel.getLastReadChapter()));
            chapterCount = in.nextInt();
            novel.setLastReadChapter(chapterCount);
            x.refreshScreen();
        }
    }
    private void setupListeners() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent key) {
                if(key.getKeyChar()=='d') {
                    novel.setLastReadChapter(novel.getLastReadChapter()+1);
                    x.refreshScreen();
                } else if(key.getKeyChar()=='a') {
                    novel.setLastReadChapter(novel.getLastReadChapter()-1);
                    x.refreshScreen();
                }
            }
            @Override
            public void keyTyped(KeyEvent key) {}
            @Override
            public void keyReleased(KeyEvent key) {}
        });
    }

    @Override
    public void mouseClicked(MouseEvent e) {}
    @Override
    public void mousePressed(MouseEvent e) {}
    @Override
    public void mouseReleased(MouseEvent e) {}

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
}

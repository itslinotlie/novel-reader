package main;

import objects.Novel;
import tools.Design;
import tools.WebScraping;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

public class LightNovelTest extends JFrame {
    private int WIDTH = 600, HEIGHT = 800;
    private Novel novel;

    private JTextArea text;
    private JScrollPane scroll;

    static LightNovelTest x;

    public LightNovelTest(Novel novel) {
        this.novel = novel;
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
    private void setupLabel() {
        JPanel bot = new JPanel();
        bot.setBackground(Color.RED);
        bot.setPreferredSize(new Dimension(WIDTH, 100));
        add(bot, BorderLayout.SOUTH);

        JPanel top = new JPanel();
        top.setBackground(Color.BLACK);
        top.setPreferredSize(new Dimension(WIDTH, 100));
        add(top, BorderLayout.NORTH);

        JPanel left = new JPanel();
        left.setBackground(Color.BLUE);
        left.setPreferredSize(new Dimension(10, HEIGHT));
        add(left, BorderLayout.WEST);

        JPanel right = new JPanel();
        right.setBackground(Color.cyan);
        right.setPreferredSize(new Dimension(10, HEIGHT));
        add(right, BorderLayout.EAST);


        text = new JTextArea(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        text.setBackground(Color.GREEN);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setFont(Design.novelTextFont);
//        text.setOpaque(false);
        add(text);

        scroll = new JScrollPane(text);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        add(scroll);
    }
    public void refreshScreen() {
        text.setText(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        text.setSelectionStart(0);
        text.setSelectionEnd(0);
        repaint();
    }

    public static void main(String[] args) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);
        System.out.println(novel);

        x = new LightNovelTest(novel);

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
            System.out.println(WebScraping.getChapterName(novel, novel.getLastReadChapter()));
            chapterCount = in.nextInt();
            novel.setLastReadChapter(chapterCount);
            x.refreshScreen();
        }
    }
//    public void getFont() {
//        try {
//            Font f = Font.createFont(Font.TRUETYPE_FONT, new File("Raleway-Light.ttf")).deriveFont(12f);
//            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
//            ge.registerFont(f);
////            InputStream is = new FileInputStream("Raleway-Light.ttf");
////            Font f = Font.createFont(Font.TRUETYPE_FONT, is);
////            return f;
//        } catch (Exception e) {
//
////            return null;
//        }
//
//    }
}

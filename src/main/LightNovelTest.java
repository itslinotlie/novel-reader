package main;

import objects.Novel;
import tools.WebScraping;

import java.awt.*;
import java.util.Arrays;
import java.util.Scanner;

import javax.swing.*;

public class LightNovelTest extends JFrame {
    static String information = "";
    static String summary = "";
    static String description = "";
    static int WIDTH=600, HEIGHT = 800;
    static int maxChap = 0, minChap = 0x3f3f3f3f;
    public LightNovelTest() {
        setupLabel();
        setupFrame();
    }
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        setLayout(new BorderLayout());
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


        JTextArea text = new JTextArea(information);
        text.setBackground(Color.GREEN);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
//        text.setOpaque(false);
        add(text);

        JScrollPane scroll = new JScrollPane(text);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        add(scroll);
    }

    public static void main(String[] args) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);

        Scanner in = new Scanner(System.in);
        int x = in.nextInt();
        while(x!=-1) {
            System.out.println(novel);
            System.out.println(WebScraping.getChapterName(novel, x));
            System.out.println(WebScraping.getChapterContent(novel, x));
            x = in.nextInt();
        }
//        new LightNovelTest();
    }
}

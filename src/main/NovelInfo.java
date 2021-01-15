package main;

import objects.Novel;
import tools.ButtonStyle;
import tools.Design;
import tools.TextAreaLimit;
import tools.WebScraping;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.*;

public class NovelInfo {

    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot;
    private Novel novel;

    private JLabel thumbnail;

    private NovelDisplay novelDisplay;

    public static void main(String args[]) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);
        System.out.println(novel);

        JFrame frame = new JFrame();

        new NovelInfo(frame, novel);
    }

    public NovelInfo(JFrame frame, Novel novel) {
        this.frame = frame;
        this.novel = novel;
        setupPanel();
        setupContent();
        setupFrame();
    }

    //testing purposes, delete once more GUI is complete
    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Design.WIDTH, Design.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle(String.format("Viewing %s", novel.getNovelName()));
    }

    private void setupContent() {
        //novel information
        thumbnail = new JLabel();
        thumbnail.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(
                novel.getThumbnailWidth()/2, novel.getThumbnailHeight()/2, 0)));
        thumbnail.setBounds(50, 50, novel.getThumbnailWidth()/2, novel.getThumbnailHeight()/2);
        thumbnail.setBorder(BorderFactory.createLineBorder(Design.screenPop, 4));
        top.add(thumbnail);

        //novel title
        JLabel title = new JLabel(novel.getNovelName());
        title.setForeground(Design.foreground);
        title.setFont(Design.buttonTextFont.deriveFont(28f));
        title.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        title.setBounds(200, 50, 250, 50);
        top.add(title);

        //novel author
        JLabel author = new JLabel(novel.getAuthor());
        author.setForeground(Design.foreground);
        author.setFont(Design.buttonTextFont.deriveFont(18f));
        author.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        author.setBounds(200, 100, 200, 30);
        top.add(author);

        //novel summary in JScrollPane
        JLabel summary = new JLabel("Novel Summary");
        summary.setForeground(Design.foreground);
        summary.setFont(Design.buttonTextFont.deriveFont(24f));
        summary.setBorder(BorderFactory.createLineBorder(Color.WHITE));
        summary.setBounds(200, 200, 200, 50);
        top.add(summary);

        //JTextArea to display novel summary
        JTextArea text = new JTextArea(novel.getSummary());
        text.setBackground(Design.screenLightBackground);
        text.setForeground(Design.foreground);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setFont(Design.novelTextFont);
        center.add(text);

        //JScrollPane to allow for continuous scrolling of summary
        JScrollPane scroll = new JScrollPane(text);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(5);
        scroll.setBounds(50, 25, 500, 150);
        center.add(scroll);

        //chapter list
        JLabel chapterList = new JLabel(String.format("Chapter List (Total of %d)", novel.getChapterRange()[1]));
        chapterList.setForeground(Design.foreground);
        chapterList.setFont(Design.buttonTextFont.deriveFont(24f));
        chapterList.setBounds(50, 10, 350, 50);
        chapterList.setBorder(BorderFactory.createLineBorder(Color.white));
        bot.add(chapterList);

        //displaying chapters
        JButton chapter[] = new JButton[7];
        for(int i=1;i<=6;i++) {
            chapter[i] = new JButton();
            chapter[i].setText(WebScraping.getChapterName(novel, i<=3? i:novel.getChapterRange()[1]+i-6));
            chapter[i].setForeground(Design.screenBackground);
            chapter[i].setBackground(Design.novelButtonBackground);
            chapter[i].setFont(Design.buttonTextFont.deriveFont(12f));
            chapter[i].setBounds(50, 35+40*i, 300, 30);
            chapter[i].setBorder(BorderFactory.createLineBorder(Color.WHITE));
            chapter[i].addMouseListener(new ButtonStyle());
            int finalI = i;
            chapter[i].addActionListener(e -> refreshScreen(finalI<=3? finalI:novel.getChapterRange()[1]+finalI-6));
            bot.add(chapter[i]);
        }

        //resume to last read chapter
        JButton resume = new JButton("Resume");
        resume.setFont(Design.buttonTextFont.deriveFont(24f));
        resume.setBounds(400, 250, 150, 50);
        resume.setForeground(Design.foreground);
        resume.setBackground(Design.novelButtonBackground);
        resume.addMouseListener(new ButtonStyle());
        resume.addActionListener(e -> refreshScreen(novel.getLastReadChapter()));
        bot.add(resume);
    }

    private void setupPanel() {
        //panel to display everything
        content.setBackground(Color.GREEN);
        content.setBounds(0, 0, Design.WIDTH, Design.HEIGHT);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //novel information
        top = new JPanel();
        top.setBackground(Design.screenBackground);
        top.setPreferredSize(new Dimension(Design.WIDTH, 250));
        top.setLayout(null);

        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setLayout(null);

        bot = new JPanel();
        bot.setBackground(Design.screenBackground);
        bot.setPreferredSize(new Dimension(Design.WIDTH, 325));
        bot.setLayout(null);

        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(bot, BorderLayout.SOUTH);
    }

    private void refreshScreen(int targetChapter) {
        novel.setLastReadChapter(targetChapter);
        content.setVisible(false);
        novelDisplay = new NovelDisplay(frame, content, novel);
    }
}

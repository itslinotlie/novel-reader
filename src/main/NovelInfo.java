package main;

import objects.Novel;
import tools.ButtonStyle;
import tools.Design;
import tools.WebScraping;

import java.awt.*;
import javax.swing.*;

public class NovelInfo {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot;

    private Novel novel;
    private NovelDisplay novelDisplay;

    private boolean firstOpen = true;

    public static void main(String args[]) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[0]);
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
        frame.setTitle(String.format("Current novel: %s", novel.getNovelName()));
    }

    private void setupContent() {
        //novel information
        JLabel thumbnail = new JLabel();
        thumbnail.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(
                novel.getThumbnailWidth()/2, novel.getThumbnailHeight()/2, 0)));
        thumbnail.setBounds(50, 50, novel.getThumbnailWidth()/2, novel.getThumbnailHeight()/2);
        thumbnail.setBorder(BorderFactory.createLineBorder(Design.screenPop, 4));
        top.add(thumbnail);

        //novel title
        JLabel title = new JLabel(novel.getNovelName());
        title.setForeground(Design.foreground);
        title.setFont(Design.buttonTextFont.deriveFont(28f));
        title.setBounds(200, 50, 250, 50);
        top.add(title);

        //novel author
        JLabel author = new JLabel(novel.getAuthor());
        author.setForeground(Design.foreground);
        author.setFont(Design.buttonTextFont.deriveFont(18f));
        author.setBounds(200, 100, 200, 30);
        top.add(author);

        //novel summary in JScrollPane
        JLabel summary = new JLabel("Novel Summary");
        summary.setForeground(Design.foreground);
        summary.setFont(Design.buttonTextFont.deriveFont(24f));
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
        chapterList.setBounds(50, 0, 350, 50);
        bot.add(chapterList);

        //displaying chapters
        JButton chapter[] = new JButton[7];
        for(int i=1;i<=6;i++) {
            chapter[i] = new JButton();
            chapter[i].setText(WebScraping.getChapterName(novel, i<=3? i:novel.getChapterRange()[1]+i-6));
            chapter[i].setForeground(Design.screenBackground);
            chapter[i].setBackground(Design.novelButtonBackground);
            chapter[i].setFont(Design.buttonTextFont.deriveFont(12f));
            chapter[i].setBounds(50, 10+40*i+(i<=3? 0:30), 300, 30);
            chapter[i].addMouseListener(new ButtonStyle());
            int finalI = i;
            chapter[i].addActionListener(e -> refreshScreen(finalI<=3? finalI:novel.getChapterRange()[1]+finalI-6));
            bot.add(chapter[i]);
        }

        //gap between first and last 3 chapters
        JLabel dots = new JLabel(". . .");
        dots.setForeground(Design.foreground);
        dots.setFont(Design.buttonTextFont.deriveFont(24f));
        dots.setBounds(185, 10+150, 50, 30);
        bot.add(dots);

        //resume to last read chapter
        JButton resume = new JButton("Resume");
        resume.setFont(Design.buttonTextFont.deriveFont(24f));
        resume.setBounds(400, 250, 150, 50);
        resume.setForeground(Design.screenBackground);
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

        //novel summary
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setLayout(null);

        //novel chapter list
        bot = new JPanel();
        bot.setBackground(Design.screenBackground);
        bot.setPreferredSize(new Dimension(Design.WIDTH, 325));
        bot.setLayout(null);

        //panel containing everything
        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(bot, BorderLayout.SOUTH);
    }

    private void refreshScreen(int targetChapter) {
        //rather than creating a new instance of novelDisplay everytime a button click
        //having a boolean flag will allow only one instance per novel
        if(firstOpen) {
            novelDisplay = new NovelDisplay(frame, content, novel);
            firstOpen = false;
        } else {
            content.setVisible(false);
            novelDisplay.getPanel().setVisible(true);
        }
        content.setVisible(false);
        novel.setLastReadChapter(targetChapter);
        novelDisplay.refreshScreen();
    }
}

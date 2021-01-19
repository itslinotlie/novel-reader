package gui;

import objects.Bookshelf;
import objects.Novel;
import tools.ButtonStyle;
import tools.Design;
import tools.Misc;
import tools.WebScraping;

import java.awt.*;
import javax.swing.*;

public class NovelInfo {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot, browse;
    private Bookshelf bookshelf;

    private Novel novel;
    private NovelDisplay novelDisplay;

    private boolean firstOpen = true;

    public NovelInfo(JFrame frame, JPanel browse, Novel novel, Bookshelf bookshelf) {
        this.frame = frame;
        this.browse = browse;
        this.novel = novel;
        this.bookshelf = bookshelf;
        setupPanel();
        setupContent();
        setupFrame();
    }

    //testing purposes, delete once more GUI is complete
    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle(String.format("Current novel: %s", novel.getNovelName()));
    }

    private void setupContent() {
        //novel information
        JLabel thumbnail = new JLabel();
        double scaleFactor = 1/2f;
        int novelWidth = (int)(novel.getThumbnailWidth()*scaleFactor);
        int novelHeight = (int)(novel.getThumbnailHeight()*scaleFactor);
        int thickness = 4;
        thumbnail.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(novelWidth, novelHeight, 0)));
        thumbnail.setBounds(50, 50, novelWidth+2*thickness, novelHeight+2*thickness);
        thumbnail.setBorder(BorderFactory.createLineBorder(Design.screenPop, thickness));
        top.add(thumbnail);

        //novel title
        JLabel title = new JLabel("<html>"+novel.getNovelName()+"</html>");
        title.setForeground(Design.foreground);
        title.setFont(Design.buttonTextFont.deriveFont(24f));
        title.setBounds(200, 50, 350, 75);
        title.setBorder(BorderFactory.createLineBorder(Color.white));
        top.add(title);

        //novel author
        JLabel author = new JLabel("<html>"+novel.getAuthor()+"</html>");
        author.setForeground(Design.foreground);
        author.setFont(Design.buttonTextFont.deriveFont(16f));
        author.setBounds(200, 150, 350, 50);
        author.setBorder(BorderFactory.createLineBorder(Color.white));
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

        //closes the current novel and displays novelInfo screen
        JButton goBack = new JButton("Go Back");
        goBack.setFont(Design.buttonTextFont.deriveFont(16f));
        goBack.setBounds(10, 10, 120, 30);
        goBack.setBackground(Design.novelButtonBackground);
        goBack.setForeground(Design.foreground);
        goBack.addMouseListener(new ButtonStyle());
        goBack.addActionListener(e -> {
            content.setVisible(false);
            browse.setVisible(true);
            frame.setTitle(String.format("Currently browsing titles"));
        });
        top.add(goBack);
    }

    private void setupPanel() {
        //panel to display everything
        content.setBackground(Color.GREEN);
        content.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //novel information
        top = new JPanel();
        top.setBackground(Design.screenBackground);
        top.setPreferredSize(new Dimension(Misc.WIDTH, 250));
        top.setLayout(null);

        //novel summary
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setLayout(null);

        //novel chapter list
        bot = new JPanel();
        bot.setBackground(Design.screenBackground);
        bot.setPreferredSize(new Dimension(Misc.WIDTH, 325));
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

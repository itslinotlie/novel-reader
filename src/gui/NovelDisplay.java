package gui;

import objects.Novel;
import tools.*;

import java.awt.*;
import javax.swing.*;

/**
 * This creates the novel display screen, where users will be able to read the contents of a specified novel.
 * To navigate between chapters, users can either click the back/next button to traverse -/+ 1 chapter respectively.
 * Another option is to jump straight to a chapter by inputting a chapter # in the textbox and pressing go. Users
 * are able to read the contents of the novel by scrolling with the implementation of the JScrollPane, which allows
 * for "infinite scrolling". If the novel is bookmarked in the library, the last read chapter will be stored even
 * when the program is closed and reloaded when the program is relaunched.
 */
public class NovelDisplay {
    private JFrame frame;
    private JPanel content = new JPanel(), novelInfo;

    private JButton goBack, back, next, go;
    private JTextArea text, skip;
    private JScrollPane scroll;

    private Novel novel;

    //basic constructor
    public NovelDisplay(JFrame frame, JPanel novelInfo, Novel novel) {
        this.frame = frame;
        this.novelInfo = novelInfo;
        this.novel = novel;
        setupPanel();
        setupContent();
        refreshScreen();
    }
    //display novel text to reader in JScrollPane
    //to allow for continuous scrolling
    private void setupContent() {
        //JTextArea to display chapter content
        text = new JTextArea(WebScraping.getChapterContent(novel, novel.getLastReadChapter()));
        text.setBackground(Design.screenLightBackground);
        text.setForeground(Design.foreground);
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
        content.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //closes the current novel and displays novelInfo screen
        goBack = new JButton("Go Back");
        goBack.setFont(Design.buttonTextFont);
        goBack.setBackground(Design.novelButtonBackground);
        goBack.setForeground(Design.screenBackground);
        goBack.addMouseListener(new ButtonStyle());
        goBack.setFocusable(false);
        goBack.setBounds(20, 30, 150, 40);
        goBack.addActionListener(e -> {
            content.setVisible(false);
            novelInfo.setVisible(true);
            frame.setTitle(String.format("Viewing %s", novel.getNovelName()));
        });

        //text area to skip to a chapter
        skip = new JTextArea();
        skip.setBackground(Design.novelButtonBackground);
        skip.setForeground(Design.screenBackground);
        skip.setFont(Design.buttonTextFont);
        skip.setDocument(new TextAreaLimit(4));
        skip.setText(Integer.toString(novel.getChapterRange()[1]-1));
        skip.setBounds(200, 30, 60, 40);

        //shows the maximum chapter the user can jump too before an error occurs
        JLabel max = new JLabel("/"+novel.getChapterRange()[1]);
        max.setForeground(Design.novelButtonBackground);
        max.setFont(Design.buttonTextFont);
        max.setBounds(265, 25, 85, 40);

        //navigation buttons
        go = new JButton("GO");
        go.setFont(Design.buttonTextFont);
        go.setBounds(350, 30, 80, 40);
        go.setBackground(Design.novelButtonBackground);
        go.setForeground(Design.screenBackground);
        go.addMouseListener(new ButtonStyle());
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

        //go back one chapter
        back = new JButton("< Back");
        back.setFont(Design.buttonTextFont);
        back.setBackground(Design.novelButtonBackground);
        back.setForeground(Design.screenBackground);
        back.setBounds(125, 30, 150, 40);
        back.addMouseListener(new ButtonStyle());
        back.addActionListener(e -> {
            novel.setLastReadChapter(novel.getLastReadChapter()-1);
            refreshScreen();
        });

        //go forward one chapter
        next = new JButton("Next >");
        next.setFont(Design.buttonTextFont);
        next.setBackground(Design.novelButtonBackground);
        next.setForeground(Design.screenBackground);
        next.setBounds(325, 30, 150, 40);
        next.addMouseListener(new ButtonStyle());
        next.addActionListener(e -> {
            novel.setLastReadChapter(novel.getLastReadChapter()+1);
            refreshScreen();
        });

        //header
        JPanel top = new JPanel();
        top.setBackground(Design.screenLightBackground);
        top.setPreferredSize(new Dimension(Misc.WIDTH, 100));
        top.setLayout(null);
        top.add(goBack);
        top.add(skip);
        top.add(max);
        top.add(go);

        //footer
        JPanel bot = new JPanel();
        bot.setBackground(Design.screenLightBackground);
        bot.setPreferredSize(new Dimension(Misc.WIDTH, 100));
        bot.setLayout(null);
        bot.add(next);
        bot.add(back);

        //side margins (left and right)
        JPanel left = new JPanel();
        left.setBackground(Design.screenLightBackground);
        left.setPreferredSize(new Dimension(10, Misc.HEIGHT));

        JPanel right = new JPanel();
        right.setBackground(Design.screenLightBackground);
        right.setPreferredSize(new Dimension(10, Misc.HEIGHT));

        content.add(top, BorderLayout.NORTH);
        content.add(bot, BorderLayout.SOUTH);
        content.add(left, BorderLayout.WEST);
        content.add(right, BorderLayout.EAST);
        content.setVisible(false);
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

    public JPanel getPanel() {
        return content;
    }
}

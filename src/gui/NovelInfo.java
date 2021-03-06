package gui;

import objects.Novel;
import tools.ButtonStyle;
import tools.Design;
import tools.Misc;
import tools.WebScraping;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

/**
 * This creates the novel info screen, where uses can find more information on a specific novel.
 * Displayed information includes the novel name, author, rating, genre list, summary, and the first and last
 * 3 chapters of the novel. Users can jump to these 6 given chapters, or decide to resume to the last read chapter.
 * If the user has not read the novel before, this is chapter 1. The user can also bookmark the novel so that the
 * information can be tracked in the library screen.
 */
public class NovelInfo {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot;
    private Browse browse;
    private JLabel gif;
    private Library library;
    private Recommend recommend;

    private Novel novel;
    private NovelDisplay novelDisplay;
    private SwingWorker worker = null; //allows "multi-threading"

    public static int previousScreen = -1;

    //basic constructor
    public NovelInfo(JFrame frame, Browse browse, Novel novel, Library library, Recommend recommend) {
        this.frame = frame;
        this.browse = browse;
        this.novel = novel;
        this.library = library;
        this.recommend = recommend;
        setupPanel();
        setupContent();
        frame.setTitle(Misc.novelTitle(novel));
    }

    private void setupContent() {
        //gif has to be added first to be on top
        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setBounds(250, 50, 100, 100);
        gif.setVisible(false);
        top.add(gif);

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
        title.setBounds(200, 50, 350, 110);
        top.add(title);

        //novel author
        JLabel author = new JLabel("<html>"+novel.getAuthor()+"</html>");
        author.setForeground(Design.foreground);
        author.setFont(Design.novelTextFont.deriveFont(16f));
        author.setBounds(200, 160, 350, 50);
        top.add(author);

        //novel summary in JScrollPane
        JLabel summary = new JLabel("Novel Summary");
        summary.setForeground(Design.foreground);
        summary.setFont(Design.buttonTextFont.deriveFont(24f));
        summary.setBounds(200, 200, 200, 50);
        top.add(summary);

        JLabel rating = new JLabel(novel.getRating());
        rating.setForeground(Design.foreground);
        rating.setFont(Design.buttonTextFont.deriveFont(10f));
        rating.setBounds(25, 215, 200, 30);
        top.add(rating);

        //add to library
        JButton libraryButton = new JButton("Add to Library");
        libraryButton.setFont(Design.buttonTextFont.deriveFont(16f));
        libraryButton.setForeground(Design.screenBackground);
        libraryButton.setBackground(library.getBookshelf().contains(novel)? Design.novelButtonBackground:Design.red);
        libraryButton.setBounds(425, 200, 150, 50);
        libraryButton.setFocusable(false);
        libraryButton.addActionListener(e -> {
            if(library.getBookshelf().contains(novel)) {
                library.getBookshelf().remove(novel);
                libraryButton.setBackground(Design.red);
            } else {
                library.getBookshelf().add(novel);
                libraryButton.setBackground(Design.novelButtonBackground);
            }
            library.updateLibrary();
        });
        top.add(libraryButton);

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
        scroll.setBounds(50, 25, 500, 175);
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
            chapter[i].setFocusable(false);
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

        JLabel genre = new JLabel("Genre List");
        genre.setForeground(Design.foreground);
        genre.setFont(Design.buttonTextFont.deriveFont(18f));
        genre.setBounds(400, 25, 200, 50);
        bot.add(genre);

        //list of genres related to this novel
        String genres = Arrays.toString(novel.getGenreList());
        JLabel genreList = new JLabel("<html>"+ genres.substring(1, genres.length()-1)+"</html>");
        genreList.setForeground(Design.foreground);
        genreList.setFont(Design.buttonTextFont.deriveFont(12f));
        genreList.setBounds(400, 75, 125, 175);
        bot.add(genreList);

        //resume to last read chapter
        JButton resume = new JButton("Resume");
        resume.setFont(Design.buttonTextFont.deriveFont(24f));
        resume.setBounds(400, 250, 150, 50);
        resume.setForeground(Design.screenBackground);
        resume.setBackground(Design.novelButtonBackground);
        resume.addMouseListener(new ButtonStyle());
        resume.setFocusable(false);
        resume.addActionListener(e -> refreshScreen(novel.getLastReadChapter()));
        bot.add(resume);

        //closes the current novel and displays novelInfo screen
        JButton goBack = new JButton("Go Back");
        goBack.setFont(Design.buttonTextFont.deriveFont(16f));
        goBack.setBounds(10, 10, 120, 30);
        goBack.setBackground(Design.novelButtonBackground);
        goBack.setForeground(Design.screenBackground);
        goBack.addMouseListener(new ButtonStyle());
        goBack.setFocusable(false);
        goBack.addActionListener(e -> {
            content.setVisible(false);
            if(previousScreen==1) { //library
                library.getPanel().setVisible(true);
                frame.setTitle(Misc.libraryTitle);
            } else if(previousScreen==2) { //browse
                browse.getPanel().setVisible(true);
                frame.setTitle(Misc.browseTitle);
            } else if(previousScreen==3) { //recommend
                recommend.getPanel().setVisible(true);
                frame.setTitle(Misc.recommendTitle);
            }
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
        content.setVisible(false);
    }

    private void refreshScreen(int targetChapter) {
        setupWorker(targetChapter);
        worker.execute();
    }

    //used to multithread, aka load novel and display loading screen gif
    //because Swing works off of only one thread ):
    private void setupWorker(int targetChapter) {
        worker = new SwingWorker() {
            @Override
            protected Void doInBackground() {
                //displaying gif
                gif.setVisible(true);
                if(novelDisplay==null) {
                    novelDisplay = new NovelDisplay(frame, content, novel);
                }
                novel.setLastReadChapter(targetChapter);
                novelDisplay.refreshScreen();
                return null;
            }
            @Override
            protected void done() {
                gif.setVisible(false);
                content.setVisible(false);
                novelDisplay.getPanel().setVisible(true);
            }
        };
    }

    public JPanel getPanel() {
        return content;
    }
}

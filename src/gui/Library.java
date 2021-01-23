package gui;

import objects.Bookshelf;
import objects.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tools.ButtonStyle;
import tools.Design;
import tools.Misc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * This creates the library screen, where users will be able to view their bookmarked novels.
 * The novels in the library will have their last read chapter tracked even when the application
 * is closed. This means that the information will be saved and loaded everytime the application runs.
 * The novels are clickable, and will redirect the user to the novel information screen, with more information in that file.
 */
public class Library {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot, helpPanel;
    private JLabel highlight, helpHighlight, gif;
    private JButton help;
    private JScrollPane scroll;

    private SwingWorker worker = null; //allows "multi-threading"

    private Bookshelf bookshelf;
    private Browse browse;
    private Recommend recommend;
    private Novel novelPlaceHolder;
    private NovelInfo novelInfo;

    private double scaleFactor = 3/5f;
    private int novelWidth, novelHeight, thickness = 4;
    private boolean clickHelp = false, clickGraphic = false;

    //basic constructor
    public Library(JFrame frame, Bookshelf bookshelf) {
        this.frame = frame;
        this.bookshelf = bookshelf;
        setupPanel();
        setupContent();
        setupDashboard();
        setupFrame();
    }

    //creates the logic to save files before the JFrame is closed and program is terminated
    private void setupFrame() {
        frame.setTitle(Misc.libraryTitle);
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
            JOptionPane.showMessageDialog(frame, "Saving files. Click ok and wait until a confirmation message.");
            bookshelf.save();
            JOptionPane.showMessageDialog(frame, "Thank you for your patience. You can close the screen now");
            }
        });
    }

    private void setupContent() {
        //screen header
        JLabel library = new JLabel("Library");
        library.setForeground(Design.foreground);
        library.setFont(Design.buttonTextFont.deriveFont(24f));
        library.setBounds(25, 0, 100, 50);
        top.add(library);

        //button to display library help screen
        help = new JButton();
        help.setIcon(new ImageIcon(new ImageIcon("./res/help.png").getImage().getScaledInstance(35, 35, 0)));
        help.setBackground(Design.novelButtonBackground);
        help.addMouseListener(new ButtonStyle());
        help.addActionListener(e -> help());
        help.setBounds(500, 2, 46, 46);
        top.add(help);

        //highlight to go show the help button is clicked
        helpHighlight = new JLabel();
        helpHighlight.setIcon(new ImageIcon("./res/highlight-2.png"));
        helpHighlight.setBounds(498, 0, 50, 50);
        helpHighlight.setVisible(false);
        top.add(helpHighlight);

        //header to show user "this is the help screen"
        JLabel help = new JLabel("Library Help Screen");
        help.setForeground(Design.foreground);
        help.setFont(Design.buttonTextFont.deriveFont(24f));
        help.setBounds(150, 20, 300, 50);
        helpPanel.add(help);

        //help text label
        JLabel libraryInfo = new JLabel("<html>"+Misc.libraryInfo+"</html>");
        libraryInfo.setForeground(Design.foreground);
        libraryInfo.setFont(Design.novelTextFont);
        libraryInfo.setBounds(50, 50, 500, 300);
        helpPanel.add(libraryInfo);

        //annotated help screen
        JLabel libraryGraphic = new JLabel();
        libraryGraphic.setIcon(new ImageIcon(new ImageIcon("./res/help/library.png").getImage().getScaledInstance(400, 570, 0)));
        libraryGraphic.setBounds(50, 20, 400, 570);
        libraryGraphic.setVisible(false);
        helpPanel.add(libraryGraphic);

        //button to display help screen graphic or help screen text
        JButton moreHelp = new JButton("More Help");
        moreHelp.setForeground(Design.screenBackground);
        moreHelp.setBackground(Design.novelButtonBackground);
        moreHelp.setFont(Design.novelTextFont);
        moreHelp.setBounds(460, 500, 115, 50);
        moreHelp.addMouseListener(new ButtonStyle());
        moreHelp.addActionListener(e -> {
            libraryGraphic.setVisible(!clickGraphic);
            libraryInfo.setVisible(clickGraphic);
            help.setVisible(clickGraphic);
            clickGraphic = !clickGraphic;
        });
        helpPanel.add(moreHelp);

        //JScrollPane to allow for continuous scrolling of browsing novels
        scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        content.add(scroll);

        //shown when things are loaded
        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setVisible(false);
        gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);

        //updates the current library
        updateLibrary();
    }

    private void setupPanel() {
        //need to initialize this value to set center panel height
        novelWidth = (int)(Misc.novel.getThumbnailWidth()*scaleFactor);
        novelHeight = (int)(Misc.novel.getThumbnailHeight()*scaleFactor);

        //panel to display personal library
        content.setBackground(Color.green);
        content.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //library screen
        top = new JPanel();
        top.setBackground(Design.screenBackground);
        top.setPreferredSize(Design.header);
        top.setLayout(null);

        //bookmarked novels
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setLayout(null);
        center.setPreferredSize(new Dimension(Misc.WIDTH, 50+bookshelf.size()*(novelHeight+50)));

        //panel to display help contents
        helpPanel = new JPanel();
        helpPanel.setBackground(Design.screenLightBackground);
        helpPanel.setLayout(null);
        helpPanel.setVisible(false);
        helpPanel.setBounds(0, 50, 600, 612);
        content.add(helpPanel);

        //application dashboard
        bot = new JPanel();
        bot.setBackground(Design.screenBackground);
        bot.setPreferredSize(Design.footer);
        bot.setLayout(null);

        //panel containing everything
        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(bot, BorderLayout.SOUTH);
    }

    private void setupDashboard() {
        //redirects to library, the current screen
        JButton library = new JButton();
        library.setIcon(new ImageIcon(new ImageIcon("./res/library.png").getImage().getScaledInstance(90, 90, 0)));
        library.setBounds(55, 5, 90, 90);
        library.setBackground(Design.novelButtonBackground);
        library.addMouseListener(new ButtonStyle());
        library.setFocusable(false);
//        library.addActionListener(e -> refreshScreen(1));
        bot.add(library);

        //label to explain in case the library icon is not clear enough
        JLabel libraryLabel = new JLabel("Library");
        libraryLabel.setBounds(55, 100, 100, 25);
        libraryLabel.setForeground(Design.foreground);
        libraryLabel.setFont(Design.novelTextFont);
        bot.add(libraryLabel);

        //redirects to browse screen
        JButton window = new JButton();
        window.setIcon(new ImageIcon(new ImageIcon("./res/window.png").getImage().getScaledInstance(90, 90, 0)));
        window.setBounds(255, 5, 90, 90);
        window.setBackground(Design.novelButtonBackground);
        window.addMouseListener(new ButtonStyle());
        window.setFocusable(false);
        window.addActionListener(e -> refreshScreen(2));
        bot.add(window);

        //label to explain in case the browse icon is not clear enough
        JLabel windowLabel = new JLabel("Browse");
        windowLabel.setBounds(255, 100, 100, 25);
        windowLabel.setForeground(Design.foreground);
        windowLabel.setFont(Design.novelTextFont);
        bot.add(windowLabel);

        //redirects to the recommend screen
        JButton recommend = new JButton();
        recommend.setIcon(new ImageIcon(new ImageIcon("./res/recommend.png").getImage().getScaledInstance(90, 90, 0)));
        recommend.setBounds(455, 5, 90, 90);
        recommend.setBackground(Design.novelButtonBackground);
        recommend.addMouseListener(new ButtonStyle());
        recommend.setFocusable(false);
        recommend.addActionListener(e -> refreshScreen(3));
        bot.add(recommend);

        //label to explain in case the recommend icon is not clear enough
        JLabel recommendLabel = new JLabel("Recommend");
        recommendLabel.setBounds(455, 100, 100, 25);
        recommendLabel.setForeground(Design.foreground);
        recommendLabel.setFont(Design.novelTextFont);
        bot.add(recommendLabel);

        //highlight to show the user what screen they are currently on
        highlight = new JLabel();
        highlight.setIcon(new ImageIcon("./res/highlight-2.png"));
        highlight.setBounds(50, 0, 100, 100);
        bot.add(highlight);
    }

    //used in conjunction with the swing worker to allow for "multi-threading"
    public void refreshScreen(int location) {
        setupWorker(location);
        worker.execute();
    }

    //overloading ^^ to also include a novel parameter
    public void refreshScreen(int location, Novel novel) {
        novelPlaceHolder = novel;
        setupWorker(location);
        worker.execute();
    }

    //overloading once again, but includes the logic associated with
    //button presses (library button goes to library screen etc.)
    private void refreshScreen(int location, int random) {
        if(location==-1) { //displaying novel info
            NovelInfo.previousScreen = 1;
            novelInfo = new NovelInfo(frame, browse, novelPlaceHolder, this, recommend);
            frame.setTitle(String.format("Viewing %s", novelPlaceHolder.getNovelName()));
        } else if(location==1) { //go to library
        } else if(location==2) { //go to to browse screen
            if(browse==null) {
                browse = new Browse(frame, this, recommend);
                recommend.setBrowse(browse);
            } else {
                browse.getPanel().setVisible(true);
            }
            frame.setTitle(Misc.browseTitle);
        } else if(location==3) { //go to recommend screen
            if(recommend==null) {
                recommend = new Recommend(frame, this, browse);
            } else {
                recommend.updateRecommendation();
            }
            frame.setTitle(Misc.recommendTitle);
        }
    }

    //used to multithread, aka load novels and display loading screen gif
    //because Swing works off of only one thread ):
    private void setupWorker(int location) {
        worker = new SwingWorker() {
            @Override
            protected Void doInBackground() {
                //displaying gif
                gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);
                gif.setVisible(true);
                refreshScreen(location, -1);
                return null;
            }
            @Override
            protected void done() {
                if(location==-1) {
                    novelInfo.getPanel().setVisible(true);
                } else if(location==2) {
                    browse.getPanel().setVisible(true);
                } else if(location==3) {
                    recommend.getPanel().setVisible(true);
                }
                gif.setVisible(false);
                content.setVisible(false);
            }
        };
    }

    //updates the contents of the library and shows the appropriate
    //message based on status of the bookshelf
    public void updateLibrary() {
        center.removeAll();
        center.add(gif);

        if(bookshelf.isEmpty()) { //display empty library message
            JLabel info = new JLabel("<html>"+Misc.emptyLibrary+"</html>");
            info.setForeground(Design.foreground);
            info.setFont(Design.novelTextFont);
            info.setBounds(125, 100, 350, 300);
            center.add(info);
        } else { //display novels
            for (int i = 0; i < bookshelf.size(); i++) {
                Novel novel = bookshelf.get(i);

                //novel thumbnail
                JLabel icon = new JLabel();
                icon.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(novelWidth, novelHeight, 0)));
                icon.setBounds(50, 50 + i*(novelHeight+50), novelWidth+2*thickness, novelHeight+2*thickness);
                icon.setBorder(BorderFactory.createLineBorder(Design.screenPop, thickness));
                center.add(icon);

                //novel title
                JLabel title = new JLabel("<html>"+novel.getNovelName()+"</html>");
                title.setForeground(Design.foreground);
                title.setFont(Design.novelTextFont.deriveFont(22f));
                title.setBounds(200, 20 + i*(novelHeight+50), 350, 100);
                center.add(title);

                //novel author
                JLabel author = new JLabel(novel.getAuthor());
                author.setForeground(Design.foreground);
                author.setFont(Design.buttonTextFont.deriveFont(18f));
                author.setBounds(200, 100 + i*(novelHeight+50), 350, 50);
                center.add(author);

                //summary
                JLabel summary = new JLabel("<html>"+limit(novel.getSummary())+"</html>");
                summary.setForeground(Design.foreground);
                summary.setFont(Design.novelTextFont);
                summary.setBounds(200, 140 + i*(novelHeight+50), 350, 125);
                center.add(summary);

                //invisible but clickable button
                JButton click = new JButton();
                click.setOpaque(false);
                click.setContentAreaFilled(false);
                click.setFocusable(false);
                click.setBorder(BorderFactory.createEmptyBorder());
                click.setBounds(50, 50 + i*(novelHeight+50), 500, 200);
                click.addActionListener(e -> refreshScreen(-1, novel));
                center.add(click);
            }
            center.setPreferredSize(new Dimension(Misc.WIDTH, 50 + bookshelf.size() * (novelHeight + 50)));
        }
    }

    //used to display the help screen
    private void help() {
        if(!clickHelp) { //show help screen
            helpPanel.add(gif);
            center.setVisible(false);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
            helpPanel.setVisible(true);
            helpHighlight.setVisible(true);
        } else { //show library screen
            center.add(gif);
            helpPanel.setVisible(false);
            center.setVisible(true);
            scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            helpHighlight.setVisible(false);
        }
        clickHelp = !clickHelp;
    }

    //used to limit the novel summary so that only a snippet is displayed
    private String limit(String text) {
        String arr[] = text.split("[ ]"), ret="";
        for(int i=0;i<Math.min(25, arr.length);i++) {
            if(arr[i]=="") continue;
            ret+=arr[i].trim()+" ";
        }
        return ret+"...";
    }

    public JPanel getPanel() {
        return content;
    }

    public Bookshelf getBookshelf() {
        return bookshelf;
    }

    public void setBrowse(Browse browse) {
        this.browse = browse;
    }
}

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
import java.util.HashMap;

/**
 * This creates the novel info screen, where uses can find more information on a specific novel.
 * Displayed information includes the novel name, author, rating, genre list, summary, and the first and last
 * 3 chapters of the novel. Users can jump to these 6 given chapters, or decide to resume to the last read chapter.
 * If the user has not read the novel before, this is chapter 1. The user can also bookmark the novel so that the
 * information can be tracked in the library screen.
 */
public class Recommend {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot, helpPanel;
    private JLabel highlight, gif, helpHighlight;
    private JButton help;
    private JScrollPane scroll;
    private SwingWorker worker = null; //allows "multi-threading"

    private Library library;
    private Browse browse;
    private Bookshelf recommendations = new Bookshelf();
    private Novel novelPlaceHolder;
    private NovelInfo novelInfo;

    private double scaleFactor = 1/2f;
    private int novelWidth, novelHeight, thickness = 4;
    private boolean clickHelp = false, clickGraphic = false;
    //calculates page number for recommendation
    //in case the user bookmarks all the novels in page 1
    private HashMap<String, Integer> map = new HashMap();

    //basic constructor
    public Recommend(JFrame frame, Library library, Browse browse) {
        this.frame = frame;
        this.library = library;
        this.browse = browse;
        setupPanel();
        setupContent();
        setupDashboard();
        frame.setTitle(Misc.recommendTitle);
    }

    private void setupContent() {
        //screen header
        JLabel recommend = new JLabel("Recommend");
        recommend.setForeground(Design.foreground);
        recommend.setFont(Design.buttonTextFont.deriveFont(24f));
        recommend.setBounds(25, 0, 150, 50);
        top.add(recommend);

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
        JLabel help = new JLabel("Recommend Help Screen");
        help.setForeground(Design.foreground);
        help.setFont(Design.buttonTextFont.deriveFont(24f));
        help.setBounds(150, 20, 300, 50);
        helpPanel.add(help);

        //help text label
        JLabel recommendInfo = new JLabel("<html>"+Misc.recommendInfo+"</html>");
        recommendInfo.setForeground(Design.foreground);
        recommendInfo.setFont(Design.novelTextFont);
        recommendInfo.setBounds(50, 50, 500, 300);
        helpPanel.add(recommendInfo);

        //annotated help screen
        JLabel recommendGraphic = new JLabel();
        recommendGraphic.setIcon(new ImageIcon(new ImageIcon("./res/help/recommend.png").getImage().getScaledInstance(400, 570, 0)));
        recommendGraphic.setBounds(50, 20, 400, 570);
        recommendGraphic.setVisible(false);
        helpPanel.add(recommendGraphic);

        //button to display help screen graphic or help screen text
        JButton moreHelp = new JButton("More Help");
        moreHelp.setForeground(Design.screenBackground);
        moreHelp.setBackground(Design.novelButtonBackground);
        moreHelp.setFont(Design.novelTextFont);
        moreHelp.setBounds(460, 500, 115, 50);
        moreHelp.addMouseListener(new ButtonStyle());
        moreHelp.addActionListener(e -> {
            recommendGraphic.setVisible(!clickGraphic);
            recommendInfo.setVisible(clickGraphic);
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
        gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);
        gif.setVisible(false);

        //updates the current recommendation
        updateRecommendation();
    }

    private void setupPanel() {
        //need to initialize this value to set center panel height
        novelWidth = (int)(Misc.novel.getThumbnailWidth()*scaleFactor);
        novelHeight = (int)(Misc.novel.getThumbnailHeight()*scaleFactor);

        //panel to display personal library
        content.setVisible(false);
        content.setBackground(Color.green);
        content.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //library screen
        top = new JPanel();
        top.setBackground(Design.screenBackground);
        top.setPreferredSize(Design.header);
        top.setLayout(null);

        //recommendation panel
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setLayout(null);

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

    //updates the recommendation and shows the appropriate
    //novels based on the matchmaking algorithm
    public void updateRecommendation() {
        center.removeAll();
        center.add(gif);

        if(library.getBookshelf().isReadyForRecommendation()) { //display novel recommendation
            clickHelp = true;
            help();
            library.getBookshelf().sort();
            center.setPreferredSize(new Dimension(Misc.WIDTH,6*(novelHeight+50+50)));
            int index = 0;

            //creating a new copy of the exisitng bookshelf
            recommendations.clear();
            for(Novel novel:library.getBookshelf().getAllNovels()) {
                recommendations.add(novel);
            }

            //adding recommendations based on top 3 genres
            for(String genre:library.getBookshelf().getTopGenre()) {
                JLabel header = new JLabel(String.format("<html>Because you read %s from:</html>", genre));
                header.setForeground(Design.foreground);
                header.setFont(Design.novelTextFont.deriveFont(22f));
                header.setBounds(100, 25+index*(2*novelHeight+100+50+50), 400, 50);
                center.add(header);

                int sideways = 0;
                //novels the user has read that fall under the give genre
                for(Novel novel:library.getBookshelf().getNovelFromGenre(genre)) {
                    //novel thumbnail
                    JButton icon = new JButton();
                    icon.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(novelWidth, novelHeight, 0)));
                    icon.setBounds(50+175*sideways++, 25+50 + 2*index*(novelHeight+50+50), novelWidth+2*thickness, novelHeight+2*thickness);
                    icon.setBorder(BorderFactory.createLineBorder(Design.screenPop, thickness));
                    icon.addActionListener(e -> refreshScreen(-1, novel));
                    center.add(icon);
                }

                //recommendations
                JLabel secondaryHeader = new JLabel("You might like:");
                secondaryHeader.setForeground(Design.foreground);
                secondaryHeader.setFont(Design.novelTextFont.deriveFont(22f));
                secondaryHeader.setBounds(100, 25+50+novelHeight+index*(2*novelHeight+75+50+75), 400, 50);
                center.add(secondaryHeader);

                sideways = 0;
                //new novels that aren't in library, that fit the genre tag
                for(Novel novel:findChapter(genre)) {
                    //novel thumbnail
                    JButton icon = new JButton();
                    icon.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(novelWidth, novelHeight, 0)));
                    icon.setBounds(50+175*sideways++, 25+50 + 50 + novelHeight+2*index*(novelHeight+50+50), novelWidth+2*thickness, novelHeight+2*thickness);
                    icon.setBorder(BorderFactory.createLineBorder(Design.screenHighlight, thickness));
                    icon.addActionListener(e -> refreshScreen(-1, novel));
                    center.add(icon);
                }
                index++;
            }
        } else { //read more novel message
            JLabel info = new JLabel("<html>"+Misc.notEnoughTitles+"</html>");
            info.setForeground(Design.foreground);
            info.setFont(Design.novelTextFont);
            info.setBounds(125, 100, 350, 200);
            center.add(info);
        }
    }

    //finds the top 3 chapters from a given genre
    private Novel[] findChapter(String genre) {
        int page = 1 + map.getOrDefault(genre, 0);
        String url = "https://novelfull.com/genre/"+genre.trim().replace(" ", "+")+"?page="+page;
        map.put(genre, 1 + map.getOrDefault(genre, 0));
        Novel ret[] = new Novel[3];
        int index = 0;
        try {
            Document doc = Jsoup.connect(url).get();
            //all the <div> with title information are in a class called "col-xs-7"
            //to call a class from a div, the dot is used
            for(Element row:doc.select("div.col-xs-7")) {
                if(index>2) break;
                String novelLink = row.select("h3 > a").attr("href");
                String novelName = row.select("h3 > a").text();

                Novel novel = new Novel(novelName, novelLink);
                if(!recommendations.contains(novel)) {
                    ret[index++] = novel;
                    recommendations.add(novel);
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println("HAD PROBLEM GETTING GENRE");
        }
        return ret;
    }

    private void setupDashboard() {
        //redirects to library
        JButton library = new JButton();
        library.setIcon(new ImageIcon(new ImageIcon("./res/library.png").getImage().getScaledInstance(90, 90, 0)));
        library.setBounds(55, 5, 90, 90);
        library.setBackground(Design.novelButtonBackground);
        library.addMouseListener(new ButtonStyle());
        library.setFocusable(false);
        library.addActionListener(e -> refreshScreen(1));
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

        //redirects to the recommend screen, the current screen
        JButton recommend = new JButton();
        recommend.setIcon(new ImageIcon(new ImageIcon("./res/recommend.png").getImage().getScaledInstance(90, 90, 0)));
        recommend.setBounds(455, 5, 90, 90);
        recommend.setBackground(Design.novelButtonBackground);
        recommend.addMouseListener(new ButtonStyle());
        recommend.setFocusable(false);
//        recommend.addActionListener(e -> refreshScreen(3));
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
        highlight.setBounds(450, 0, 100, 100);
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
            NovelInfo.previousScreen = 3;
            novelInfo = new NovelInfo(frame, browse, novelPlaceHolder, library, this);
        }
        else if(location==1) { //go to library
            library.getPanel().setVisible(true);
            frame.setTitle(Misc.libraryTitle);
        } else if(location==2) { //go to browse screen
            if(browse==null) {
                browse = new Browse(frame, library, this);
                library.setBrowse(browse);
            } else {
                browse.getPanel().setVisible(true);
            }
            frame.setTitle(Misc.browseTitle);
        } else if(location==3) { //go to recommend
        }
    }

    //used to multithread, aka load novels and display loading screen gif
    //because Swing works off of only one thread ):
    private void setupWorker(int location) {
        worker = new SwingWorker() {
            @Override
            protected Void doInBackground() {
                //shown when things are loaded
                gif.setVisible(true);
                gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);
                refreshScreen(location, -1);
                return null;
            }
            @Override
            protected void done() {
                if(location==-1) {
                    novelInfo.getPanel().setVisible(true);
                } else if(location==2) {
                    browse.getPanel().setVisible(true);
                }
                gif.setVisible(false);
                content.setVisible(false);
            }
        };
    }

    //used to display help screen
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

    public JPanel getPanel() {
        return content;
    }

    public void setBrowse(Browse browse) {
        this.browse = browse;
    }
}

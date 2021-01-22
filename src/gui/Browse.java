package gui;

import objects.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tools.ButtonStyle;
import tools.Design;
import tools.Misc;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Browse {
    private JFrame frame;
    public static JPanel content = new JPanel();
    private JPanel top, center, bot, helpPanel;
    private JLabel gif, helpHighlight;
    private JButton viewMore, help;

    private JScrollPane scroll;
    private SwingWorker worker = null; //allows "multi-threading"

    private ArrayList<Novel> list = new ArrayList();
    private Novel novel, novelPlaceHolder;
    private NovelInfo novelInfo;
    private Library library;
    private Recommend recommend;

    private int novelPerPage = 20, total = 0, amountPerLoad = 4, page = 0, size;
    private double scaleFactor = 3/5f;

    private int novelWidth, novelHeight, thickness = 4;
    private boolean firstOpen = true, clickHelp = false, clickGraphic = false;

    private JLabel highlight;

    public Browse(JFrame frame, Library library, Recommend recommend) {
        this.frame = frame;
        this.library = library;
        this.recommend = recommend;
        setupPanel();
        setupContent();
        setupDashboard();
//        setupFrame();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle(Misc.browseTitle);
    }

    private void setupContent() {
        //screen header
        JLabel browse = new JLabel("Browse");
        browse.setForeground(Design.foreground);
        browse.setFont(Design.buttonTextFont.deriveFont(24f));
        browse.setBounds(25, 0, 100, 50);
        top.add(browse);

        help = new JButton();
        help.setIcon(new ImageIcon(new ImageIcon("./res/help.png").getImage().getScaledInstance(35, 35, 0)));
        help.setBackground(Design.novelButtonBackground);
        help.addMouseListener(new ButtonStyle());
        help.addActionListener(e -> help());
        help.setBounds(500, 2, 46, 46);
        top.add(help);

        helpHighlight = new JLabel();
        helpHighlight.setIcon(new ImageIcon("./res/highlight-2.png"));
        helpHighlight.setBounds(498, 0, 50, 50);
        helpHighlight.setVisible(false);
        top.add(helpHighlight);

        JLabel help = new JLabel("Browse Help Screen");
        help.setForeground(Design.foreground);
        help.setFont(Design.buttonTextFont.deriveFont(24f));
        help.setBounds(150, 20, 300, 50);
        helpPanel.add(help);

        JLabel browseInfo = new JLabel("<html>"+Misc.browseInfo+"</html>");
        browseInfo.setForeground(Design.foreground);
        browseInfo.setFont(Design.novelTextFont);
        browseInfo.setBorder(BorderFactory.createLineBorder(Color.white));
        browseInfo.setBounds(50, 50, 500, 400);
        helpPanel.add(browseInfo);

        JLabel browseGraphic = new JLabel();
        browseGraphic.setIcon(new ImageIcon(new ImageIcon("./res/help/browse.png").getImage().getScaledInstance(400, 570, 0)));
        browseGraphic.setBounds(50, 20, 400, 570);
        browseGraphic.setVisible(false);
        helpPanel.add(browseGraphic);

        JButton moreHelp = new JButton("More Help");
        moreHelp.setForeground(Design.screenBackground);
        moreHelp.setBackground(Design.novelButtonBackground);
        moreHelp.setFont(Design.novelTextFont);
        moreHelp.setBounds(460, 500, 115, 50);
        moreHelp.addMouseListener(new ButtonStyle());
        moreHelp.addActionListener(e -> {
            browseGraphic.setVisible(!clickGraphic);
            browseInfo.setVisible(clickGraphic);
            help.setVisible(clickGraphic);
            clickGraphic = !clickGraphic;
        });
        helpPanel.add(moreHelp);

        //shown when things are loaded
        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setVisible(false);
        center.add(gif);

        //JScrollPane to allow for continuous scrolling of browsing novels
        scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        content.add(scroll);

        //loads up initial novels
        loadChapters();

        //click to view more novels
        viewMore = new JButton("View More");
        viewMore.setFont(Design.buttonTextFont.deriveFont(24f));
        viewMore.setBounds(200, 50 + list.size()*(novelHeight+50), 200, 50);
        viewMore.setForeground(Design.screenBackground);
        viewMore.setBackground(Design.novelButtonBackground);
        viewMore.addMouseListener(new ButtonStyle());
        viewMore.setFocusable(false);
        viewMore.addActionListener(e -> {
            size = list.size();
            viewMore.setVisible(false); //hide viewMore button
            setupWorker(0);
            worker.execute();
        });
        center.add(viewMore);
    }

    private void setupPanel() {
        //need to initialize this value to set center panel height
        novelWidth = (int)(Misc.novel.getThumbnailWidth()*scaleFactor);
        novelHeight = (int)(Misc.novel.getThumbnailHeight()*scaleFactor);

        //panel to display the mangas
        content.setBackground(Color.GREEN);
        content.setBounds(0, 0, Misc.WIDTH, 1000);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //browse screen
        top = new JPanel();
        top.setBackground(Design.screenBackground);
        top.setPreferredSize(Design.header);
        top.setLayout(null);

        //browsable novels
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setLayout(null);
        center.setPreferredSize(new Dimension(Misc.WIDTH, 150+total*(novelHeight+50)));

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
        content.setVisible(false);
    }

    //displays the most recent novel (for an animation like feel)
    //when multiple novels are displayed (rather than everything displaying at once)
    private void displayChapter() {
        //need to create a local instance of the novel or else
        //the action listener will pick up the global novel variable (aka no good)
        int i = list.size()-1;
        Novel novel = list.get(i);

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

        //updating gif to be in the center of the screen is user scrolls
        gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);
        center.setPreferredSize(new Dimension(Misc.WIDTH, 150+total*(novelHeight+50)));
    }

    //load chapters into arraylist
    private void loadChapters() {
        total += amountPerLoad;
        int count = amountPerLoad;
        try {
            while(count>0) {
                String url = "https://novelfull.com/most-popular?page="+(++page);
                Document doc = Jsoup.connect(url).get();
                //all the <div> with title information are in a class called "col-xs-7"
                //to call a class from a div, the dot is used
                for(Element row:doc.select("div.col-xs-7")) {
                    if(count--<=0) break;
                    //finds the <a> tag that is contained in a <h3> tag
                    String novelLink = row.select("h3 > a").attr("href");
                    String novelName = row.select("h3 > a").text();
                    list.add(new Novel(novelName, novelLink));
                    System.out.println(list.get(list.size()-1));
                    displayChapter();
                }
            }
        } catch (IOException e) {
            System.out.println("Problem loading chapters in browse screen");
            e.printStackTrace();
        }
    }

    private void setupDashboard() {
        JButton library = new JButton();
        library.setIcon(new ImageIcon(new ImageIcon("./res/library.png").getImage().getScaledInstance(90, 90, 0)));
        library.setBounds(55, 5, 90, 90);
        library.setBackground(Design.novelButtonBackground);
        library.addMouseListener(new ButtonStyle());
        library.setFocusable(false);
        library.addActionListener(e -> refreshScreen(1));
        bot.add(library);

        JLabel libraryLabel = new JLabel("Library");
        libraryLabel.setBounds(55, 100, 100, 25);
        libraryLabel.setForeground(Design.foreground);
        libraryLabel.setFont(Design.novelTextFont);
        bot.add(libraryLabel);

        JButton window = new JButton();
        window.setIcon(new ImageIcon(new ImageIcon("./res/window.png").getImage().getScaledInstance(90, 90, 0)));
        window.setBounds(255, 5, 90, 90);
        window.setBorder(BorderFactory.createLineBorder(Color.white));
        window.setBackground(Design.novelButtonBackground);
        window.addMouseListener(new ButtonStyle());
        window.setFocusable(false);
//        window.addActionListener(e -> refreshScreen(2));
        bot.add(window);

        JLabel windowLabel = new JLabel("Browse");
        windowLabel.setBounds(255, 100, 100, 25);
        windowLabel.setForeground(Design.foreground);
        windowLabel.setFont(Design.novelTextFont);
        bot.add(windowLabel);

        JButton recommend = new JButton();
        recommend.setIcon(new ImageIcon(new ImageIcon("./res/recommend.png").getImage().getScaledInstance(90, 90, 0)));
        recommend.setBounds(455, 5, 90, 90);
        recommend.setBorder(BorderFactory.createLineBorder(Color.white));
        recommend.setBackground(Design.novelButtonBackground);
        recommend.addMouseListener(new ButtonStyle());
        recommend.setFocusable(false);
        recommend.addActionListener(e -> refreshScreen(3));
        bot.add(recommend);

        JLabel recommendLabel = new JLabel("Recommend");
        recommendLabel.setBounds(455, 100, 100, 25);
        recommendLabel.setForeground(Design.foreground);
        recommendLabel.setFont(Design.novelTextFont);
        bot.add(recommendLabel);

        highlight = new JLabel();
        highlight.setIcon(new ImageIcon("./res/highlight-2.png"));
        highlight.setBounds(250, 0, 100, 100);
        bot.add(highlight);
    }

    public void refreshScreen(int location) {
        setupWorker(location);
        worker.execute();
    }

    public void refreshScreen(int location, Novel novel) {
        novelPlaceHolder = novel;
        setupWorker(location);
        worker.execute();
    }

    private void refreshScreen(int location, int random) {
        if(location==-1) { //displaying novel info
            NovelInfo.previousScreen = 2;
            System.out.println(novelPlaceHolder);
            novelInfo = new NovelInfo(frame, this, novelPlaceHolder, library, recommend);
        }
        else if(location==0) { //view more
            loadChapters();
        }
        else if(location==1) {
            library.getPanel().setVisible(true);
            frame.setTitle(Misc.libraryTitle);
        } else if(location==2) {
        } else if(location==3) {
            if(recommend==null) {
                recommend = new Recommend(frame, library, this);
            } else {
                recommend.updateRecommendation();
            }
            frame.setTitle(Misc.recommendTitle);
        }
    }

    //used to multithread, aka load novel and display loading screen gif
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
                gif.setVisible(false);
                if(location==-1) {
                    content.setVisible(false);
                    novelInfo.getPanel().setVisible(true);
                }
                else if(location==0) {
                    viewMore.setBounds(200, 50 + list.size() * (novelHeight + 50), 200, 50);
                    viewMore.setVisible(true);
                    viewMore.setEnabled(page > 7 ? false : true); //only 7 pages for hot novels
                } else if(location==3) {
                    content.setVisible(false);
                    recommend.getPanel().setVisible(true);
                } else {
                    content.setVisible(false);
                }
            }
        };
    }

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
}

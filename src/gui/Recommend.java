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
import java.awt.print.Book;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Recommend {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot;

    private Library library;
    private Browse browse;
    private Bookshelf recommendations = new Bookshelf();

    private double scaleFactor = 1/2f;
    private int novelWidth, novelHeight, thickness = 4;
    private boolean firstOpen = true;

    private JLabel highlight, gif;

    private JScrollPane scroll;

    private SwingWorker worker = null; //allows "multi-threading"
    private HashMap<String, Integer> map = new HashMap();

    public Recommend(JFrame frame, Library library, Browse browse) {
        this.frame = frame;
        this.library = library;
        this.browse = browse;
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
        frame.setTitle(Misc.recommendTitle);
        frame.repaint();
    }

    private void setupContent() {
        //screen header
        JLabel recommend = new JLabel("Recommend");
        recommend.setForeground(Design.foreground);
        recommend.setFont(Design.buttonTextFont.deriveFont(24f));
        recommend.setBounds(25, 0, 150, 50);
        top.add(recommend);

        //JScrollPane to allow for continuous scrolling of browsing novels
        scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        content.add(scroll);

        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);
        gif.setVisible(false);

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

    public void updateRecommendation() {
        center.removeAll();
        center.add(gif);

        if(library.getBookshelf().isReadyForRecommendation()) {
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
                header.setBorder(BorderFactory.createLineBorder(Color.white));
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
                secondaryHeader.setBorder(BorderFactory.createLineBorder(Color.white));
                center.add(secondaryHeader);

                findChapter(genre);
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
        } else {
            JLabel info = new JLabel("<html>"+Misc.notEnoughTitles+"</html>");
            info.setForeground(Design.foreground);
            info.setFont(Design.novelTextFont);
            info.setBounds(125, 100, 350, 200);
            center.add(info);
        }
    }

    private Novel[] findChapter(String genre) {
        int page = 1 + map.getOrDefault(genre, 0);
        String url = "https://novelfull.com/genre/"+genre.replace(" ", "+")+"?page="+page;
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
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("HAD PROBLEM GETTING GENRE");
        }
        return ret;
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

        JButton window = new JButton();
        window.setIcon(new ImageIcon(new ImageIcon("./res/window.png").getImage().getScaledInstance(90, 90, 0)));
        window.setBounds(255, 5, 90, 90);
        window.setBorder(BorderFactory.createLineBorder(Color.white));
        window.setBackground(Design.novelButtonBackground);
        window.addMouseListener(new ButtonStyle());
        window.setFocusable(false);
        window.addActionListener(e -> refreshScreen(2));
        bot.add(window);

        JButton recommend = new JButton();
        recommend.setIcon(new ImageIcon(new ImageIcon("./res/recommend.png").getImage().getScaledInstance(90, 90, 0)));
        recommend.setBounds(455, 5, 90, 90);
        recommend.setBorder(BorderFactory.createLineBorder(Color.white));
        recommend.setBackground(Design.novelButtonBackground);
        recommend.addMouseListener(new ButtonStyle());
        recommend.setFocusable(false);
//        recommend.addActionListener(e -> refreshScreen(3));
        bot.add(recommend);

        highlight = new JLabel();
        highlight.setIcon(new ImageIcon("./res/highlight-2.png"));
        highlight.setBounds(450, 0, 100, 100);
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

    private Novel novelPlaceHolder;
    private NovelInfo novelInfo;

    private void refreshScreen(int location, int random) {
        if(location==-1) {
            NovelInfo.previousScreen = 3;
            novelInfo = new NovelInfo(frame, browse, novelPlaceHolder, library, this);
        }
        else if(location==1) {
            library.getPanel().setVisible(true);
            frame.setTitle(Misc.libraryTitle);
        } else if(location==2) {
            if(browse==null) {
                browse = new Browse(frame, library, this);
                library.setBrowse(browse);
            } else {
                browse.getPanel().setVisible(true);
            }
            frame.setTitle(Misc.browseTitle);
        } else if(location==3) {
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

    public JPanel getPanel() {
        return content;
    }

    public Browse getBrowse() {
        return browse;
    }
    public void setBrowse(Browse browse) {
        this.browse = browse;
    }
}

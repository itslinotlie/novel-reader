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
import java.io.IOException;
import java.util.ArrayList;

public class Library {
    private JFrame frame;
    private static JPanel content = new JPanel(), top, center, bot;

    private Bookshelf bookshelf;
    private Browse browse;
    private Recommend recommend;
    private Novel novelPlaceHolder;
    private NovelInfo novelInfo;

    private double scaleFactor = 3/5f;
    private int novelWidth, novelHeight, thickness = 4;

    private JLabel highlight, gif;

    private JScrollPane scroll;

    private SwingWorker worker = null; //allows "multi-threading"

    public static void main(String[] args) {
        Bookshelf one = new Bookshelf();
        Bookshelf two = new Bookshelf();

        Novel novel1 = new Novel("Overgeared1", "/overgeared.html");
        Novel novel2 = new Novel("Overgeared2", "/overgeared.html");
        Novel novel3 = new Novel("Overgeared3", "/overgeared.html");
        Novel novel4 = new Novel("Overgeared4", "/overgeared.html");

        one.add(novel1); one.add(novel3);
        one.add(novel2); one.add(novel4);

        new Library(new JFrame(), one);
    }

    public Library(JFrame frame, Bookshelf bookshelf) {
        this.frame = frame;
        this.bookshelf = bookshelf;
        setupPanel();
        setupContent();
        setupDashboard();
        setupFrame();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle(Misc.libraryTitle);
        frame.repaint();
    }

    private void setupContent() {
        //screen header
        JLabel library = new JLabel("Library");
        library.setForeground(Design.foreground);
        library.setFont(Design.buttonTextFont.deriveFont(24f));
        library.setBounds(25, 0, 100, 50);
        top.add(library);

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
        center.add(gif);

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
        JButton library = new JButton();
        library.setIcon(new ImageIcon(new ImageIcon("./res/library.png").getImage().getScaledInstance(90, 90, 0)));
        library.setBounds(55, 5, 90, 90);
        library.setBackground(Design.novelButtonBackground);
        library.addMouseListener(new ButtonStyle());
        library.setFocusable(false);
//        library.addActionListener(e -> refreshScreen(1));
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
        recommend.addActionListener(e -> refreshScreen(3));
        bot.add(recommend);

        highlight = new JLabel();
        highlight.setIcon(new ImageIcon("./res/highlight-2.png"));
        highlight.setBounds(50, 0, 100, 100);
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
            novelInfo = new NovelInfo(frame, content, novelPlaceHolder, this);
        } else if(location==1) {
        } else if(location==2) {
            if(browse==null) {
                browse = new Browse(frame, this, recommend);
                recommend.setBrowse(browse);
            } else {
                browse.getPanel().setVisible(true);
            }
        } else if(location==3) {
            if(recommend==null) {
                recommend = new Recommend(frame, this, browse);
            } else {
                recommend.updateRecommendation();
            }
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
                }
                gif.setVisible(false);
                content.setVisible(false);
            }
        };
    }

    public void updateLibrary() {
        center.removeAll();
        System.out.println(bookshelf);
        if(bookshelf.isEmpty()) {
            JLabel info = new JLabel("<html>"+Misc.emptyLibrary+"</html>");
            info.setForeground(Design.foreground);
            info.setFont(Design.novelTextFont);
            info.setBounds(125, 100, 350, 300);
            center.add(info);
            return;
        }
        for (int i=0;i<bookshelf.size();i++) {
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
            title.setFont(Design.buttonTextFont.deriveFont(24f));
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
            summary.setBounds(200, 140 + i*(novelHeight+50), 350, 100);
            summary.setBorder(BorderFactory.createLineBorder(Color.white));
            center.add(summary);

            //invisible but clickable button
            JButton click = new JButton();
            click.setOpaque(false);
            click.setContentAreaFilled(false);
            click.setFocusable(false);
            click.setBorder(BorderFactory.createLineBorder(Color.white));
            click.setBounds(50, 50 + i*(novelHeight+50), 500, 200);
            click.addActionListener(e -> refreshScreen(-1, novel));
            center.add(click);
        }
        center.setPreferredSize(new Dimension(Misc.WIDTH, 50+bookshelf.size()*(novelHeight+50)));
        center.add(gif);
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

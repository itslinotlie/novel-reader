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

public class Recommend {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot;

    private Library library;
    private Browse browse;

    private double scaleFactor = 3/5f;
    private int novelWidth, novelHeight, thickness = 4;
    private boolean firstOpen = true;

    private JLabel highlight, gif;

    private JScrollPane scroll;

    private SwingWorker worker = null; //allows "multi-threading"

//    public static void main(String[] args) {
//        Bookshelf one = new Bookshelf();
//        Bookshelf two = new Bookshelf();
//
//        Novel novel1 = new Novel("Overgeared1", "/overgeared.html");
//        Novel novel2 = new Novel("Overgeared2", "/overgeared.html");
//        Novel novel3 = new Novel("Overgeared3", "/overgeared.html");
//        Novel novel4 = new Novel("Overgeared4", "/overgeared.html");
//
//        one.add(novel1); one.add(novel3);
//        one.add(novel2); one.add(novel4);
//
//        new Recommend(new JFrame(), one);
//    }

    public Recommend(JFrame frame, Library library, Browse browse) {
        this.frame = frame;
        this.library = library;
        this.browse = browse;
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

        //shown when things are loaded
        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setVisible(false);
        gif.setBounds(250, (int)scroll.getViewport().getViewPosition().getY()+200, 100, 100);
        center.add(gif);
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

        //recommendation panel
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
//        center.setPreferredSize(new Dimension(Misc.WIDTH, 150+total*(novelHeight+50)));
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
        recommend.addActionListener(e -> refreshScreen(3));
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

    private void refreshScreen(int location, int random) {
        if(location==1) {
            library.getPanel().setVisible(true);
        } else if(location==2) {
            System.out.println(browse==null);
            if(browse==null) {
                browse = new Browse(frame, library, this);
            } else {
                browse.getPanel().setVisible(true);
            }
        } else if(location==3) {
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
                gif.setVisible(false);
                content.setVisible(false);
            }
        };
    }

    public JPanel getPanel() {
        return content;
    }
}

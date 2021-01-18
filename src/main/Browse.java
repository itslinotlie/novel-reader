package main;

import gui.NovelInfo;
import objects.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import tools.ButtonStyle;
import tools.Design;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
public class Browse {
    private JFrame frame;
    private JPanel content = new JPanel(), top, center, bot;

    private JScrollPane scroll;

    private static ArrayList<Novel> list;
    private static Novel novel;

    private static int novelPerPage = 20, total = 0, initial = 5, page = 0;
    private static double scaleFactor = 3/5f;

    private static int novelWidth, novelHeight, thickness = 4;;

    public Browse(JFrame frame) {
        this.frame = frame;
        setupPanel();
        setupContent();
        setupFrame();
    }
    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Design.WIDTH, Design.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle(String.format("Currently browsing titles"));
    }
    private void setupContent() {
        //JScrollPane to allow for continuous scrolling of browsing novels
        scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        content.add(scroll);

        displayChapter(0, list.size());

        JButton viewMore = new JButton("View More");
        viewMore.setFont(Design.buttonTextFont.deriveFont(24f));
        viewMore.setBounds(200, 50 + list.size()*(novelHeight+50), 200, 50);
        viewMore.setForeground(Design.screenBackground);
        viewMore.setBackground(Design.novelButtonBackground);
        viewMore.addMouseListener(new ButtonStyle());
        viewMore.addActionListener(e -> {
            viewMore.setVisible(false);
            int size = list.size();
            loadChapters();
            displayChapter(size, list.size());
            viewMore.setBounds(200, 50 + list.size()*(novelHeight+50), 200, 50);
            viewMore.setVisible(true);
        });
//        resume.addActionListener(e -> refreshScreen(novel.getLastReadChapter()));
        center.add(viewMore);


//        //JScrollPane to allow for continuous scrolling of browsing novels
//        scroll = new JScrollPane(center);
//        scroll.setBorder(BorderFactory.createEmptyBorder());
//        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scroll.getVerticalScrollBar().setUnitIncrement(15);
//        content.add(scroll);
    }

    private void setupPanel() {
        //need to initialize this value to set center panel height
        novel = list.get(0);
        novelWidth = (int)(novel.getThumbnailWidth()*scaleFactor);
        novelHeight = (int)(novel.getThumbnailHeight()*scaleFactor);

        //panel to display the mangas
        content.setBackground(Color.GREEN);
        content.setBounds(0, 0, Design.WIDTH, 1000);
        content.setLayout(new BorderLayout());
        frame.add(content);

        //browse screen
        top = new JPanel();
        top.setBackground(Design.screenBackground);
        top.setPreferredSize(new Dimension(Design.WIDTH, 50));
        top.setLayout(null);

        //browsable novels
        center = new JPanel();
        center.setBackground(Design.screenLightBackground);
        center.setPreferredSize(new Dimension(Design.WIDTH, 150+total*(novelHeight+50)));
        center.setLayout(null);

        //application dashboard
        bot = new JPanel();
        bot.setBackground(Design.screenBackground);
        bot.setPreferredSize(new Dimension(Design.WIDTH, 100));
        bot.setLayout(null);

        //panel containing everything
        content.add(top, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);
        content.add(bot, BorderLayout.SOUTH);
    }

    private void displayChapter(int begin, int end) {
        for(int i=begin;i<end;i++) {
            novel = list.get(i);

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
            click.setBorder(BorderFactory.createLineBorder(Color.white));
            click.setBounds(50, 50 + i*(novelHeight+50), 500, 200);
            int finalI = i;
            click.addActionListener(e -> refreshScreen(finalI));
            center.add(click);
        }

        center.setPreferredSize(new Dimension(Design.WIDTH, 150+total*(novelHeight+50)));

//        content.remove(scroll);
//
//        scroll = new JScrollPane(center);
//        scroll.setBorder(BorderFactory.createEmptyBorder());
//        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
//        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
//        scroll.getVerticalScrollBar().setUnitIncrement(15);
//        content.add(scroll);

//        scroll.setPreferredSize(new Dimension(Design.WIDTH, 150+initial*(novelHeight+50)));
//        scroll = new JScrollPane(center);
////        scroll.setBorder(BorderFactory.createEmptyBorder());
////        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
////        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
////        scroll.getVerticalScrollBar().setUnitIncrement(15);
//        content.add(scroll);
    }

    private void refreshScreen(int targetChapter) {
        content.setVisible(false);
        System.out.println(targetChapter+"\n"+list.get(targetChapter));
        new NovelInfo(frame, content, list.get(targetChapter));
    }

    private String limit(String text) {
        String arr[] = text.split("[ ]"), ret="";
        for(int i=0;i<Math.min(25, arr.length);i++) {
            if(arr[i]=="") continue;
            ret+=arr[i].trim()+" ";
        }
        return ret+"...";
    }

    private static void loadChapters() {
        total+=initial;
        String url = "https://novelfull.com/most-popular?page="+(++page);
        int count = initial;
        try {
            while(count>0) {
                Document doc = Jsoup.connect(url).get();
                //all the <div> with title information are in a class called "col-xs-7"
                //to call a class from a div, the dot is used
                for(Element row:doc.select("div.col-xs-7")) {
                    if(count<=0) break;
                    //finds the <a> tag that is contained in a <h3> tag
                    String novelLink = row.select("h3 > a").attr("href");
                    String novelName = row.select("h3 > a").text();
                    list.add(new Novel(novelName, novelLink));
                    System.out.println(list.get(list.size()-1));
                    count--;
                }
                url = "https://novelfull.com/index.php/most-popular?page="+(++page);
            }
        } catch (IOException e) {
            System.out.println("Problem loading chapters in browse screen");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        list = new ArrayList();
        loadChapters();
        JFrame frame = new JFrame();
        new Browse(frame);
    }
}

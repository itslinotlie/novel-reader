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

    private static ArrayList<Novel> list;
    private static Novel novel;

    private static int novelPerPage = 20, initial = 20, page = 1;
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
        for(int i=0;i<list.size();i++) {
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
            author.setFont(Design.buttonTextFont);
            author.setBounds(200, 100 + i*(novelHeight+50), 350, 50);
            center.add(author);
        }

        JButton viewMore = new JButton("View More");
        viewMore.setFont(Design.buttonTextFont.deriveFont(24f));
        viewMore.setBounds(200, 50 + list.size()*(novelHeight+50), 200, 50);
        viewMore.setForeground(Design.screenBackground);
        viewMore.setBackground(Design.novelButtonBackground);
        viewMore.addMouseListener(new ButtonStyle());
//        resume.addActionListener(e -> refreshScreen(novel.getLastReadChapter()));
        center.add(viewMore);


        //JScrollPane to allow for continuous scrolling of browsing novels
        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        content.add(scroll);
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
        center.setPreferredSize(new Dimension(Design.WIDTH, 150+initial*(novelHeight+50)));
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

    public static void main(String[] args) {
        String url = "https://novelfull.com/most-popular?page=1";

        list = new ArrayList();
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
                    count--;
                }
                url = "https://novelfull.com/index.php/most-popular?page="+(++page);
            }
            for(Novel novel:list) {
                System.out.println(novel+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame();
        new Browse(frame);
    }
}

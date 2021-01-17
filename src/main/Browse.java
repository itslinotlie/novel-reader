package main;

import gui.NovelInfo;
import objects.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    private Novel novel;

    private int novelPerPage = 10;
    private double scaleFactor = 3/5f;

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
            JLabel icon = new JLabel();
            icon.setLayout(null);
            int novelWidth = (int)(novel.getThumbnailWidth()*scaleFactor);
            int novelHeight = (int)(novel.getThumbnailHeight()*scaleFactor);
            System.out.printf("%d | %d %d\n", i, novelWidth, novelHeight);
            int thickness = 4;
            icon.setIcon(new ImageIcon(novel.getThumbnail().getImage().getScaledInstance(novelWidth, novelHeight, 0)));
            icon.setBounds(50, 50 + i*(novelHeight+50), novelWidth+2*thickness, novelHeight+2*thickness);
            center.add(icon);
            icon.setBorder(BorderFactory.createLineBorder(Design.screenPop, thickness));
        }

        //JScrollPane to allow for continuous scrolling of browsing novels
        JScrollPane scroll = new JScrollPane(center);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(5);
        content.add(scroll);
    }

    private void setupPanel() {
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
        center.setLayout(null);
//        center.setSize(Design.WIDTH, 1000);
//        center.setMinimumSize(new Dimension(Design.WIDTH, 1000));
        center.setPreferredSize(new Dimension(Design.WIDTH, 5000));

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

        int count = 10, page = 1;
        list = new ArrayList();
        try {
            while(count>0) {
                Document doc = Jsoup.connect(url).get();
                //all the <div> with title information are in a class called "col-xs-7"
                //to call a class from a div, the dot is used
                for(Element row:doc.select("div.col-xs-7")) {
                    if(count<0) break;
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

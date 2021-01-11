package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.imageio.ImageIO;

public class MangaTest extends JFrame {
    static String information = "";
    static int WIDTH=600, HEIGHT = 800;
    public MangaTest() {
        setupLabel();
        setupFrame();
    }
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }
    private void setupLabel() {
//        JTextArea text = new JTextArea(information);
//        text.setWrapStyleWord(true);
//        text.setLineWrap(true);
//        text.setEditable(false);
//        text.setFocusable(false);
//        text.setOpaque(false);
        Image img = null;
        try {
            img = ImageIO.read(new URL("https://fan-official.lastation.us/manga/Solo-Leveling/0000-001.png"));
        } catch(Exception e) {
            e.printStackTrace();
        }
        ImageIcon icon = new ImageIcon(img);
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();
        if (icon.getIconWidth() > WIDTH) {
            nw = WIDTH-50;
            nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        icon = new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
        JLabel text = new JLabel(icon);
        JScrollPane scroll = new JScrollPane(text);
        scroll.getVerticalScrollBar().setUnitIncrement(15);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
    }
    public static void main(String[] args) {
//        String url = "https://mangasee123.com/read-online/Solo-Leveling-chapter-0.html";
//        try {
//            Document doc = Jsoup.connect(url).get();
//            System.out.println("Title: "+doc.title());
//            System.out.println(doc.outerHtml());
//            for(Element row:doc.select("img")) { //prints all the text with a <p> tag
//                System.out.println(row.absUrl("scr")+" | "+row.attr("src"));
////                information+=row.text()+"\n";
////                System.out.println(row.text());
//            }
//        } catch(IOException e) {
//            e.printStackTrace();
//        }
        new MangaTest();
    }
}

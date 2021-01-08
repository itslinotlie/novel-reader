package main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;

public class LightNovelTest extends JFrame {
    static String information = "";
    static int WIDTH=600, HEIGHT = 800;
    public LightNovelTest() {
        setupLabel();
        setupFrame();
    }
    private void setupFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setVisible(true);
    }
    private void setupLabel() {
        JTextArea text = new JTextArea(information);
        text.setWrapStyleWord(true);
        text.setLineWrap(true);
        text.setEditable(false);
        text.setFocusable(false);
        text.setOpaque(false);

        JScrollPane scroll = new JScrollPane(text);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scroll);
    }
    public static void main(String[] args) {
        String url = "https://novelfull.com/overgeared/chapter-1019.html";
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("Title: "+doc.title());
//            System.out.println(doc.outerHtml());
            for(Element row:doc.getElementsByTag("p")) { //prints all the text with a <p> tag
                information+=row.text()+"\n";
//                System.out.println(row.text());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
        new LightNovelTest();
    }
}

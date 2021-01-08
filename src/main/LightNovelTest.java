package main;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;

public class LightNovelTest extends JFrame {
    static String information = "";
    static String summary = "";
    static String description = "";
    static int WIDTH=600, HEIGHT = 800;
    static int maxChap = 0, minChap = 0x3f3f3f3f;
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
        String website = "https://novelfull.com";
        String title[] = {"/overgeared", "/the-kings-avatar"};
        String novel = title[0];
        String chapter = "/chapter-1019";
        String thumbnail = "";
        String html = "";

        String url = website+novel+".html";
        try { //novel information
            Document doc = Jsoup.connect(url).get();
            html = doc.outerHtml();
            thumbnail = doc.select("div.book").first().select("img").attr("src");
                    //doc.select("book").select("img").attr("src"));


            summary+=doc.select(".desc-text").text(); //novel summary
            description+=doc.select(".info").text(); //author, genre, source, status
            for(Element row:doc.getElementsByTag("li")) {
                String tmp = row.select("a").attr("title"); //tmp is the chapter name
                if(!tmp.startsWith("Chapter")) continue;

                maxChap = Math.max(maxChap, Integer.parseInt(tmp.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
                minChap = Math.min(minChap, Integer.parseInt(tmp.replaceAll("[^0-9 ]", "").split("[ ]")[1]));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        url = website+novel+chapter+".html";
        try { //chapter web-scraping
            Document doc = Jsoup.connect(url).get();
            for(Element row:doc.getElementsByTag("p")) { //this is the light novel text
                information+=row.text()+"\n";
            }
        } catch(IOException e) {
            e.printStackTrace();
        }

        String info[] = description.split("\\s+(?=(Genre:|Source:|Status:))");
        for(int i=0;i<info.length;i++) {
            if(info[i].startsWith("Author")) {
                String tmp = info[i].replace("Author:", "");
                tmp = tmp.replace(",", "");
                System.out.println("Author: "+tmp);
            } else if(info[i].startsWith("Genre")) {
                String tmp[] = info[i].replace("Genre:", "").split("[,]");
                System.out.println("Genre(s) include: "+ Arrays.toString(tmp));
            }
        }
        System.out.println("Novel Thumbnail: "+website+thumbnail);
        System.out.printf("Summary: %s\n", summary);
        System.out.printf("Max chap: %d | Min chap: %d\n", maxChap, minChap);
//        System.out.println(html);
//        new LightNovelTest();
    }
}

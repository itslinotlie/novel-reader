package main;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class test {
    public static void main(String[] args) {
        String url = "https://novelfull.com/overgeared/chapter-1019.html";
        try {
            Document doc = Jsoup.connect(url).get();
            System.out.println("Title: "+doc.title());
//            System.out.println(doc.outerHtml());
            for(Element row:doc.getElementsByTag("p")) { //prints all the text with a <p> tag
                System.out.println(row.text());
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

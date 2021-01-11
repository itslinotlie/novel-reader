package tools;

import objects.Novel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.Arrays;

public class WebScraping {
    public static String errorMessage = ">>>>>>>>>> -1 <<<<<<<<<<";

    //website in the form of | "https://novelfull.com"
    //novel in the form of | overgeared
    //does not contain .html ending
    public static String getChapterName(Novel novel, int targetChapter) {
        //this is the way novelfull.com queries/displays pages
        String url = novel.getWebsite() + "/" + novel.getNovelName() + ".html?page=" + ((targetChapter + 49) / 50) + "&per-page=50";
        String fullUrl = "";
        boolean found = false;

        try {
            //connecting to the url and getting html
            Document doc = Jsoup.connect(url).get();
            //chapter titles are in the ".list-chapter" div and the element is in a <a> tag
            for (Element row : doc.select(".list-chapter").select("a")) {
                //within the <a> tag, the attribute "title" is where the chapter name is
                fullUrl = row.attr("title");
                //massaging the information to be url friendly
                fullUrl = fullUrl.trim().replaceAll("[^a-zA-Z0-9 ]", ""); //replaces everything except for Alphanumeric + space to nothing
                fullUrl = fullUrl.replaceAll(" +", "-"); //replaces all consecutive spaces with a dash

                int currentChapter = Integer.parseInt(fullUrl.split("-")[1]); //url is in the form of Chapter x, where x is chapter #
                if (currentChapter==targetChapter) {
                    found = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter name");
        }
        return found? fullUrl:errorMessage;
    }

    public static String getChapterContent(Novel novel, int chapter) {
        String url = novel.getWebsite() + "/" + novel.getNovelName() + "/" + getChapterName(novel, chapter) + ".html";
        String content = "";

        try {
            Document doc = Jsoup.connect(url).get();
            for(Element row:doc.getElementsByTag("p")) { //this is the light novel text
                if(row.text().equals("")) continue;
                content += row.text()+"\n\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter content");
        }
        return content;
    }
}

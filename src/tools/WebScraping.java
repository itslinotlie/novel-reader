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
        String chapterName = "", chapterUrl="";
        boolean found = false;

        try {
            //due to chapter being missing, the logic gets messed up
            //so by going +/- 1 chapter, the target chapter should be found
            for(int i=-50;i<=50;i+=50) {
                if (found) break;
                url = novel.getWebsite() + "/" + novel.getNovelName() + ".html?page=" + ((targetChapter + 49 + i) / 50) + "&per-page=50";
                //connecting to the url and getting html
                Document doc = Jsoup.connect(url).get();
                //chapter titles are in the ".list-chapter" div and the element is in a <a> tag
                for (Element row : doc.select(".list-chapter").select("a")) {
                    chapterUrl = row.attr("href");
                    //within the <a> tag, the attribute "title" is where the chapter name is
                    chapterName = row.attr("title");
                    //massaging the information to be url friendly
                    chapterName = chapterName.trim().replaceAll("[^a-zA-Z0-9 ]", ""); //replaces everything except for Alphanumeric + space to nothing
                    chapterName = chapterName.replaceAll(" +", "-"); //replaces all consecutive spaces with a dash

                    int currentChapter = -1;
                    if(isInteger(chapterName.split("-")[1])) {
                        currentChapter = Integer.parseInt(chapterName.split("-")[1]); //url is in the form of Chapter x, where x is chapter #
                    }
                    if(chapterName.split("-")[1].contains(Integer.toString(targetChapter))
                        || currentChapter==targetChapter) {
                        found = true;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter name");
        }
        return found? chapterUrl:errorMessage;
    }

    public static String getChapterContent(Novel novel, int chapter) {
        String url = novel.getWebsite() + getChapterName(novel, chapter);
        String content = "";

        try {
            Document doc = Jsoup.connect(url).get();
            //the light novel content are all in the <p> tag
            for(Element row:doc.getElementsByTag("p")) {
                if(row.text().equals("")) continue;
                content += row.text()+"\n\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter content");
        }
        return content;
    }

    //some chapters such as https://novelfull.com/the-kings-avatar/chapter-1729end-final-update-thoughts-on-completion.html
    //have letters beside numbers, need to make sure parsed string is an integer or else exception occurs
    private static boolean isInteger(String str) {
        if(str==null || str.length()==0) {
            return false;
        }
        if(str.length()==1 && str.charAt(0)=='-') {
            return false;
        }
        for(int i=1;i<str.length();i++) {
            char c = str.charAt(i);
            if(c<'0' || c>'9') {
                return false;
            }
        }
        return true;
    }
}

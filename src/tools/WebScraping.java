package tools;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;
import java.util.Arrays;

public class WebScraping {
    public static String errorMessage = "-1";

    //website in the form of | "https://novelfull.com"
    //novel in the form of | overgeared
    //does not contain .html ending
    public static String getChapterName(String website, String novel, int targetChapter) {
        //this is the way novelfull.com queries/displays pages
        String url = website + "/" + novel + ".html?page=" + ((targetChapter + 49) / 50) + "&per-page=50";
        String fullUrl = errorMessage;
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

    public static String getAuthorName(String website, String novel) {
        String url = website + "/" + novel + ".html";
        String description = "", descriptionInfo[] = null;
        String authorName = errorMessage;

        try {
            Document doc = Jsoup.connect(url).get();
            //since there is only one .info div, there is no need for a for-each loop
            description = doc.select(".info").text();
            descriptionInfo = description.split("\\s+(?=(Genre|Source|Status))"); //splits by the keywords: Genre, Source, and Status

            for(int i=0;i<descriptionInfo.length;i++) {
                if (descriptionInfo[i].startsWith("Author")) {
//                    authorName = descriptionInfo[i].replaceAll("\\bAuthor:\\b", "");
                    authorName = descriptionInfo[i].replace("Author:", "");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting author name");
        }
        return authorName;
    }

    public static String[] getGenre(String website, String novel) {
        String url = website + "/" + novel + ".html";
        String description = "", descriptionInfo[] = null;
        String genreList[] = null;

        try {
            Document doc = Jsoup.connect(url).get();
            //since there is only one .info div, there is no need for a for-each loop
            description = doc.select(".info").text();
            descriptionInfo = description.split("\\s+(?=(Genre|Source|Status))"); //splits by the keywords: Genre, Source, and Status

            for(int i=0;i<descriptionInfo.length;i++) {
                if (descriptionInfo[i].startsWith("Genre")) {
                    genreList = descriptionInfo[i].replace("Genre:", "").split("[,]");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting genre(s)");
        }
        return genreList;
    }

    public static String getSummary(String website, String novel) {
        String url = website + "/" + novel + ".html";
        String summary = "";

        try {
            Document doc = Jsoup.connect(url).get();
            //since there is only one .desc-text, there is no need for a for-each loop
            summary = doc.select(".desc-text").text();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting summary");
        }
        return summary;
    }

    //does not contain website root
    public static String getThumbnail(String website, String novel) {
        String url = website + "/" + novel + ".html";
        String thumbnail = "";

        try {
            Document doc = Jsoup.connect(url).get();
            //since there is only one .div.book, there is no need for a for-each loop
            thumbnail = doc.select("div.book").first().select("img").attr("src"); //selects the <img> tag and gets the src attribute
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting thumbnail");
        }
        return thumbnail;
    }

    public static int[] getChapterRange(String website, String novel) {
        String url = website + "/" + novel + ".html";
        int minChap = 0x3f3f3f3f, maxChap = -1;


        try {
            Document doc = Jsoup.connect(url).get();
            //searching for elements with the <li> tag
            for(Element row:doc.getElementsByTag("li")) {
                String chapterName = row.select("a").attr("title");
                if(!chapterName.startsWith("Chapter")) continue; //first row is hidden/blank, need to filter it out
                //ReGeX to replace everything but numbers and spaces (spaces because sometimes numbers
                //are in the chapter name and it needs to be separated to find the chapter number)
                maxChap = Math.max(maxChap, Integer.parseInt(chapterName.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
                minChap = Math.min(minChap, Integer.parseInt(chapterName.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter range");
        }
        return new int[]{minChap, maxChap};
    }

    public static String getChapterContent(String website, String novel, int chapter) {
        String url = website + "/" + novel + "/" + getChapterName(website, novel, chapter) + ".html";
        String content = "";
        System.out.println(url);

        try {
            Document doc = Jsoup.connect(url).get();
            for(Element row:doc.getElementsByTag("p")) { //this is the light novel text
                content += row.text()+"\n";
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter content");
        }
        return content;
    }
}

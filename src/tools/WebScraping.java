package tools;

import objects.Novel;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;

import java.io.IOException;

/**
 * The heart of the program. This file is used to scrape information from websites,
 * keeping the front-end portion of the code void of back-end related tasks. This class will scrape
 * for the following tasks: the chapter url, chapter name, and chapter contents for any given novel,
 * regardless if it is bookmarked in the library or not. However, this process takes a long time, resulting
 * in a very long load time ):
 */
public class WebScraping {
    public static String errorMessage = "THIS CHAPTER/NOVEL IS FAULTY";

    //returns the url of a given chapter, if this is not possible the error message is displayed
    public static String getChapterUrl(Novel novel, int targetChapter) {
        String url = "";
        String chapterName = "", chapterUrl="";
        boolean found = false;

        try {
            //due to chapter(s) being missing, direct math logic gets messed up.
            //To avoid this, space of +/- 50 chapters (1 page) will be given and the target chapter should be found
            for(int i=-50;i<=50;i+=50) {
                if (found) break;
                //this is the format novelfull.com queries/displays pages
                url = novel.getWebsite() + novel.getNovelLink() +"?page=" + ((targetChapter + 49 + i) / 50) + "&per-page=50";
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

                    //some chapters have text adjacent to the numbers, breaking the ReGeX formatting
                    //this is why there is an additional conditional statement to check the chapter
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

    //pretty much the same code as getChapterUrl except the
    //original un-tampered chapter name is returned here
    public static String getChapterName(Novel novel, int targetChapter) {
        String url = "";
        String chapterName = "";
        boolean found = false;

        try {
            //due to chapter(s) being missing, direct math logic gets messed up.
            //To avoid this, space of +/- 50 chapters (1 page) will be given and the target chapter should be found
            for(int i=-50;i<=50;i+=50) {
                if (found) break;
                //this is the way novelfull.com queries/displays pages
                url = novel.getWebsite() + novel.getNovelLink() +"?page=" + ((targetChapter + 49 + i) / 50) + "&per-page=50";
                //connecting to the url and getting html
                Document doc = Jsoup.connect(url).get();
                //chapter titles are in the ".list-chapter" div and the element is in a <a> tag
                for (Element row : doc.select(".list-chapter").select("a")) {
                    //within the <a> tag, the attribute "title" is where the chapter name is
                    chapterName = row.attr("title");
                    //massaging the information to be url friendly
                    chapterName = chapterName.trim().replaceAll("[^a-zA-Z0-9 ]", ""); //replaces everything except for Alphanumeric + space to nothing
                    chapterName = chapterName.replaceAll(" +", "-"); //replaces all consecutive spaces with a dash

                    //some chapters have text adjacent to the numbers, breaking the ReGeX formatting
                    //this is why there is an additional conditional statement to check the chapter
                    int currentChapter = -1;
                    if(isInteger(chapterName.split("-")[1])) {
                        currentChapter = Integer.parseInt(chapterName.split("-")[1]); //url is in the form of Chapter x, where x is chapter #
                    }
                    if(chapterName.split("-")[1].contains(Integer.toString(targetChapter))
                            || currentChapter==targetChapter) {
                        found = true;
                        chapterName = row.attr("title");;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Had issue getting chapter name");
        }
        return found? chapterName:errorMessage;
    }

    //returns the text from a given novel and given chapter number
    public static String getChapterContent(Novel novel, int targetChapter) {
        String url = novel.getWebsite() + getChapterUrl(novel, targetChapter);
        String content = "";

        //very coincidentally, the king's avatar chapter 1234 does not physically exist, which
        //is why an additional sentence was included (the hard coding)
        if(url.contains(errorMessage)) {
            content = String.format("The novel \"%s\" chapter %d that you requested for is currently unavailable. " +
                "Reasons can include the chapter does not exist (i.e. the-kings-avatar chapter 1234 --> # is not hardcoded :/) or there was an error " +
                "in parsing the information. Thank you for your understanding.", novel.getNovelName(), targetChapter);
        } else {
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
        }
        return content;
    }

    //some chapters such as https://novelfull.com/the-kings-avatar/chapter-1729end-final-update-thoughts-on-completion.html
    //have letters beside numbers. Need to make sure the parsed string is an integer or else an exception occurs
    public static boolean isInteger(String str) {
        if(str==null || str.length()==0
            || (str.length()==1 && str.charAt(0)=='-')) {
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

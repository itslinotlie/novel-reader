package objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.Arrays;

public class Novel {
    private String website, novelName;
    private String author, summary, thumbnail;
    private String[] genreList;
    private int[] chapterRange;

    public Novel(String website, String novelName) {
        this.website = website;
        this.novelName = novelName;
        loadInformation();
    }

    private void loadInformation() {
        String url = website + "/" + novelName + ".html";
        String description = "", descriptionInfo[] = null;
        int minChap = 0x3f3f3f3f, maxChap = -1;

        try {
            //connects to the url and gets the html
            Document doc = Jsoup.connect(url).get();
            //there are only one of these divs, so no need for a for-each loop
            summary = doc.select(".desc-text").text();
            thumbnail = website + doc.select("div.book").first().select("img").attr("src"); //selects the <img> tag in a div called book and gets the src attribute
            description = doc.select(".info").text();
            descriptionInfo = description.split("\\s+(?=(Genre|Source|Status))"); //splits description by the keywords: Genre, Source, and Status

            //parsing information in the novel description
            for(int i=0;i<descriptionInfo.length;i++) {
                if (descriptionInfo[i].startsWith("Author")) {
                    author = descriptionInfo[i].replace("Author:", "");
                }
                else if (descriptionInfo[i].startsWith("Genre")) {
                    genreList = descriptionInfo[i].replace("Genre:", "").split("[,]");
                }
            }

            //parsing information to get chapter range goes through
            //all the elements with a <li> tag on the front page
            for(Element row:doc.getElementsByTag("li")) {
                String chapterName = row.select("a").attr("title");
                if(!chapterName.startsWith("Chapter")) continue; //first row is hidden/blank, needs to be filtered out
                //ReGeX to replace everything but numbers and spaces (spaces because sometimes numbers
                //are in the chapter name and it needs to be separated to find the chapter number)
                maxChap = Math.max(maxChap, Integer.parseInt(chapterName.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
                minChap = Math.min(minChap, Integer.parseInt(chapterName.replaceAll("[^0-9 ]", "").split("[ ]")[1]));
            }
            chapterRange = new int[]{minChap, maxChap};
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Problem loading novel information for "+novelName);
        }
    }

    @Override
    public String toString() {
        String ret = "";
        ret+=String.format("Novel name: %s | Website name: %s\n", novelName, website);
        ret+=String.format("Author name: %s\n", author);
        ret+=String.format("Genre(s): %s\n", Arrays.toString(genreList));
        ret+=String.format("Chapter range: %s\n", Arrays.toString(chapterRange));
        ret+=String.format("Thumbnail: %s\n", thumbnail);
        ret+=String.format("Summary: %s\n", summary);
        return ret;
    }

    public String getNovelName() {
        return novelName;
    }

    public String getWebsite() {
        return website;
    }

    public String[] getGenreList() {
        return genreList;
    }

    public int[] getChapterRange() {
        return chapterRange;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getSummary() {
        return summary;
    }
}

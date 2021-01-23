package objects;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

/**
 * This class represents a novel from novelfull.com. It includes all the information
 * unique to said novel, such as the novel name, author, summary, rating, thumbnail image, etc.
 * There are two ways to create a novel object, through loading an existing novel or scraping
 * the novel information given its novel link (i.e. /the-kings-avatar.html)
 */
public class Novel {
    private final String website = "https://novelfull.com";
    private String novelLink, novelName;
    private String author, summary, thumbnailLink, rating;
    private String genreList[];
    private int lastReadChapter, chapterRange[];
    private ImageIcon thumbnail;

    //blank constructor for loading novels rather than web scraping novels from website
    public Novel() {

    }
    //constructor to web-scrape novel information given its novelLink
    public Novel(String novelName, String novelLink) {
        this.novelName = novelName;
        this.novelLink = novelLink;
        lastReadChapter = 1;
        loadInformation();
    }
    //loads all the information unique to the novel,
    //including parameters discussed above
    private void loadInformation() {
        String url = website + novelLink;
        String description = "", descriptionInfo[] = null;
        int minChap = 1, maxChap = 6;

        try {
            //connects to the url and gets the html
            Document doc = Jsoup.connect(url).get();
            rating = doc.select("em").text();
            //there are only one of these divs, so no need for a for-each loop
            summary = doc.select(".desc-text").text();
            thumbnailLink = website + doc.select("div.book").first().select("img").attr("src"); //selects the <img> tag in a div called book and gets the src attribute
            thumbnail = new ImageIcon(ImageIO.read(new URL(thumbnailLink)));
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
            for(Element row:doc.getElementsByTag("span")) {
                String chapterName = row.text();
                if(!chapterName.startsWith("Chapter")) continue; //first row is hidden/blank, needs to be filtered out
                //ReGeX to replace everything but numbers and spaces (spaces because sometimes numbers
                //are in the chapter name and it needs to be separated to find the chapter number)
                chapterName = chapterName.replace("-", " ");
                String arr[] = chapterName.replaceAll("[^0-9 ]", "").split("[ ]");
                maxChap = Math.max(maxChap, Integer.parseInt(arr[Math.min(arr.length-1, 1)]));
                minChap = Math.min(minChap, Integer.parseInt(arr[Math.min(arr.length-1, 1)]));
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
        ret+=String.format("Novel name: %s | Novel Link: %s\n", novelName, novelLink);
        ret+=String.format("Author name: %s\n", author);
        ret+=String.format("Genre(s): %s\n", Arrays.toString(genreList));
        ret+=String.format("Chapter range: %s\n", Arrays.toString(chapterRange));
        ret+=String.format("Last read chapter: %d\n", lastReadChapter);
        ret+=String.format("Thumbnail: %s\n", thumbnailLink);
        ret+=String.format("Summary: %s\n", summary);
        ret+=String.format("Rating: %s\n", rating);
        return ret;
    }

    //need a equals to remove Novel objects in the bookshelf object
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Novel) {
            Novel compare = (Novel) obj;
            if(this.novelName.equals(compare.getNovelName())) return true;
        }
        return false;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getLastReadChapter() {
        return lastReadChapter;
    }

    public void setLastReadChapter(int chapter) {
        if(chapter<chapterRange[0] || chapter>chapterRange[1]) {
            System.out.println(">>>>>>>>>> Chapter out of bounds <<<<<<<<<<");
            return;
        }
        lastReadChapter = chapter;
    }

    public String getNovelName() {
        return novelName;
    }

    public void setNovelName(String novelName) {
        this.novelName = novelName;
    }

    public String getNovelLink() {
        return novelLink;
    }

    public void setNovelLink(String novelLink) {
        this.novelLink = novelLink;
    }

    public String getWebsite() {
        return website;
    }

    public String[] getGenreList() {
        return genreList;
    }

    public void setGenreList(String[] genreList) {
        this.genreList = genreList;
    }

    public boolean hasGenre(String genre) {
        for(int i=0;i<genreList.length;i++) {
            if(genre.equals(genreList[i])) return true;
        } return false;
    }

    public int[] getChapterRange() {
        return chapterRange;
    }

    public void setChapterRange(int[] chapterRange) {
        this.chapterRange = chapterRange;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public ImageIcon getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ImageIcon thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getThumbnailHeight() {
        return thumbnail.getIconHeight();
    }

    public int getThumbnailWidth() {
        return thumbnail.getIconWidth();
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}

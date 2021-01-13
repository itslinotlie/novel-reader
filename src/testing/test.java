package testing;

import main.LightNovelTest;
import objects.Novel;
import tools.WebScraping;

import java.util.Scanner;


//delete later
public class test {
    static LightNovelTest x;

    public static void main(String[] args) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);
        System.out.println(novel);

        x = new LightNovelTest(novel);
        x.refreshScreen();

//        for(int i=1;i<=novel.getChapterRange()[1];i++) {
//            System.out.println(WebScraping.getChapterName(novel, novel.getLastReadChapter()+1));
//            novel.setLastReadChapter(i);
//
//            x.refreshScreen();
////            try {
////                Thread.sleep(1);
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
//        }
        Scanner in = new Scanner(System.in);
        int chapterCount = 1;
        while(chapterCount!=-1) {
            System.out.println(WebScraping.getChapterUrl(novel, novel.getLastReadChapter()));
            chapterCount = in.nextInt();
            novel.setLastReadChapter(chapterCount);
            x.refreshScreen();
        }
    }
}

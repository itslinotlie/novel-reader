package tools;

import objects.Novel;

public class Misc {
    //dimensions
    public static final int WIDTH = 600, HEIGHT = 800;
    //novel for measurement purposes
    public static Novel novel = new Novel("Overgeared", "/overgeared.html");
    //JFrame titles
    public static String libraryTitle = "Your Personal Library";
    public static String browseTitle = "Currently browsing titles";
    public static String recommendTitle = "Some Titles You May Enjoy";
    public static String novelTitle(Novel novel) {
        return String.format("Current novel: %s", novel.getNovelName());
    }
    public static String notEnoughTitles = "Your library does not contain sufficient " +
            "amounts of novels for an accurate recommendation. Please have a minimum of " +
            "5 novels in your library with at least 3 unique genres in the library. " +
            "Thank you for your understanding.";
    public static String emptyLibrary = "Your library is empty. Chances are that you just " +
            "opened up this application for the first time. Click on the browse icon (window) down " +
            "below to browse for titles. Find more information on a novel by clicking on it and add it " +
            "to your library if you want the novel reading history to be tracked. If you ever get confused, " +
            "click on the question mark icon in the top right of your screen for more information. ";
    public static String libraryInfo = "This is your library, the place where you can view your bookmarked novels. " +
            "Bookmarked novels will have their reading history tracked, which means that you will be able to resume " +
            "from your last read chapter if you click the \"Resume\" button in the novel information screen. To get to the " +
            "novel information screen, you can click on the novel thumbnail from either your library, browse screen, or recommendation " +
            "screen. To browse titles, click on the browse icon (window) down below. To view recommended novels, click on the recommend" +
            " icon (thumbs-up) down below as well.";
//    public static String libraryInfo = "This is your library. Add novels from the browse screen by clicking on" +
//            " the browse icon (window) down below. Click on the novels you are interested in and click the in library" +
//            " button and the button will turn green. Click it again and you will remove it from your library. Novels in " +
//            "the library will have their reading history tracked, which you can jump to by clicking the resume button in the" +
//            " novel information screen. If you want some novel recommendations, click the recommendation icon (thumbs up) below " +
//            "to get recommendations based on the novels in your library. ";
}

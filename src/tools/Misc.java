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
}

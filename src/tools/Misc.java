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
}

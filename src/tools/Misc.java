package tools;

import objects.Novel;

/**
 * Similar to the src/tools/Design.java class, except for miscellaneous purposes.
 * Included are the dimensions of the project, novel for thumbnail dimensions,
 * JFrame title messages, user messages, and help screen messages
 */
public class Misc {
    //dimensions of the project
    public static final int WIDTH = 600, HEIGHT = 800;

    //novel for thumbnail measurement purposes
    public static Novel novel = new Novel("Overgeared", "/overgeared.html");

    //JFrame titles messages
    public static String libraryTitle = "Your Personal Library";
    public static String browseTitle = "Currently browsing titles";
    public static String recommendTitle = "Some Titles You May Enjoy";
    public static String novelTitle(Novel novel) {
        return String.format("Current novel: %s", novel.getNovelName());
    }

    //messages to display to user in unique situations (empty library, or not enough novels for recommendation)
    public static String notEnoughTitles = "Your library does not contain sufficient " +
        "amounts of novels for an accurate recommendation. Please have a minimum of " +
        "5 novels in your library with at least 3 unique genres in the library. " +
        "Thank you for your understanding.";
    public static String emptyLibrary = "Your library is empty. Chances are that you just " +
        "opened up this application for the first time. Click on the browse icon (window) down " +
        "below to browse for titles. Find more information on a novel by clicking on it and add it " +
        "to your library if you want the novel reading history to be tracked. If you ever get confused, " +
        "click on the question mark icon in the top right of your screen for more information. ";

    //help screen messages
    public static String libraryInfo = "This is your library, the place where you can view your bookmarked novels. " +
        "Bookmarked novels will have their reading history tracked, which means that you will be able to resume " +
        "from your last read chapter if you click the \"Resume\" button in the novel information screen. To get to the " +
        "novel information screen, you can click on the novel thumbnail from either your library, browse screen, or recommendation " +
        "screen. To browse titles, click on the browse icon (window) down below. To view recommended novels based " +
        "on your selection, click on the recommend icon (thumbs-up) down below as well.";
    public static String browseInfo = "This is where you can browse for new novels. By clicking on the \"View More\" button, " +
        "you will be able to load more novels. These novels are the hottest novels on novelfull.com, the source of these novels. " +
        "If a novel's thumbnail, title, author, or summary intrigues you, click on the novel thumbnail and the novel information will appear." +
        " If you want to test read it before you add it to your library, you can read away. Just note that progress will not be tracked until " +
        "the novel is added to your library. To go back to your library, click on the book icon (stack of books) down below. To view " +
        "recommended novels based on your selection, click on the recommend icon (thumbs-up) down below as well.";
    public static String recommendInfo = "This is where novel recommendations are made based on the novels in your library. " +
        "For the recommendation to be useful, you are required to have a minimum of 5 novels in your library with a total of at " +
        "least 3 unique genres. Recommended novels will never exist in your current library. The algorithm finds the most commonly " +
        "read genres and web-scrapes for the hottest novels related to that category. Everytime the recommended page is loaded, a new " +
        "set of recommendations are made. To go back to your library, click on the book icon (stack of books) down below. To browse titles " +
        "click on the browse icon (window) down below.";
}

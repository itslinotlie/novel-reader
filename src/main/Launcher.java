package main;

import gui.StartScreen;

//IMPORTANT MESSAGE: you need to add the lib/jsoup-1.13.1.jar file
//to your Java build path in order for the program to work.
//General how to (depends on ide but the ideas there):
//Select Build Path > Configure Build Path > Add external jar > add the jsoup jar > apply and voila

/**
 * Programmer: Michael Li
 * Date of submission: Jan 22, 2021
 * Course code: ICS4U-51
 * Instructor name: Mr. Fernandes
 *
 * Project title: Novel Reader
 * Project description: Read and store information on your favourite web novels.
 * With web-scraping technology, you don't have to store the contents of the novels on your computer,
 * the application scrapes the website for the information. With a front-end design inspired by
 * Discord and novelfull.com, you can read novels without criticizing the looks of the program. Browse through
 * novels in the browse screen and add the novels that interest you by clicking add to library. This will allow
 * your last read chapter to be tracked, and your novels will appear in your library everytime you re-open the
 * application. If you want some new novels, go to the recommend screen and new novels will be recommended based
 * on your genre preferences.
 *
 * List of wow features:
 * -Webscraping technology with the JSoup external library
 * -"Multi-threading" with the use of a SwingWorker
 * -95% of novels can be read from novelfull.com without any problems
 * -Usage of JScrollPanes to continuously scroll through the contents of chapters and libraries
 * -Very fast saving and loading novel information speeds from txt with buffered reader + printwriter
 * -Novels are always saved even if the user closes the window/application
 * -Completely innovative, very useful, groundbreaking, not sarcastic algorithm that recommends new novels
 *
 * Major skills used:
 * -Creating branches and then submitting a pull request through Git (version control)
 * -Importing and loading custom fonts (through a .ttf file)
 * -Object oriented programming (OOP)
 * -Polymorphism
 * -Java swing
 * -File writing and reading
 *
 * Areas of concern:
 * -There are the occasional faulty novels that are scraped. This results in the program creating null pointer errors
 * around the novel (i.e. if novel 3 is faulty, novel 2 and novel 4 may act funky as well). I tried my best blacklisting them (there
 * are 6 completely faulty novels or so). Some might not have been accounted for, but the majority of the novels work. (this is a web-scraping
 * issue that is hard to completely fix)
 * -There will be some times where the program will glitch due to faulty novels. This is occurs only during the
 * recommend screen where new novels are being recommended. There is no physical blacklist for the recommend screen, which means
 * that a faulty novel exists. If a funky novel exists, the novel info screen will act funky, with buttons overlapping and clicking on thumbnails will not
 * be 100% reactive.
 * -Some novels cannot be displayed properly. These are either in the form of partial chapters (which is not
 * 100% consistent -> 1, 1.5, 2, 3, 4, 4.5, etc. or volumes are used -> vol 1 chapter 1-24, vol 2 chapter 1-24
 * but then it becomes chapter 1333, 1334 later on, or sometimes chapters are missing from the website). Some of
 * these novels include Hail the King (with partial chapters), Lord Xue Ying and Arifureta Shokugyou... (the volume chapter thing)
 * Enchantress Amongst alchemists... (for combining chapters periodically -> chapter 1235, 1236, 1237-1238 etc).
 * A blacklist could be created, but due to limited labour, this was not done.
 * -Clicking the novel thumbnail twice will reveal two instances of the novel information screen, causing
 * buttons to overlap and the program essentially breaks at this point. The only solution would be to close the program and
 * reopen it.
 * -Novels take an absurd amount of time to load. When loading the novel information with an already loaded novel,
 *  it takes a roughly second. To scrape new novels (used in the recommendation+browse screen) it takes
 * 2-3 seconds per novel to load and to display.
 * -Large file size. NotoSansSC was one of the few fonts that supported chinese characters. This is so that
 * the author name does not look like a bunch of squares. However, because of this, the res/font-NotoSansSC is a
 * whopping 48MB...
 */
public class Launcher {
    public static void main(String[] args) {
        new StartScreen();
    }
}

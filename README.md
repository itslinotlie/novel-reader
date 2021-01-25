# Novel Reader

![Novel Reader Home Screen](res/start-screen.png)

###### When a "vector" artist tries photoshop - Michael

The final rendition of the ICS4U project series. Spending a total of 40 hours, this solo project integrates my interest for reading web-novels onto an application. With the JSoup library, an open source Java HTML parser, the program is able to mimic the reading experience like that of a website such as novelfull.com. The user experience is similiar to Tachiyomi, but minus the benefits Tachiyomi has (: The user can, in no order of quality, browse for novels, add novels to a library, read and navigate through novels, and view recommendations for new novels. 

### Some screenshots of the program

![Library Screeen](https://imgur.com/1bzn1jC.png)

###### Overgeared and Omniscient Reader's Viewpoint are great novels

The place where you can bookmark and store your favourite novels. The last read chapter of these novels will always be stored, even if you suddenly exit the program. When you have enough novels in your library, you will be eligible for new novel recommendations in the recommendation screen

![Browse Screen](https://imgur.com/OOcaWM1.png)

Because loading novels takes awhile to load and display, the user has the option to determine how many novels to load on demand, ranging from 1 to 15 novels. These novels are scraped from the hottest novel list under novelfull.com, a popular website to read web-novels.

![Novel Info Screen](https://imgur.com/tvowL13.png)

Here you can find more information on your novel. By clicking on any novel thumbnail, you will be redirected to this page. Every piece of information shown here came from novelfull.com. Clicking resume will resume to the latest chapter read, chapter 1 in the case that the novel has never been read before. By adding novels into the library, the novel will be tracked in the library screen.

![Recommendation Screen](https://imgur.com/LWIiRIt.png)

Recommendations can be found under the recommendation screen. Recommendations will be made based on the top 3 most occuring genres from the library. Recommendations will always be new novels that aren't in the existing library, and no two recommendations will be the same novel. By clicking on the novel thumbnail, you will be redirected to the novel information screen, where you can view more information on the novel or start reading away. 

![Reading Screen](https://imgur.com/vPaUOBd.png)

The heart of the application. The place where you will be reading the contents of the web novels. Navigate between chapters with the back/next buttons, or jump straight to a chapter number with the text box above. Scraped straight from novelfull.com, the contents of the chapters are 100% the same. 

### Challenges I ran into

For the past few group projects, I always made an emphasis to avoid the GUI side of coding as much as possilbe. As a logical thinker, the GUI never made any sense. Add components one way and the desired results are shown. Re-order the code and everything instantly breaks. Another drawdown was how outdated and cluncky the Java Swing library was. Programming felt very outdated with the blocky and restrictive componenets. This meant that I was very inexperienced with the Swing library when I had to create a functional GUI. Solo ):

Another annoying problem was the web scraping protion of the project. 90% of the novels scraped from novelfull.com follow the a set format. Chapters are listed by numbers with an ocassional chapter name. However, some novels have irregular naming (i.e. Volume 1, Chapter 1-24, ..., Chapter 1333, 1334, etc.) and other problems which ruined my algorithm. For many cases, I was able to go around these issues; however, there are a handful of novels that when opened, will ruin the program (very funky things start to occur). From the browse screen, I was able to blacklist ~6 novels, but the novels being recommended are not handpicked, meaning that they could also be buggy, ruining the user experience. The rough fix would be to restart the program and avoid clicking on buggy novels. 

### Accomplishments I'm proud of

- Using an external Java library (Jsoup)
- Learning how to web-scrape (it sounds more complicated than it really is)
- Using Java's SwingWorker to "multi-thread"
- Using the Swing JScrollPane to allow for "infinite scrolling"
- Creating a dashboard that alternates which panels to display
- Actually creating something that works with Java's Swing library

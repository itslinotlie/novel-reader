package objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * This class represents a collection of novels. It is used in the library
 * and recommending novels when it is required to group novels together. It 
 * also contains methods to save and load a collection of novels
 */
public class Bookshelf {
    //bookshelf stores a collection of novels
    private ArrayList<Novel> bookshelf = new ArrayList();
    //frequency stores a tally of the amount of times various genres show up in the bookshelf
    private Map<String, Integer> frequency = new LinkedHashMap();

    //used for reading/loading file information
    private String path = new File("").getAbsolutePath()+"/res/bookshelf.txt";
    private static BufferedReader br;
    private static PrintWriter pw;
    private static StringTokenizer st;

    //constructor to load information from a file, if possible
    public Bookshelf() {
        load();
    }
    //saves the novel contents onto a .txt file
    public void save() {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)));
            pw.println(bookshelf.size());
            //looping however many times there are novels
            for(Novel novel:bookshelf) {
                //the character used to separate the information is an @
                //which is not commonly used anywhere in the novel information
                //this is done to prevent collisions (bad things from happening)
                pw.println(String.format("%s@%s", novel.getNovelName(), novel.getNovelLink()));
                pw.println(String.format("%s@%s@%s@%s", novel.getAuthor(), novel.getSummary(),
                    novel.getThumbnailLink(), novel.getRating()));
                for(int i=0;i<novel.getGenreList().length;i++) {
                    pw.print(novel.getGenreList()[i]+(i==novel.getGenreList().length-1? "\n":"@"));
                }
                pw.println(String.format("%d@%d@%d", novel.getLastReadChapter(), novel.getChapterRange()[0], novel.getChapterRange()[1]));
            }
            pw.close();
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Problem saving bookshelf");
        }
    }
    //loads the novel contents from a .txt file, if the file exists
    public void load() {
        if(!new File(path).isFile()) return;
        try {
            br = new BufferedReader(new FileReader(path));
            int novelAmount = readInt();
            String ret[];
            //writing down the contents of each novel
            for(int i=0;i<novelAmount;i++) {
                Novel novel = new Novel();
                ret = readLine().split("@");
                novel.setNovelName(ret[0]);
                novel.setNovelLink(ret[1]);

                ret = readLine().split("@");
                novel.setAuthor(ret[0]);
                novel.setSummary(ret[1]);
                novel.setThumbnailLink(ret[2]);
                novel.setThumbnail(new ImageIcon(ImageIO.read(new URL(novel.getThumbnailLink()))));
                novel.setRating(ret[3]);

                ret = readLine().split("@");
                novel.setGenreList(ret);

                ret = readLine().split("@");
                novel.setChapterRange(new int[] {
                    Integer.parseInt(ret[1]), Integer.parseInt(ret[2])
                });
                novel.setLastReadChapter(Integer.parseInt(ret[0]));
                add(novel);
            }
        } catch(IOException e) {
            e.printStackTrace();
            System.out.println("Problem loading bookshelf");
        }
    }

    @Override
    public String toString() {
        sort();
        String ret = "";
        ret = frequency.toString();
        return ret;
    }

    //returns the three highest genres occurring in the bookshelf
    public String[] getTopGenre() {
        int amount = 3, index = 0;
        String ret[] = new String[amount];
        for(Map.Entry<String, Integer> entry:frequency.entrySet()) {
            if(amount-->0) {
                ret[index++] = entry.getKey();
            }
        }
        return ret;
    }

    //finds the three novels that have the same genre as the parameter
    public Novel[] getNovelFromGenre(String genre) {
        int amount = 3, index = 0;
        Novel ret[] = new Novel[amount];
        ArrayList<Novel> list = new ArrayList();
        for(Novel novel:bookshelf) {
            if(novel.hasGenre(genre)) {
                list.add(novel);
            }
        }
        Collections.shuffle(list);
        for(Novel novel:list) {
            if(amount-->0) {
                ret[index++] = novel;
            }
        }
        return ret;
    }

    //clears the bookshelf
    public void clear() {
        bookshelf.clear();
        frequency.clear();
    }

    //checks if the bookshelf is empty
    public boolean isEmpty() {
        return bookshelf.size()==0;
    }

    //checks if a novel is in a bookshelf
    public boolean contains(Novel novel) {
        for(Novel title:bookshelf) {
            if(title.equals(novel)) return true;
        } return false;
    }

    //checks if a recommendation can be made
    public boolean isReadyForRecommendation() {
        return frequency.size()>2 && bookshelf.size()>4;
    }
    //adds novel to the bookshelf
    public void add(Novel novel) {
        bookshelf.add(novel);
        for(int i=0;i<novel.getGenreList().length;i++) {
            frequency.put(novel.getGenreList()[i], 1+frequency.getOrDefault(novel.getGenreList()[i], 0));
        }
    }
    //removes novel from the bookshelf
    public void remove(Novel novel) {
        bookshelf.remove(novel);
    }

    public int size() {
        return bookshelf.size();
    }

    //sorts the genres by most occurring
    public void sort() {
        //converting map to list of map
        List<Map.Entry<String, Integer>> list = new LinkedList(frequency.entrySet());
        //sorting with built in comparator
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        //looping through an inserting into new map
        frequency.clear();
        for(Map.Entry<String, Integer> entry:list) {
            frequency.put(entry.getKey(), entry.getValue());
        }
    }
    //returns novel at a given index
    public Novel get(int target) {
        return bookshelf.get(target);
    }
    //returns the bookshelf
    public ArrayList<Novel> getAllNovels() {
        return bookshelf;
    }

    private static String next() throws IOException {
        while(st==null || !st.hasMoreTokens())
            st = new StringTokenizer(br.readLine().trim());
        return st.nextToken();
    }
    private static String readLine() throws IOException {return br.readLine().trim();}
    private static int readInt() throws IOException {return Integer.parseInt(next());}
}

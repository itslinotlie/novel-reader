package objects;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.*;

public class Bookshelf {
    private ArrayList<Novel> bookshelf = new ArrayList();
    private Map<String, Integer> freq = new LinkedHashMap();

    //used for reading/loading file information
    private String path = new File("").getAbsolutePath()+"/res/bookshelf.txt";
    private static BufferedReader br;
    private static PrintWriter pw;
    private static StringTokenizer st;

    public Bookshelf() {
        load();
    }

    public static void main(String args[]) {
        Bookshelf one = new Bookshelf();
        Bookshelf two = new Bookshelf();

        Novel novel1 = new Novel("Overgeared1", "/overgeared.html");
        Novel novel2 = new Novel("Overgeared2", "/overgeared.html");
        Novel novel3 = new Novel("Overgeared3", "/overgeared.html");
        Novel novel4 = new Novel("Overgeared4", "/overgeared.html");

        one.add(novel1); one.add(novel3);
        System.out.println(one);
        one.remove(novel1);
        System.out.println(one);
    }

    public void save() {
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(path)));
            pw.println(bookshelf.size());
            for(Novel novel:bookshelf) {
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

    public void load() {
        if(!new File(path).isFile()) return;
        try {
            br = new BufferedReader(new FileReader(path));
            int novelAmount = readInt();
            String ret[];
            for(int i=0;i<novelAmount;i++) {
                Novel novel = new Novel();
                ret = readLine().split("@");
                System.out.println(Arrays.toString(ret));
                novel.setNovelName(ret[0]);
                novel.setNovelLink(ret[1]);

                ret = readLine().split("@");
                System.out.println(Arrays.toString(ret));
                novel.setAuthor(ret[0]);
                novel.setSummary(ret[1]);
                novel.setThumbnailLink(ret[2]);
                novel.setThumbnail(new ImageIcon(ImageIO.read(new URL(novel.getThumbnailLink()))));
                novel.setRating(ret[3]);

                ret = readLine().split("@");
                System.out.println(Arrays.toString(ret));
                novel.setGenreList(ret);

                ret = readLine().split("@");
                novel.setChapterRange(new int[] {
                    Integer.parseInt(ret[1]), Integer.parseInt(ret[2])
                });
                novel.setLastReadChapter(Integer.parseInt(ret[0]));
                System.out.println(novel);
                bookshelf.add(novel);
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
        ret = freq.toString();
        return ret;
    }

    public String[] getTopGenre() {
        int amount = 3, index = 0;
        String ret[] = new String[amount];
        for(Map.Entry<String, Integer> entry:freq.entrySet()) {
            if(amount-->0) {
                ret[index++] = entry.getKey();
            }
        }
        return ret;
    }

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

    public void clear() {
        bookshelf.clear();
        freq.clear();
    }

    public boolean isEmpty() {
        return bookshelf.size()==0;
    }

    public boolean contains(Novel novel) {
        for(Novel title:bookshelf) {
            if(title.equals(novel)) return true;
        } return false;
    }

    public boolean isReadyForRecommendation() {
        return freq.size()>2 && bookshelf.size()>4;
    }

    public void add(Novel novel) {
        bookshelf.add(novel);
        for(int i=0;i<novel.getGenreList().length;i++) {
            freq.put(novel.getGenreList()[i], 1+freq.getOrDefault(novel.getGenreList()[i], 0));
        }
    }

    public void remove(Novel novel) {
        bookshelf.remove(novel);
    }

    public int size() {
        return bookshelf.size();
    }

    public void sort() {
        //converting map to list of map
        List<Map.Entry<String, Integer>> list = new LinkedList(freq.entrySet());
        //sorting with built in comparator
        Collections.sort(list, (o1, o2) -> o2.getValue().compareTo(o1.getValue()));
        //looping through an inserting into new map
        freq.clear();
        for(Map.Entry<String, Integer> entry:list) {
            freq.put(entry.getKey(), entry.getValue());
        }
    }

    public Novel get(int target) {
        return bookshelf.get(target);
    }

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
    private static double readDouble() throws IOException {return Double.parseDouble(next());}
}

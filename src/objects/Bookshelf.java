package objects;

import java.util.*;

public class Bookshelf {
    private ArrayList<Novel> bookshelf = new ArrayList();
    private Map<String, Integer> freq = new LinkedHashMap();

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

    @Override
    public String toString() {
        sort();
        String ret = "";
        ret = freq.toString();
//        ret = Collections.max(freq.keySet());
//        for(int i=0;i<bookshelf.size();i++) {
//            ret+=String.format("Item %d:\n%s\n", i, bookshelf.get(i));
//        }
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
            if(title==novel) return true;
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
}

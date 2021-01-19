package objects;

import java.util.ArrayList;

public class Bookshelf {
    private ArrayList<Novel> bookshelf = new ArrayList();

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
        String ret = "";
        for(int i=0;i<bookshelf.size();i++) {
            ret+=String.format("Item %d:\n%s\n", i, bookshelf.get(i));
        }
        return ret;
    }

    public boolean contains(Novel novel) {
        for(Novel title:bookshelf) {
            if(title==novel) return true;
        } return false;
    }

    public void add(Novel novel) {
        bookshelf.add(novel);
    }

    public void remove(Novel novel) {
        bookshelf.remove(novel);
    }

    public int size() {
        return bookshelf.size();
    }

    public Novel get(int target) {
        return bookshelf.get(target);
    }
}

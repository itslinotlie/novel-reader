package main;

import objects.Novel;

import javax.swing.*;
import java.util.Scanner;

public class Library {

    public static void main(String[] args) {
        String website = "https://novelfull.com";
        String title[] = {"overgeared", "the-kings-avatar"};

        Novel novel = new Novel(website, title[1]);
        System.out.println(novel);

        JFrame frame = new JFrame();

//        NovelDisplay one = new NovelDisplay(frame, novel);
//        NovelDisplay two = new NovelDisplay(frame, new Novel(website, title[0]));
//        one.getPanel().setVisible(false);
//        two.getPanel().setVisible(false);
//
//        Scanner in = new Scanner(System.in);
//        int x = in.nextInt();
//        while(x!=-1) {
//            if(x==1) one.getPanel().setVisible(true);
//            else if(x==2) two.getPanel().setVisible(true);
//            else if(x==3) one.getPanel().setVisible(false);
//            else if(x==4) two.getPanel().setVisible(false);
//            x = in.nextInt();
//        }
    }
}

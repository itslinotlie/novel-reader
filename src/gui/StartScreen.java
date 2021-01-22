package gui;

import objects.Bookshelf;
import tools.ButtonStyle;
import tools.Design;
import tools.Misc;

import javax.swing.*;

/**
 * This displays the home screen, which will always be displayed to the user first.
 * This is a place to showcase some personal art and to load the bookshelf before it is needed.
 * By pressing the start button, the user will be directed to the library screen.
 */
public class StartScreen {
    private JFrame frame;
    private JPanel panel;
    private JLabel gif;

    private SwingWorker worker = null; //allows "multi-threading"
    private Bookshelf bookshelf;

    //basic constructor
    public StartScreen() {
        setupPanel();
        setupContent();
        setupFrame();
    }

    private void setupFrame() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setTitle(Misc.libraryTitle);
        frame.repaint();
    }

    private void setupContent() {
        //shown when things are being loaded
        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setVisible(false);
        gif.setBounds(250, 400, 100, 100);
        panel.add(gif);

        //button to direct user to library screen
        JButton go = new JButton("START");
        go.setBounds(225, 700, 150, 50);
        go.setForeground(Design.foreground);
        go.setBackground(Design.novelButtonBackground);
        go.setFont(Design.buttonTextFont);
        go.setFocusable(false);
        go.addMouseListener(new ButtonStyle());
        go.addActionListener(e -> refreshScreen());
        panel.add(go);

        //background image by me (:
        JLabel background = new JLabel();
        background.setIcon(new ImageIcon(new ImageIcon("./res/start-screen.png").getImage().getScaledInstance(Misc.WIDTH, Misc.HEIGHT, 0)));
        background.setBounds(0, 0, Misc.WIDTH, Misc.HEIGHT);
        panel.add(background);
    }

    private void setupPanel() {
        frame = new JFrame();

        panel = new JPanel();
        panel.setBackground(Design.screenBackground);
        panel.setLayout(null);
        frame.add(panel);
    }

    private void refreshScreen() {
        setupWorker();
        worker.execute();
    }

    //loads the bookshelf while displaying the gif
    private void setupWorker() {
        worker = new SwingWorker() {
            @Override
            protected Void doInBackground() {
                gif.setVisible(true);
                bookshelf = new Bookshelf();
                return null;
            }
            @Override
            protected void done() {
                panel.setVisible(false);
                new Library(frame, bookshelf);
            }
        };
    }
}

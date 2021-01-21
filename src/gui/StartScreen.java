package gui;

import objects.Bookshelf;
import tools.ButtonStyle;
import tools.Design;
import tools.Misc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.concurrent.TimeUnit;

public class StartScreen {
    private JFrame frame;
    private JPanel panel;
    private JLabel gif;

    private SwingWorker worker = null; //allows "multi-threading"
    private Bookshelf bookshelf;

    public static void main(String[] args) {
        new StartScreen();
    }

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
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                JOptionPane.showMessageDialog(frame, "Saving files. Click ok and wait until a confirmation message.");
                bookshelf.save();
                JOptionPane.showMessageDialog(frame, "Thank you for your patience. You can close now the screen now");
            }
        });
    }

    private void setupContent() {
        gif = new JLabel();
        gif.setIcon(new ImageIcon(new ImageIcon("./res/load.gif").getImage().getScaledInstance(100, 100, 0)));
        gif.setVisible(false);
        gif.setBounds(250, 400, 100, 100);
        panel.add(gif);

        JButton go = new JButton("START");
        go.setBounds(225, 650, 150, 50);
        go.setForeground(Design.foreground);
        go.setBackground(Design.novelButtonBackground);
        go.setFont(Design.buttonTextFont);
        go.setFocusable(false);
        go.addMouseListener(new ButtonStyle());
        go.addActionListener(e -> refreshScreen());
        panel.add(go);

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
                System.out.println(bookshelf);
                new Library(frame, bookshelf);
            }
        };
    }
}

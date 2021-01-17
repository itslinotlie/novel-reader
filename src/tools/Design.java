package tools;

import java.awt.*;
import java.io.*;

public class Design {
    //dimensions
    public static final int WIDTH = 600, HEIGHT = 800;

    //custom fonts
    public static Font novelTextFont;
    public static Font buttonTextFont;
    public static Font boldText;
//    public static Font author = new Font("Tahoma", Font.PLAIN, 16);

    //loading custom font (.ttf) and adding it to GraphicsEnvironment so that it can be used
    static {
        try {
            //path to the font library
            String font = "NotoSansSC";
            String root = "res/font-" + font + "/";
            novelTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+font+"-Medium.otf")).deriveFont(16f);
            buttonTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+font+"-Bold.otf")).deriveFont(24f);
            boldText = Font.createFont(Font.TRUETYPE_FONT, new File(root+font+"-Black.otf")).deriveFont(28f);
            //adding fonts to the graphics environment, which will allow
            //them to be used throughout the project
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(novelTextFont);
            ge.registerFont(buttonTextFont);
            ge.registerFont(boldText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //colours
    public static Color novelBackground = Color.decode("#f4f4f4"); //newspaper
    public static Color novelButtonBackground = Color.decode("#5cb85c"); //green
    public static Color novelButtonBackgroundDark = Color.decode("#449d44"); //dark green
    public static Color novelText = Color.decode("#455d72"); //grey
    public static Color foreground = Color.decode("#fff4c0"); //white

    public static Color screenBackground = Color.decode("#23272a"); //dark gray
    public static Color screenLightBackground = Color.decode("#2c2f33"); //lighter-ish dark gray
    public static Color screenHighlight = Color.decode("#99aab5"); //light gray
    public static Color screenPop = Color.decode("#7289da"); //blue

}

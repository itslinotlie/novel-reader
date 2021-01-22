package tools;

import java.awt.*;
import java.io.*;

/**
 * Various design styling. Creating this class creates unity amongst the components
 * that use the variables here. Changes can also easily be made by changing one variable
 * rather than changing variables from multiple files.
 * In essence, consistent styling across all files and convenience for the programmer (reusable styling)
 */
public class Design {
    //custom fonts
    public static Font novelTextFont;
    public static Font buttonTextFont;

    //loading custom font (NotoSansSC.ttf) and adding it to GraphicsEnvironment so that it can be used
    //across various files with .setFont(<font>);
    static {
        try {
            //path to the font library
            String font = "NotoSansSC";
            String root = "res/font-" + font + "/";
            novelTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+font+"-Medium.otf")).deriveFont(16f);
            buttonTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+font+"-Bold.otf")).deriveFont(24f);
            //adding fonts to the graphics environment, which will allow
            //them to be used throughout the project
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(novelTextFont);
            ge.registerFont(buttonTextFont);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Problem loading fonts");
        }
    }

    //colours
    public static Color novelBackground = Color.decode("#f4f4f4"); //newspaper
    public static Color novelButtonBackground = Color.decode("#5cb85c"); //green
    public static Color red = Color.decode("#f08080"); //red
    public static Color novelButtonBackgroundDark = Color.decode("#449d44"); //dark green
    public static Color novelText = Color.decode("#455d72"); //grey
    public static Color foreground = Color.decode("#fff4c0"); //white

    public static Color screenBackground = Color.decode("#23272a"); //dark gray
    public static Color screenLightBackground = Color.decode("#2c2f33"); //lighter-ish dark gray
    public static Color screenHighlight = Color.decode("#99aab5"); //light gray
    public static Color screenPop = Color.decode("#7289da"); //blue

    //dimensions for JPanels
    public static Dimension header = new Dimension(Misc.WIDTH, 50);
    public static Dimension footer = new Dimension(Misc.WIDTH, 125);
}

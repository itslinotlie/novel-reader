package tools;

import java.awt.*;
import java.io.*;

public class Design {
    //custom fonts
    public static Font novelTextFont;
    public static Font buttonTextFont;

    //loading custom font (.ttf) and adding it to GraphicsEnvironment so that it can be used
    static {
        try {
            //path to the font library
            String root = "res/font-raleway/";
            novelTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+"Raleway-Medium.ttf")).deriveFont(16f);
            buttonTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+"Raleway-SemiBold.ttf")).deriveFont(16f);
            //adding fonts to the graphics environment, which will allow
            //them to be used throughout the project
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(novelTextFont);
            ge.registerFont(buttonTextFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //colours
    public static Color novelBackground = Color.decode("#f4f4f4"); //newspaper
    public static Color novelButtonBackground = Color.decode("#5cb85c"); //green
    public static Color novelButtonBackgroundDark = Color.decode("#449d44");
    public static Color novelText = Color.decode("#455d72"); //grey
    public static Color novelButtonForeground = Color.decode("#fff4c0"); //white

}

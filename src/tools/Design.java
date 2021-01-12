package tools;

import java.awt.*;
import java.io.*;

public class Design {
    //custom fonts
    public static Font novelTextFont;

    //loading custom font (.ttf) and adding it to GraphicsEnvironment so that it can be used
    static {
        try {
            String root = "res/font-raleway/";
            novelTextFont = Font.createFont(Font.TRUETYPE_FONT, new File(root+"Raleway-Regular.ttf")).deriveFont(16f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(novelTextFont);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //colours
    public static Color novelBackground = Color.decode("#f4f4f4"); //newspaper
    public static Color novelButton = Color.decode("#5cb85c");
    public static Color novelText = Color.decode("#455d72");

}

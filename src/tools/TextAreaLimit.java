package tools;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class modifies the default methods for PlainDocument This allows
 * customization for JTextAreas to limit the amount of characters inputted
 */
public class TextAreaLimit extends PlainDocument {
    private int limit;
    //constructor to set the maximum limit allowed in the text field
    public TextAreaLimit(int limit) {
        this.limit = limit;
    }
    // this changes the maximum size allowed for JTextArea
    // if the inserted length + existing length is under 4 (max length of chapter #) for chapters
    // or two for the browse screen novel per load, the character will be inserted, else, nothing occurs
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
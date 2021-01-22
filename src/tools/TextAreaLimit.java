package tools;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * This class modifies the default methods for PlainDocument This allows
 * customization for JTextAreas
 */
public class TextAreaLimit extends PlainDocument {

    // this changes the maximum size allowed for JTextArea
    // if the inserted length + existing length is under 4 (max length of chapter #)
    // since there is no chapter 10k+, the character will be inserted, else, nothing occurs
    @Override
    public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
        if ((getLength() + str.length()) <= 4) {
            super.insertString(offset, str, attr);
        }
    }
}
package enigma;

import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ria Vora
 */
class Alphabet {


    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        checkChars(chars);
        setChars(chars.toCharArray());
    }

    /** A helper method to check if the characters passed
     *  into alphabet are not *, (, or ).
     *  @param chars is the chars passed into alphabet*/
    private void checkChars(String chars) {
        if (!chars.matches("[^\\*\\(\\)]+")) {
            throw new EnigmaException("You cannot have a"
                    + "*, (, ) in your alphabet! The alphabet "
                    + chars + " is not allowed.");
        }
    }

    /** A helper method that sets the Arraylist LETTERS to
     * be the contents of charsArray while checking for
     * duplicates.
     * @param charsArray is the array of chars
     * made from chars passed into alphabet*/
    private void setChars(char[] charsArray) {
        _letters = new ArrayList<Character>();
        for (char c: charsArray) {
            if (_letters.contains(c)) {
                throw new EnigmaException("You cannot create an"
                        + "Alphabet with duplicates in it."
                        + " Your duplicate letter is " + c);
            }
            _letters.add(c);
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _letters.size();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {
        return _letters.contains(ch);
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (index >= size() || index < 0) {
            throw new EnigmaException("Your index of "
                    + index + " is out of bounds for an alphabet of length "
                    + size());
        }
        return _letters.get(index);
    }

    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {
        if (!contains(ch)) {
            throw new EnigmaException("The alphabet does not contain " + ch);

        }
        return _letters.indexOf(ch);
    }

    /** An ArrayList containing each char in the alphabet. */
    private ArrayList<Character> _letters;


}

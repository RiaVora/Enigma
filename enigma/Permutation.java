package enigma;

import java.util.HashMap;
import java.util.ArrayList;


/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ria Vora
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */


    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        checkCycles(cycles);
        _cycles = new HashMap<Character, Character>();
        setCycles(cycles);
        addNotInCycles();
    }

    /** Checks whether the cycles match a certain format and
     * whether they are contained in the alphabet.
     * @param cycles contains all the cycles*/
    private void checkCycles(String cycles) {
        if (!cycles.matches("(\\([^ \\*\\(\\)]+\\) ?)*")) {
            throw new EnigmaException("Your cycles " + cycles
                    + " are incorrectly formatted, and "
                    + "should be in the form of (...) (..)");
        }
        char[] letters = cycles.replaceAll("\\(|\\)| ", "").toCharArray();
        for (char a: letters) {
            if (!_alphabet.contains(a)) {
                throw new EnigmaException("Your cycles " + cycles
                        + " have the additional character " + a
                        + " that is not in the given alphabet");
            }
        }

        hasDuplicates(letters);
    }

    /** Checks whether the cycles have duplicate letters.
     * @param letters is the array of letters in cycles */
    private void hasDuplicates(char[] letters) {
        ArrayList<Character> temp = new ArrayList<Character>();
        for (char a: letters) {
            if (temp.contains(a)) {
                throw new EnigmaException("Your cycles have "
                        + "the duplicate letter of " + a);
            }
            temp.add(a);
        }
    }

    /** Cuts the cycles into each separate cycle, removes parentheses,
     * and adds them to the Permutation.
     * @param cycles is a string with all of the cycles*/
    private void setCycles(String cycles) {
        String[] cutCycles = cycles.replaceAll(
                "\\(| ", "").split("\\)");
        for (String cycle: cutCycles) {
            if (cycle.length() > 0) {
                addCycle(cycle);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length() - 1; i++) {
            char a = cycle.charAt(i);
            if (_cycles.containsKey(a)) {
                throw new EnigmaException("The cycle "
                        + cycle + " has the duplicate letter " + a);
            }
            _cycles.put(a, cycle.charAt(i + 1));
        }
        char c = cycle.charAt(cycle.length() - 1);
        if (_cycles.containsKey(c)) {
            throw new EnigmaException("The cycle "
                    + cycle + " has the duplicate letter " + c);
        }
        _cycles.put(c, cycle.charAt(0));
    }

    /** Adds all of the letters not in cycles to map
     * to themselves in the HashMap cycles.*/
    private void addNotInCycles() {
        for (int i = 0; i < _alphabet.size(); i++) {
            char a = _alphabet.toChar(i);
            if (!_cycles.containsKey(a)) {
                _cycles.put(a, a);
                _derangement = false;
            }
        }
    }


    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        char translation = permute(_alphabet.toChar(wrap(p)));
        return _alphabet.toInt(translation);
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        char translation = invert(_alphabet.toChar(wrap(c)));
        return _alphabet.toInt(translation);
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (_cycles.containsKey(p)) {
            return _cycles.get(p);
        }
        throw new EnigmaException("Character is not in the alphabet");

    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        for (char key: _cycles.keySet()) {
            if (_cycles.get(key) == c) {
                return key;
            }
        }
        throw new EnigmaException("Character is not in the alphabet");
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        if (!_derangement) {
            return false;
        }
        for (char key : _cycles.keySet()) {
            if (key == _cycles.get(key)) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** Cycles of this permutation. */
    private HashMap<Character, Character> _cycles;

    /** Whether this permutation is a derangement. */
    private boolean _derangement = true;

}

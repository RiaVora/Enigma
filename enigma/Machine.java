package enigma;

import java.util.ArrayList;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author Ria Vora
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            ArrayList<Rotor> allRotors) {
        _alphabet = alpha;
        setNumRotors(numRotors);
        setPawls(pawls);
        setAllRotors(allRotors);
    }

    /** Checks if the number of rotors is greater than one (otherwise it
     * throws an exception) and then it initializes NUMROTORS. */
    private void setNumRotors(int numRotors) {
        if (numRotors <= 1) {
            throw new EnigmaException("You have too few rotors, "
                    + numRotors + " is not enough! You need more than one.");
        }
        _numRotors = numRotors;
    }

    /** Checks if 0 <= PAWLS < NUMROTORS pawls
     * (otherwise it throws an exception) and then it initializes
     * PAWLS. */
    private void setPawls(int pawls) {
        if (pawls < 0 || pawls >= _numRotors) {
            throw new EnigmaException("You passed in " + pawls + ","
                    + "but your pawls need to be >= 0"
                    + " and < the number of rotors.");
        }
        _pawls = pawls;
    }

    /** Initializes the collection of rotors given to the setting
     * of 0.
     * @param allRotors is all of the given rotors*/
    private void setAllRotors(ArrayList<Rotor> allRotors) {
        if (allRotors.isEmpty()) {
            throw new EnigmaException("You cannot have no rotors!");
        }
        _allRotors = allRotors;
        for (int i = 0; i < _allRotors.size(); i++) {
            _allRotors.get(i).set(0);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rotors = new ArrayList<Rotor>();
        checkRotors(rotors);
        setReflector(rotors[0]);
        for (int i = 1; i < rotors.length; i++) {
            Rotor rotor = findRotor(rotors[i]);
            rotor.set(0);

            if (i >= rotors.length - _pawls && !rotor.rotates()) {
                throw new EnigmaException("You have " + _pawls + ""
                        + "pawls but not " + "enough moving rotors! Rotor "
                        + rotor.name() + " does not rotate.");
            } else if (i < rotors.length - _pawls && rotor.rotates()) {
                throw new EnigmaException("You have " + _pawls
                        + " pawls but too many moving " + "rotors! Rotor "
                        + rotor.name() + " rotates, but shouldn't");
            }

            if (!_rotors.contains(rotor)) {
                _rotors.add(i, rotor);
            } else {
                throw new EnigmaException("You cannot put the same "
                        + rotor + " into the machine twice!");
            }
        }
    }

    /** Checks whether the length of the rotors array is
     * equal to the number of rotors.
     * @param rotors is a string of rotor names*/
    private void checkRotors(String[] rotors) {
        if (rotors.length == 0) {
            throw new EnigmaException("You cannot set a "
                    + "machine with no rotors!");
        } else if (rotors.length < _numRotors) {
            throw new EnigmaException("You cannot set a machine "
                    + "with too little rotors! "
                    + "You are supposed to have " + _numRotors
                    + " rotors but you have only given " + rotors.length);
        } else if (rotors.length > _numRotors) {
            throw new EnigmaException("You cannot set a "
                    + " machine with too many rotors! "
                    + "You are supposed to have " + _numRotors
                    + " rotors but you have given " + rotors.length);
        }

        for (int i = 0; i < rotors.length; i++) {
            if (!hasRotor(rotors[i])) {
                throw new EnigmaException("The given rotor "
                        + rotors[i] + " is not in your "
                        + "selection of all rotors!");
            }
        }

    }

    /** Checks whether the first rotor is a reflector and sets
     * it to the first position in the rotors ArrayList.
     * @param name is the name of the first rotor*/
    private void setReflector(String name) {
        Rotor rotor = findRotor(name);
        if (!rotor.reflecting()) {
            throw new EnigmaException("You cannot set "
                    + name + " to be the"
                    + " first rotor because it is not a reflector!");
        }
        _rotors.add(0, rotor);
    }

    /** Searches through all of the rotors to return
     * the rotor with the given name.
     * @param name is the name of the rotor*/
    private Rotor findRotor(String name) {
        for (int i = 0; i < _allRotors.size(); i++) {
            Rotor rotor = _allRotors.get(i);
            if (rotor.name().equals(name)) {
                return rotor;
            }
        }
        throw new EnigmaException("The rotor named "
                + name + " is not one of the possible rotors!");
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        checkSetting(setting);
        for (int i = 1; i < _rotors.size(); i++) {
            int newSetting = _alphabet.toInt(setting.charAt(i - 1));
            _rotors.get(i).set(newSetting);
        }
    }

    /** Checks whether a setting is the proper length
     * and is contained in the alphabet.
     * @param setting is the string dictating
     * the setting of each of the rotors*/
    private void checkSetting(String setting) {
        if (setting.length() != numRotors() - 1) {
            throw new EnigmaException("The setting " + setting + " is "
                    + " not the right length!"
                    + " It should be of length " + (numRotors() - 1));
        }
        for (int i = 0; i < setting.length(); i++) {
            if (!_alphabet.contains(setting.charAt(i))) {
                throw new EnigmaException("The setting " + setting
                        + " has the letter " + setting.charAt(i)
                        + " which is not in the alphabet!");
            }
        }
    }

    /** Returns true or false based on whether the
     * rotor with the given name is in the array
     * of allRotors that the machine has access to.
     * @param name is the name of the rotor*/
    boolean hasRotor(String name) {
        for (int i = 0; i < _allRotors.size(); i++) {
            if (_allRotors.get(i).name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        if (c < 0 || c > _alphabet.size() - 1) {
            throw new EnigmaException("The input character index " + c
                    + " is outside of the index "
                    + "of 0 to the alphabet of size " + _alphabet.size());
        }
        advance();
        if (_plugboard == null) {
            throw new EnigmaException("You haven't created a plugboard yet!");
        }
        c = _plugboard.permute(c);
        for (int i = _rotors.size() - 1; i >= 0; i--) {
            c = _rotors.get(i).convertForward(c);
        }
        for (int i = 1; i < _rotors.size(); i++) {
            c = _rotors.get(i).convertBackward(c);
        }
        c = _plugboard.invert(c);
        return c;
    }

    /** Moves the rotors in the machine forward based on whether they can
     * advance or not. */
    void advance() {
        HashMap<String, Boolean> canAdvance = new HashMap<String, Boolean>();
        if (_rotors.isEmpty()) {
            throw new EnigmaException("You have not set up your rotors yet!");
        }
        for (int i = 0; i < _rotors.size(); i++) {
            canAdvance.put(_rotors.get(i).name(), true);
        }

        Rotor lastRotor =  _rotors.get(_rotors.size() - 1);

        for (int i = _rotors.size() - _pawls; i < _rotors.size() - 1; i++) {
            Rotor leftRotor = _rotors.get(i + 1);
            Rotor currentRotor = _rotors.get(i);
            if (leftRotor.atNotch()) {
                if (canAdvance.get(currentRotor.name())) {
                    currentRotor.advance();
                    canAdvance.replace(currentRotor.name(), false);
                }
                if (canAdvance.get(leftRotor.name())) {
                    leftRotor.advance();
                    canAdvance.replace(leftRotor.name(), false);
                }
            }
        }
        if (canAdvance.get(lastRotor.name())) {
            lastRotor.advance();
        }
    }

    /** Prints the settings of each rotor in a row,
     * used primarily for debugging.
     * @return a string containing the settings for
     * each rotor*/
    String rowOfRotors() {
        String result = "";
        for (int i = 0; i < _rotors.size(); i++) {
            int setting = _rotors.get(i).setting();
            result += _alphabet.toChar(setting);
        }
        return result;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        char[] resultArr = new char[msg.length()];
        for (int i = 0; i < resultArr.length; i++) {
            int c = _alphabet.toInt(msg.charAt(i));
            resultArr[i] = _alphabet.toChar(convert(c));
        }
        String result = "";
        for (char a: resultArr) {
            result += a;
        }
        return result;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;

    /** Number of rotors in my machine. */
    private int _numRotors;

    /** Number of pawls in my machine. */
    private int _pawls;

    /** All possible rotors that could be used. */
    private ArrayList<Rotor> _allRotors;

    /** The rotors being used in my machine. */
    private ArrayList<Rotor> _rotors;

    /** The plugboard being used in my machine. */
    private Permutation _plugboard;
}

package enigma;

import java.util.ArrayList;


/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ria Vora
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        set(0);
        _notches = new ArrayList<Integer>();
        setNotches(notches);
    }

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is set to the given SETTING.*/
    MovingRotor(String name, Permutation perm, String notches, int setting) {
        super(name, perm);
        set(setting);
        _notches = new ArrayList<Integer>();
        setNotches(notches);
    }
    /** Sets the notches to be their index value into a notch ArrayList.
     * Also checks to see if each of the letters exists in Alphabet,
     * otherwise throws an exception.
     * @param notches is a String with all of the notches. */
    void setNotches(String notches) {
        for (int i = 0; i < notches.length(); i++) {
            char a = notches.charAt(i);
            if (!alphabet().contains(a)) {
                throw new EnigmaException("Notch of "
                        + a + " is not in your alphabet!");
            }
            _notches.add(permutation().alphabet().toInt(a));
        }
    }

    @Override
    boolean rotates() {
        return true;
    }

    @Override
    void advance() {
        int newSetting = 0;
        if (setting() <  alphabet().size() - 1) {
            newSetting = setting() + 1;
        }
        set(newSetting);
    }

    @Override
    boolean atNotch() {
        if (_notches.isEmpty()) {
            return true;
        }
        return _notches.contains(setting());
    }

    /** An ArrayList of the int index value of
     * each notch.*/
    private ArrayList<Integer> _notches;

}

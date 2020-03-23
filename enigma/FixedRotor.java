package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotor that has no ratchet and does not advance.
 *  @author Ria Vora
 */
class FixedRotor extends Rotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM. */
    FixedRotor(String name, Permutation perm) {
        super(name, perm);
        set(0);
    }

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is given by PERM, and whose setting is given by SETTING. */
    FixedRotor(String name, Permutation perm, int setting) {
        super(name, perm);
        set(setting);
    }

}

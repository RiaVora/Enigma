package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Fixed Rotor and Reflector class.
 *  @author Ria Vora
 */
public class FixedRotorTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Rotor rotor;
    private String alpha = UPPER_STRING;

    /** Check that rotor has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkRotor(String testId,
                            String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, rotor.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d (%c)", ci, c),
                    ei, rotor.convertForward(ci));
            assertEquals(msg(testId, "wrong inverse of %d (%c)", ei, e),
                    ci, rotor.convertBackward(ei));
        }
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS. */
    private void setRotor(String name, HashMap<String, String> rotors) {
        rotor = new FixedRotor(name, new Permutation(rotors.get(name), UPPER));
    }

    /** Set the rotor to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS. */
    private void setRotor(String name, HashMap<String, String> rotors,
                          int setting) {
        rotor = new FixedRotor(name,
                new Permutation(rotors.get(name), UPPER), setting);
    }

    /** Set the reflector to the one with given NAME and permutation as
     *  specified by the NAME entry in ROTORS. */
    private void setReflector(String name, HashMap<String, String> rotors) {
        rotor = new Reflector(name, new Permutation(rotors.get(name), UPPER));
    }

    /* ***** TESTS ***** */

    @Test
    public void checkRotorAtA() {
        setRotor("I", NAVALA);
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));

        setRotor("I", NAVALA, 0);
        checkRotor("Rotor I (A)", UPPER_STRING, NAVALA_MAP.get("I"));
    }

    @Test
    public void checkRotorAdvance() {
        setRotor("I", NAVALA);
        assertEquals(rotor.setting(), 0);
        rotor.advance();
        assertEquals(rotor.setting(), 0);
    }

    @Test
    public void checkRotorSet() {
        setRotor("I", NAVALA);
        rotor.set(25);
        checkRotor("Rotor I set", UPPER_STRING, NAVALZ_MAP.get("I"));
    }

    @Test
    public void checkRotorNotches() {
        setRotor("I", NAVALA);
        assertEquals(rotor.setting(), 0);
        rotor.advance();
        assertFalse(rotor.atNotch());
        rotor.set(24);
        assertFalse(rotor.atNotch());

        setRotor("I", NAVALA, 1);
        assertEquals(rotor.setting(), 1);
        assertFalse(rotor.atNotch());

    }

    @Test
    public void checkReflector() {
        setRotor("I", NAVALA);
        assertEquals(rotor.setting(), 0);
        rotor.advance();
        assertEquals(rotor.setting(), 0);
        assertFalse(rotor.atNotch());
    }







}

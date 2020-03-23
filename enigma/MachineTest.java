package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Machine class.
 *  @author Ria Vora
 */

public class MachineTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Machine machine;
    private ArrayList<Rotor> allRotors;
    private String alpha = UPPER_STRING;

    /* ***** TESTS ***** */


    private void setRotors(HashMap<String, String> given) {
        allRotors = new ArrayList<Rotor>();
        HashMap<String, String> notches = new HashMap<String, String>();
        notches.put("I", "Q"); notches.put("II", "E"); notches.put("III", "V");
        notches.put("IV", "J"); notches.put("V", "Z"); notches.put("VI", "ZM");
        notches.put("VII", "ZM"); notches.put("VIII", "ZM");

        for (String name: given.keySet()) {
            if (name.equals("B") || name.equals("C")) {
                allRotors.add(new Reflector(name,
                        new Permutation(given.get(name), new Alphabet(alpha))));
            } else if (name.equals("Beta") || name.equals("Gamma")) {
                allRotors.add(
                        new FixedRotor(name,
                                new Permutation(given.get(name),
                                        new Alphabet(alpha))));
            } else {
                allRotors.add(new MovingRotor(name,
                        new Permutation(given.get(name),
                                new Alphabet(alpha)), notches.get(name)));
            }
        }
    }

    @Test
    public void checkMachine() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 3, allRotors);

    }

    @Test(expected = EnigmaException.class)
    public void testNumRotors1() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 0, 3, allRotors);
    }
    @Test(expected = EnigmaException.class)
    public void testNumRotors2() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 1, 3, allRotors);
    }

    @Test
    public void testNumRotors() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 3, allRotors);
        assertEquals(5, machine.numRotors());
    }

    @Test(expected = EnigmaException.class)
    public void testPawls1() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, -1, allRotors);
    }

    @Test(expected = EnigmaException.class)
    public void testPawls2() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 8, allRotors);
    }

    @Test
    public void testPawls() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 3, allRotors);
        assertEquals(3, machine.numPawls());

        machine = new Machine(new Alphabet(alpha), 5, 0, allRotors);
        assertEquals(0, machine.numPawls());
    }

    @Test(expected = EnigmaException.class)
    public void testAllRotors() {
        allRotors = new ArrayList<Rotor>();
        machine = new Machine(new Alphabet(alpha), 5, 8, allRotors);
    }

    @Test
    public void testSetRotors() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 3, allRotors);
        machine.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        machine.setRotors("AXLE");
    }

    @Test
    public void testConvert() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 3, allRotors);
        machine.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        machine.setRotors("AXLE");

        machine.setPlugboard(new Permutation("(YF) (HZ)", new Alphabet(alpha)));
        int y = (new Alphabet(alpha)).toInt('Y');
        int z = (new Alphabet(alpha)).toInt('Z');
        assertEquals(z, machine.convert(y));

        machine.setPlugboard(new Permutation("(MS) (FY)", new Alphabet(alpha)));
        int m = (new Alphabet(alpha)).toInt('M');
        assertEquals(y, machine.convert(m));
    }

    @Test
    public void testConvertMsg() {
        setRotors(NAVALA);
        machine = new Machine(new Alphabet(alpha), 5, 3, allRotors);
        machine.insertRotors(new String[] {"B", "Beta", "III", "IV", "I"});
        machine.setRotors("AXLE");

        machine.setPlugboard(new
                Permutation("(YF) (HZ) (MS) (AP) (LI)", new Alphabet(alpha)));
        assertEquals("ZYSG", machine.convert("YMPI"));

        int y = (new Alphabet(alpha)).toInt('Y');

        for (int i = 0; i < 10; i++) {
            machine.convert(y);
        }
    }

    @Test
    public void testAdvance() {
        allRotors = new ArrayList<Rotor>();
        Alphabet testAlpha = new Alphabet("ABC");
        allRotors.add(new Reflector("1", new Permutation("(ABC)", testAlpha)));
        allRotors.add(new MovingRotor("2", new
                Permutation("(ABC)", testAlpha), "C"));
        allRotors.add(new MovingRotor("3", new
                Permutation("(ABC)", testAlpha), "C"));
        allRotors.add(new MovingRotor("4",
                new Permutation("(ABC)", testAlpha), "C"));
        machine = new Machine(testAlpha, 4, 3, allRotors);
        machine.insertRotors(new String[] {"1", "2", "3", "4"});
        machine.setRotors("AAA");
        machine.setPlugboard(new Permutation("(ABC)", testAlpha));

        machine.convert(0);
        assertEquals("AAAB", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("AAAC", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("AABA", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("AABB", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("AABC", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("AACA", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ABAB", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ABAC", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ABBA", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ABBB", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ABBC", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ABCA", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ACAB", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ACAC", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ACBA", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ACBB", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ACBC", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("ACCA", machine.rowOfRotors());
        machine.convert(0);
        assertEquals("AAAB", machine.rowOfRotors());
    }


}

package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Ria Vora
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test(expected = EnigmaException.class)
    public void testCyclesException1() {
        perm = new Permutation("(BAC) (DEF)", new Alphabet("ABCD"));
    }
    @Test(expected = EnigmaException.class)
    public void testCyclesException2() {
        perm = new Permutation("(BAC) (ABC)", new Alphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testCyclesException3() {
        perm = new Permutation("(BAC) (A)", new Alphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testCyclesException4() {
        perm = new Permutation("(ABCD) (   )", new Alphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testCyclesException5() {
        perm = new Permutation("(220923)", new Alphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testCyclesException6() {
        perm = new Permutation("(BA C D E G)", new Alphabet("ABCD"));
    }

    @Test(expected = EnigmaException.class)
    public void testCyclesException7() {
        perm = new Permutation("(A B C D)", new Alphabet("ABCD"));
    }
    @Test(expected = EnigmaException.class)
    public void testCyclesException8() {
        perm = new Permutation("(A) (A) (A)", new Alphabet("ABCD"));
    }

    @Test
    public void testCycles() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        perm = new Permutation("(ABCD)", new Alphabet("ABCD"));
        perm = new Permutation("(AB) (C)", new Alphabet("ABCD"));
        perm = new Permutation("(2495)", new Alphabet("9425"));
        perm = new Permutation("(24) (95)", new Alphabet("9425"));
        perm = new Permutation("(A)(B)(C)", new Alphabet("ABCD"));
        perm = new Permutation("(AB)(CD)", new Alphabet("ABCD"));


    }


    @Test
    public void testSize() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(4, perm.size());
        perm = new Permutation("(LQOPM)", new Alphabet("LMNOPQ"));
        assertEquals(6, perm.size());
        perm = new Permutation("(A)", new Alphabet("A"));
        assertEquals(1, perm.size());
        perm = new Permutation("(ABCDEFG)",
                new Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ"));
        assertEquals(26, perm.size());
        perm = new Permutation("(ABCD) (FGH)", new Alphabet("ABCDEFGH"));
        assertEquals(8, perm.size());
    }

    @Test
    public void testPermuteChar() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        alpha = "ABCD";
        checkPerm("testPermuteChar for p", "ABCD", "CADB");

        assertEquals('A', perm.permute('B'));
        assertEquals('C', perm.permute('A'));
        assertEquals('D', perm.permute('C'));
        assertEquals('B', perm.permute('D'));

        perm = new Permutation("(LQOPM)", new Alphabet("LMNOPQ"));
        alpha = "LMNOPQ";
        checkPerm("testPermuteChar for p2", "LMNOPQ", "QLNPMO");

        assertEquals('Q', perm.permute('L'));
        assertEquals('O', perm.permute('Q'));
        assertEquals('P', perm.permute('O'));
        assertEquals('M', perm.permute('P'));
        assertEquals('L', perm.permute('M'));
        assertEquals('N', perm.permute('N'));

        perm = new Permutation("(A)", new Alphabet("A"));
        alpha = "A";
        checkPerm("testPermuteChar for p3", "A", "A");
        assertEquals('A', perm.permute('A'));

        perm = new Permutation("(ABCD) (FGH)", new Alphabet("ABCDEFGH"));
        alpha = "ABCDEFGH";
        checkPerm("testPermuteChar for p4", "ABCDEFGH", "BCDAEGHF");
        assertEquals('B', perm.permute('A'));
        assertEquals('C', perm.permute('B'));
        assertEquals('D', perm.permute('C'));
        assertEquals('A', perm.permute('D'));
        assertEquals('E', perm.permute('E'));
        assertEquals('G', perm.permute('F'));
        assertEquals('H', perm.permute('G'));
        assertEquals('F', perm.permute('H'));

    }

    @Test
    public void testInvertChar() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        alpha = "ABCD";
        assertEquals('B', perm.invert('A'));
        assertEquals('D', perm.invert('B'));
        assertEquals('C', perm.invert('D'));
        assertEquals('A', perm.invert('C'));

        perm = new Permutation("(LQOPM)", new Alphabet("LMNOPQ"));
        alpha = "LMNOPQ";
        assertEquals('L', perm.invert('Q'));
        assertEquals('Q', perm.invert('O'));
        assertEquals('O', perm.invert('P'));
        assertEquals('P', perm.invert('M'));
        assertEquals('M', perm.invert('L'));

        perm = new Permutation("(A)", new Alphabet("A"));
        alpha = "A";
        assertEquals('A', perm.invert('A'));

        perm = new Permutation("(ABCD) (FGH)", new Alphabet("ABCDEFGH"));
        alpha = "ABCDEFGH";
        assertEquals('D', perm.invert('A'));
        assertEquals('A', perm.invert('B'));
        assertEquals('B', perm.invert('C'));
        assertEquals('C', perm.invert('D'));
        assertEquals('E', perm.invert('E'));
        assertEquals('H', perm.invert('F'));
        assertEquals('F', perm.invert('G'));
        assertEquals('G', perm.invert('H'));
    }

    @Test
    public void testDerangement() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        assertEquals(true, perm.derangement());

        perm = new Permutation("(LQOPM)", new Alphabet("LMNOPQ"));
        assertEquals(false, perm.derangement());

        perm = new Permutation("(A)", new Alphabet("A"));
        assertEquals(false, perm.derangement());

        perm = new Permutation("(ABCDEFGHI)", new Alphabet("ABCDEFGHI"));
        assertEquals(true, perm.derangement());

        perm = new Permutation("(ABCDEFGH)", new Alphabet("ABCDEFGHI"));
        assertEquals(false, perm.derangement());

        perm = new Permutation("(ABCD) (FGH)", new Alphabet("ABCDEFGH"));
        assertEquals(false, perm.derangement());

    }

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        alpha = UPPER_STRING;
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test(expected = EnigmaException.class)
    public void testNotInAlphabet() {
        perm = new Permutation("(BACD)", new Alphabet("ABCD"));
        perm.invert('F');
        perm.invert('K');
        perm.invert('L');
    }

}

import java.util.LinkedList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PermutationTest {
    PermutationVariation p1;
    PermutationVariation p2;
    public int n1;
    public int n2;
    int cases = 1;

    void initialize() {
        n1 = 4;
        n2 = 6;
        Cases c = new Cases();
        p1 = c.switchforTesting(cases, n1);
        p2 = c.switchforTesting(cases, n2);
    }


    @Test
    void testPermutation() {
        initialize();
        // TODO
        assertNotNull(p1.original, "oiginal of p1 is not initialised");
        assertEquals(p1.original.length, n1, "the length of p1.original[] is not n1 ");
        for (int i = 0; i < p1.original.length; i++) {
            for (int j = 0; j < p1.original.length; j++) {
                if (i != j) {
                    assertNotEquals(p1.original[i], p1.original[j], "p1.original[" + i + "] " + "and p1.original[" + j + "]are the same number");
                }
            }
        }
        assertNotNull(p2.original, "oiginal of p2 is not initialised");
        assertEquals(p1.original.length, n1, "the length of p2.original[] is not n2 ");
        for (int i = 0; i < p2.original.length; i++) {
            for (int j = 0; j < p2.original.length; j++) {
                if (i != j) {
                    assertNotEquals(p2.original[i], p2.original[j], "p2.original[" + i + "] " + "and p2.original[" + j + "]are the same number");
                }
            }
        }
        assertNotNull(p1.allDerangements, "allDerangements of p1 is not initialised");
        assertTrue(p1.allDerangements.isEmpty(), "allDerangements of p1 is not empty");
        assertNotNull(p2.allDerangements, "allDerangements of p2 is not initialised");
        assertTrue(p2.allDerangements.isEmpty(), "allDerangements of p2 is not empty");

    }

    @Test
    void testDerangements() {
        initialize();
        //in case there is something wrong with the constructor
        fixConstructor();
        // TODO
        p1.derangements();
        double e = 2.71828;
        int factn1 = 1;
        int x;
        x = n1;
        while (x > 0) {
            factn1 *= x;
            x -= 1;
        }
        int dern1 = (int) ((factn1 + 1) / e);
        assertEquals(dern1, p1.allDerangements.size(), "the number of derangement of p1 is false");
        for (int[] option : p1.allDerangements) {
            for (int i = 0; i < option.length; i++) {
                assertNotEquals(p1.original[i], option[i], "in the position " + i + " of p1.original[] " + "the numbers are the same");
            }
        }
        p2.derangements();
        int factn2 = 1;
        x = n2;
        while (x > 0) {
            factn2 *= x;
            x -= 1;
        }
        int dern2 = (int) ((factn2 + 1) / e);
        assertEquals(dern2, p2.allDerangements.size(), "the number of derangement of p2 is false");
        for (int[] option : p2.allDerangements) {
            for (int i = 0; i < option.length; i++) {
                assertNotEquals(p2.original[i], option[i], "in the position " + i + " of p2.original[] " + "the numbers are the same");
            }
        }


    }

    @Test
    void testsameElements() {
        initialize();
        //in case there is something wrong with the constructor
        fixConstructor();
        // TODO
        p1.derangements();
        assertNotEquals(p1.allDerangements.size(), 0, "no permutation with p1 is done");
        for (int[] option : p1.allDerangements) {
            assertEquals(option.length, p1.original.length, "permetution in p1 " + option + " has not the " + "recommended length");

            for (int i = 0; i < option.length; i++) {
                int y = 0;
                for (int j = 0; j < p1.original.length; j++) {
                    if (option[i] == p1.original[j]) {
                        y++;
                    }
                }
                assertEquals(y, 1, "the permutation " + option + " isn't a permutation of p1");
            }
        }

        p2.derangements();
        assertNotEquals(p2.allDerangements.size(), 0, "no permutation with p2 is done");
        for (int[] option : p2.allDerangements) {
            assertEquals(option.length, p2.original.length, "permetution in p2 " + option + " has not the " + "recommended length");
            for (int i = 0; i < option.length; i++) {
                int y = 0;
                for (int j = 0; j < p2.original.length; j++) {
                    if (option[i] == p2.original[j]) {
                        y++;
                    }
                }
                assertEquals(y, 1, "the permutation " + option + " isn't a permutation of p2");
            }
        }
    }

    void setCases(int c) {
        this.cases = c;
    }

    public void fixConstructor() {
        //in case there is something wrong with the constructor
        p1.allDerangements = new LinkedList<int[]>();
        for (int i = 0; i < n1; i++)
            p1.original[i] = 2 * i + 1;

        p2.allDerangements = new LinkedList<int[]>();
        for (int i = 0; i < n2; i++)
            p2.original[i] = i + 1;
    }
}



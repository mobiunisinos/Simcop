package br.unisinos.simcop.impl.transformations;

import br.unisinos.simcop.data.model.Context;
import br.unisinos.simcop.tests.BaseUnitTest;
import br.unisinos.simcop.data.model.ContextSequence;
import br.unisinos.simcop.data.model.Entity;
import br.unisinos.simcop.data.model.TimeDescription;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tiago
 */
public class DefaultTimeAlignTest extends BaseUnitTest {

    public DefaultTimeAlignTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    private TimeDescription createTestTD(int hour, int minute) {
        TimeDescription td = new TimeDescription();
        td.setHour(hour);
        td.setMinute(minute);
        return td;
    }

    private Context createTestContext(int hour, int minute) {
        Context ctx = new Context();
        ctx.setTime(createTestTD(hour, minute));
        return ctx;
    }

    private void testContext(int index, Context ctx, Integer hour, Integer minute) {
        if (hour == null) {
            assertNull("Index " + index + " must be null (" + (ctx != null ? ctx.getTime().asString("HH:mm") : "ok") + ")", ctx);
        } else {
            assertNotNull("Index " + index + " must be not null", ctx);
            assertNotNull("Index " + index + " time null", ctx.getTime());
            assertEquals("Index " + index + " incorrect hour", hour, ctx.getTime().getHour());
            assertEquals("Index " + index + " incorrect minute", minute, ctx.getTime().getMinute());
        }
    }

    private void printSequence(String label, ContextSequence seq) {
        System.out.print(label + ": ");
        for (Context ctx : seq.getContexts()) {
            if (ctx != null) {
                System.out.print(ctx.getTime().asString("HH:mm"));
            } else {
                System.out.print("*null");
            }
            System.out.print(" | ");
        }
        System.out.println("");
    }

    @Test
    public void testTransform() {
        showBeginTest();

        //sequence A
        ContextSequence seqA = new ContextSequence(new Entity("1", "Test A"));
        seqA.addContext(createTestContext(05, 00));
        seqA.addContext(createTestContext(05, 30));
        seqA.addContext(createTestContext(06, 00));
        seqA.addContext(createTestContext(06, 59));

        //sequence B
        ContextSequence seqB = new ContextSequence(new Entity("2", "Test B"));
        seqB.addContext(createTestContext(05, 17));
        seqB.addContext(createTestContext(05, 28));
        seqB.addContext(createTestContext(05, 37));
        seqB.addContext(createTestContext(06, 01));
        seqB.addContext(createTestContext(06, 59));
        seqB.addContext(createTestContext(07, 01));

        //show
        System.out.println("before align:");
        printSequence("A", seqA);
        printSequence("B", seqB);


        //do align
        DefaultTimeAlign instance = new DefaultTimeAlign();
        ContextSequence[] result = instance.transform(seqA, seqB);

        //show
        System.out.println("\nafter align:");
        printSequence("A", result[0]);
        printSequence("B", result[1]);


        //test size
        assertEquals(9, result[0].size());
        assertEquals(9, result[1].size());

        //test sequence A
        System.out.print("\nSequence A: ");
        testContext(0, result[0].get(0), 5, 0);
        testContext(1, result[0].get(1), null, null);
        testContext(2, result[0].get(2), null, null);
        testContext(3, result[0].get(3), 5, 30);
        testContext(4, result[0].get(4), null, null);
        testContext(5, result[0].get(5), 6, 0);
        testContext(6, result[0].get(6), null, null);
        testContext(7, result[0].get(7), 6, 59);
        testContext(8, result[0].get(8), null, null);
        System.out.println("  [OK]");

        //test sequence B
        System.out.print("Sequence B: ");
        testContext(0, result[1].get(0), null, null);
        testContext(1, result[1].get(1), 5, 17);
        testContext(2, result[1].get(2), 5, 28);
        testContext(3, result[1].get(3), null, null);
        testContext(4, result[1].get(4), 5, 37);
        testContext(5, result[1].get(5), null, null);
        testContext(6, result[1].get(6), 6, 1);
        testContext(7, result[1].get(7), 6, 59);
        testContext(8, result[1].get(8), 7, 1);
        System.out.println("  [OK]\n");


        showEndTest();
    }
}

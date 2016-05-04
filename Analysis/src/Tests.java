import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Tests {

    final float[][][] values = {
            //always the same value
            {{0.0f, 20f}, {0.1f, 20f}, {0.2f, 20f}, {0.3f, 20f}, {0.4f, 20f}, {0.5f, 20f}},
            //different values
            {{0.01f, 0f}, {0.02f, 0f}, {0.03f, 100f}, {0.04f, 100f}},
            //different values with decimal result
            {{0.01f, 1f}, {0.02f, 1f}, {0.03f, 2f}, {0.04f, 2f}},
            //results not ordered by time
            {{0.03f, 50f}, {0.04f, 0f}, {0.01f, 65f}, {0.02f, 30f}},
            //1 value only
            {{0f, 50f}},
            //result with 3 decimal units
            {{0.01f, 0.25f}, {0.02f, 0.26f}},
            //same values at the same time
            {{0.00f, 50f}, {0.00f, 50f}},
            //different values at the same time
            {{0.00f, 50f}, {0.00f, 100f}},
            //no value
            {{}},
    };

    final TypesOfCalculations[][] types = {
            {TypesOfCalculations.WAVELENGTH},
            {TypesOfCalculations.WAVENUMBER},
            {TypesOfCalculations.AMPLITUDE},
            {TypesOfCalculations.PERIOD},
            {TypesOfCalculations.VELOCITY},
            {TypesOfCalculations.MAX},
            {TypesOfCalculations.MIN},
            {TypesOfCalculations.MEAN},
            {TypesOfCalculations.MAX,TypesOfCalculations.MIN,TypesOfCalculations.MEAN},
    };

    @Test
    public void testMain() {
        Calculations calcs = new Calculations(values[1],types[0]);
        assertTrue(calcs.getResult().containsKey(TypesOfCalculations.WAVELENGTH));
        assertNotNull(calcs.getResult().values());

        Calculations calcs2 = new Calculations(values[3],types[4]);
        assertTrue(calcs2.getResult().containsKey(TypesOfCalculations.VELOCITY));
        assertNotNull(calcs2.getResult().values());

        Calculations calcs3 = new Calculations(values[8],types[8]);
        assertNull(calcs3.getResult());
    }

    @Test
    public void testWaveLength() {
        Calculations calcs = new Calculations(values[0],types[0]);
        assertEquals(1.25,calcs.getResult().get(TypesOfCalculations.WAVELENGTH),0.001);

        Calculations calcs2 = new Calculations(values[1],types[0]);
        assertEquals(0.5,calcs2.getResult().get(TypesOfCalculations.WAVELENGTH),0.1);
    }

    @Test
    public void testWaveNumber() {
        Calculations calcs = new Calculations(values[0],types[1]);
        assertEquals(0.8,calcs.getResult().get(TypesOfCalculations.WAVENUMBER),0.01);

        Calculations calcs2 = new Calculations(values[1],types[1]);
        assertEquals(2,calcs2.getResult().get(TypesOfCalculations.WAVENUMBER),0);
    }

    @Test
    public void testAmplitude() {
        //TODO
    }

    @Test
    public void testPeriod() {
        //TODO
    }

    @Test
    public void testVelocity() {
        //TODO
    }

    @Test
    public void testMax() {
        Calculations calcs = new Calculations(values[0],types[5]);
        assertEquals(20,calcs.getResult().get(TypesOfCalculations.MAX),0);

        Calculations calcs2 = new Calculations(values[2],types[5]);
        assertEquals(65,calcs2.getResult().get(TypesOfCalculations.MAX),0);

        Calculations calcs3 = new Calculations(values[8],types[5]);
        assertNull(calcs3.getResult());
    }

    @Test
    public void testMin() {
        Calculations calcs = new Calculations(values[4],types[6]);
        assertEquals(50,calcs.getResult().get(TypesOfCalculations.MIN),0);

        Calculations calcs2 = new Calculations(values[6],types[6]);
        assertEquals(50,calcs2.getResult().get(TypesOfCalculations.MIN),0);

        Calculations calcs3 = new Calculations(values[7],types[6]);
        assertEquals(50,calcs3.getResult().get(TypesOfCalculations.MIN),0);
    }

    @Test
    public void testMean() {
        Calculations calcs = new Calculations(values[0],types[7]);
        assertEquals(20,calcs.getResult().get(TypesOfCalculations.MEAN),0);

        Calculations calcs2 = new Calculations(values[5],types[7]);
        assertEquals(0.255,calcs2.getResult().get(TypesOfCalculations.MEAN),0.0001);

        Calculations calcs3 = new Calculations(values[8],types[7]);
        assertNull(calcs3.getResult());
    }
}

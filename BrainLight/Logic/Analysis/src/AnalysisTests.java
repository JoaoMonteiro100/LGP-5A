
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AnalysisTests {

    private final float[][] values = {
            {20f, 20f, 20f, 20f, 20f, 20f},
            {0f, 0f, 100f, 100f},
            {1f, 1f, 2f, 2f},
            {50f, 0f, 65f, 30f},
            {50f},
            {0.25f, 0.26f},
            {50f, 50f},
            {50f, 100f},
            {},
            {0f, 0f}
    };

    private final float[][][] valuesXY = {
            {{0f,20f}, {1f,20f}, {2f,20f}, {3f,20f}},
            {{0f,0f}, {1f,1f}, {2f,2f}},
            {{}},
            {{0f,0f}}
    };

    private final TypesOfCalculations[][] types = {
            {TypesOfCalculations.WAVELENGTH},
            {TypesOfCalculations.WAVENUMBER},
            {TypesOfCalculations.ANG_WAVENUMBER},
            {TypesOfCalculations.ANG_FREQUENCY},
            {TypesOfCalculations.AMPLITUDE},
            {TypesOfCalculations.MAX_AMPLITUDE},
            {TypesOfCalculations.PERIOD},
            {TypesOfCalculations.MAX},
            {TypesOfCalculations.MIN},
            {TypesOfCalculations.MEAN},
            {TypesOfCalculations.MODE},
            {TypesOfCalculations.MEDIAN},
            {TypesOfCalculations.MAX,TypesOfCalculations.MIN,TypesOfCalculations.MEAN},
            {TypesOfCalculations.XFORMAXY},
            {TypesOfCalculations.XFORMINY}
    };

    @Test
    public void testMain() {
        Calculations calcs = new Calculations(values[1],types[0]);
        assertTrue(calcs.getResult().containsKey(TypesOfCalculations.WAVELENGTH));
        assertNotNull(calcs.getResult().values());


        Calculations calcs2 = new Calculations(values[3],types[7]);
        assertTrue(calcs2.getResult().containsKey(TypesOfCalculations.MAX));
        assertNotNull(calcs2.getResult().values());

        Calculations calcs3 = new Calculations(values[8],types[12]);
        assertTrue(calcs3.getResult().containsValue(0f));
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
    public void testAngWaveNumber() {
        Calculations calcs = new Calculations(values[0],types[2]);
        assertEquals(40*Math.PI/25,calcs.getResult().get(TypesOfCalculations.ANG_WAVENUMBER),0.01);

        Calculations calcs2 = new Calculations(values[1],types[2]);
        assertEquals(100*Math.PI/25,calcs2.getResult().get(TypesOfCalculations.ANG_WAVENUMBER),0.01);
    }

    @Test
    public void testAngFrequency() {
        Calculations calcs = new Calculations(values[0],types[3]);
        assertEquals(40*Math.PI,calcs.getResult().get(TypesOfCalculations.ANG_FREQUENCY),0.01);

        Calculations calcs2 = new Calculations(values[1],types[3]);
        assertEquals(100*Math.PI,calcs2.getResult().get(TypesOfCalculations.ANG_FREQUENCY),0.01);
    }

    @Test
    public void testPeakToPeakAmplitude() {
        Calculations calcs = new Calculations(values[0],types[4]);
        assertEquals(0,calcs.getResult().get(TypesOfCalculations.AMPLITUDE),0.5);

        Calculations calcs2 = new Calculations(values[3],types[4]);
        assertEquals(28.3,calcs2.getResult().get(TypesOfCalculations.AMPLITUDE),0.5);
    }

    @Test
    public void testMaxAmplitude() {
        Calculations calcs = new Calculations(values[3],types[5]);
        assertEquals(65,calcs.getResult().get(TypesOfCalculations.MAX_AMPLITUDE),0);
    }

    @Test
    public void testPeriod() {
        Calculations calcs = new Calculations(values[0],types[6]);
        assertEquals(0.05,calcs.getResult().get(TypesOfCalculations.PERIOD),0.001);

        Calculations calcs2 = new Calculations(values[9],types[6]);
        assertEquals(0,calcs2.getResult().get(TypesOfCalculations.PERIOD),0.1);
    }

    @Test
    public void testMax() {
        Calculations calcs = new Calculations(values[0],types[7]);
        assertEquals(20,calcs.getResult().get(TypesOfCalculations.MAX),0);

        Calculations calcs2 = new Calculations(values[3],types[7]);
        assertEquals(65,calcs2.getResult().get(TypesOfCalculations.MAX),0);

        Calculations calcs3 = new Calculations(values[8],types[7]);
        assertTrue(calcs3.getResult().containsValue(0f));
    }

    @Test
    public void testMin() {
        Calculations calcs = new Calculations(values[4],types[8]);
        assertEquals(50,calcs.getResult().get(TypesOfCalculations.MIN),0);

        Calculations calcs2 = new Calculations(values[6],types[8]);
        assertEquals(50,calcs2.getResult().get(TypesOfCalculations.MIN),0);

        Calculations calcs3 = new Calculations(values[7],types[8]);
        assertEquals(50,calcs3.getResult().get(TypesOfCalculations.MIN),0);
    }

    @Test
    public void testMean() {
        Calculations calcs = new Calculations(values[0],types[9]);
        assertEquals(20,calcs.getResult().get(TypesOfCalculations.MEAN),0);

        Calculations calcs2 = new Calculations(values[5],types[9]);
        assertEquals(0.255,calcs2.getResult().get(TypesOfCalculations.MEAN),0.0001);

        Calculations calcs3 = new Calculations(values[8],types[9]);
        assertEquals(0,calcs3.getResult().get(TypesOfCalculations.MEAN),0);
    }

    @Test
    public void testMode() {
        Calculations calcs = new Calculations(values[0],types[10]);
        assertEquals(20,calcs.getResult().get(TypesOfCalculations.MODE),0);

        Calculations calcs2 = new Calculations(values[1],types[10]);
        assertEquals(0,calcs2.getResult().get(TypesOfCalculations.MODE),0);

        Calculations calcs3 = new Calculations(values[3],types[10]);
        assertEquals(0,calcs3.getResult().get(TypesOfCalculations.MODE),0);
    }

    @Test
    public void testMedian() {
        Calculations calcs = new Calculations(values[0],types[11]);
        assertEquals(20,calcs.getResult().get(TypesOfCalculations.MEDIAN),0);

        Calculations calcs2 = new Calculations(values[3],types[11]);
        assertEquals(40,calcs2.getResult().get(TypesOfCalculations.MEDIAN),0);
    }

    @Test
    public void testXForMaxY() {
        Calculations calcs = new Calculations(valuesXY[0],types[13]);
        assertEquals(0, calcs.getResult().get(TypesOfCalculations.XFORMAXY),0);

        Calculations calcs1 = new Calculations(valuesXY[1],types[13]);
        assertEquals(2, calcs1.getResult().get(TypesOfCalculations.XFORMAXY),0);

        Calculations calcs2 = new Calculations(valuesXY[2],types[13]);
        assertEquals(0, calcs2.getResult().get(TypesOfCalculations.XFORMAXY),0);
    }

    @Test
    public void testXForMinY() {
        Calculations calcs = new Calculations(valuesXY[0],types[14]);
        assertEquals(0, calcs.getResult().get(TypesOfCalculations.XFORMINY),0);

        Calculations calcs1 = new Calculations(valuesXY[1],types[14]);
        assertEquals(0, calcs1.getResult().get(TypesOfCalculations.XFORMINY),0);

        Calculations calcs2 = new Calculations(valuesXY[3],types[14]);
        assertEquals(0, calcs2.getResult().get(TypesOfCalculations.XFORMINY),0);
    }
}

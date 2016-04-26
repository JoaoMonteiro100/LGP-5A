import org.junit.Test;

public class Tests {

    final double[][][] values = {
            //always the same value
            {{0.0, 20}, {0.1, 20}, {0.2, 20}, {0.3, 20}, {0.4, 20}, {0.5, 20}},
            //different values
            {{0.01, 0}, {0.02, 0}, {0.03, 100}, {0.04, 100}},
            //different values with decimal result
            {{0.01, 1}, {0.02, 1}, {0.03, 2}, {0.04, 2}},
            //results not ordered by time
            {{0.03, 50}, {0.04, 0}, {0.01, 65}, {0.02, 30}},
            //1 value only
            {{0, 50}},
            //result with 3 decimal units
            {{0.01, 0.25}, {0.02, 0.26}},
            //same values at the same time
            {{0.00, 50}, {0.00, 50}},
            //different values at the same time
            {{0.00, 50}, {0.00, 100}},
    };

    @Test
    public void testMain() {
        //TODO
    }

    @Test
    public void testWaveLength() {
        //TODO
    }

    @Test
    public void testWaveNumber() {
        //TODO
    }

    @Test
    public void testAmplitude() {
        //TODO
    }

    @Test
    public void testFrequency() {
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
        //TODO
    }

    @Test
    public void testMin() {
        //TODO
    }

    @Test
    public void testMean() {
        //TODO
    }
}

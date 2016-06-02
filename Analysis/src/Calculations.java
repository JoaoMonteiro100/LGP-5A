import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by joaom on 26/04/2016.
 */
class Calculations {
    private float[][] values;
    private TypesOfCalculations[] calcs;
    private float frequencyMean;
    private HashMap<TypesOfCalculations, Float> result;
    private final float brainwaveSpeed = 25; //phase velocity in m/s and assuming the subject is thinking. SOURCE: http://hypertextbook.com/facts/2002/DavidParizh.shtml

    Calculations(float[][] values, TypesOfCalculations[] calcs) {
        this.values = values;
        this.calcs = calcs;
        this.result = new HashMap<>();

        //in case any of the values is null, result is null as well
        for(float[] iter : values) {
            if (iter.length == 0) {
                this.result = null;
                return;
            }
        }

        frequencyMean();
        calc();
    }

    //see in what units each calculation is
    static String[] whatUnit(TypesOfCalculations type) {
        String[] meters = new String[]{"Meters", "m"};
        String[] cyclesPerMeter = new String[]{"Cycles per Meter", "cycles/m"};
        String[] radiansPerMeter = new String[]{"Radians per Meter", "rad/m"};
        String[] radiansPerSecond = new String[]{"Radians per Second", "rad/s"};
        String[] hertz = new String[]{"Hertz", "Hz"};
        String[] seconds = new String[]{"Seconds", "s"};

        switch(type) {
            case WAVELENGTH:
                return meters;

            case WAVENUMBER:
                return cyclesPerMeter;

            case ANG_WAVENUMBER:
                return radiansPerMeter;

            case ANG_FREQUENCY:
                return radiansPerSecond;

            case PERIOD:
                return seconds;

            case AMPLITUDE: case MAX_AMPLITUDE: case MAX: case MIN: case MEAN: case MODE: case MEDIAN:
                return hertz;

            default:
                return null;
        }
    }

    //get the results of the calculations
    HashMap<TypesOfCalculations, Float> getResult() {
        return this.result;
    }

    //see what kind of calculations are needed and for each one call the appropriate functions
    private void calc() {

        for(TypesOfCalculations type : this.calcs) {
            switch(type) {
                case WAVELENGTH:
                    this.result.put(TypesOfCalculations.WAVELENGTH, calcWaveLength());

                case WAVENUMBER:
                    this.result.put(TypesOfCalculations.WAVENUMBER, calcWaveNumber());

                case ANG_WAVENUMBER:
                    this.result.put(TypesOfCalculations.ANG_WAVENUMBER, calcAngWaveNumber());

                case ANG_FREQUENCY:
                    this.result.put(TypesOfCalculations.ANG_FREQUENCY, calcAngFrequency());

                case AMPLITUDE:
                    this.result.put(TypesOfCalculations.AMPLITUDE, calcPeakToPeakAmplitude());

                case MAX_AMPLITUDE:
                    this.result.put(TypesOfCalculations.MAX_AMPLITUDE, calcMaxAmplitude());

                case PERIOD:
                    this.result.put(TypesOfCalculations.PERIOD, calcPeriod());

                case MAX:
                    this.result.put(TypesOfCalculations.MAX, calcMax());

                case MIN:
                    this.result.put(TypesOfCalculations.MIN, calcMin());

                case MEAN:
                    this.result.put(TypesOfCalculations.MEAN, calcMean());

                case MODE:
                    this.result.put(TypesOfCalculations.MODE, calcMode());

                case MEDIAN:
                    this.result.put(TypesOfCalculations.MEDIAN, calcMedian());

                default:
                    break;
            }
        }
    }

    private void frequencyMean() {
        float total = 0;
        for(float[] valueTuple : values) {
            total += valueTuple[1];
        }
        this.frequencyMean = total / values.length;
    }

    private float[] sortedFrequencies() {
        float[] frequencies = new float[this.values.length];
        int i = 0;

        for(float[] value : this.values) {
            frequencies[i] = value[1];
            i++;
        }
        Arrays.sort(frequencies);
        return frequencies;
    }

    private float calcWaveLength() {
        if (this.frequencyMean != 0)
            return brainwaveSpeed / this.frequencyMean;
        else
            return 0;
    }

    private float calcWaveNumber() {
        return this.frequencyMean / brainwaveSpeed;
    }

    private float calcAngWaveNumber() {
        return (2 * (float) Math.PI * this.frequencyMean) / brainwaveSpeed;
    }

    private float calcAngFrequency() {
        return (2 * (float) Math.PI * this.frequencyMean);
    }

    private float calcPeakToPeakAmplitude() {
        int maxCounter = 0, minCounter = 0, peakChange = 0;

        for(int i = 0; i < this.values.length; i++) {

            //if it's the first value we just compare it to the next
            if (i == 0) {
                if (values[i][1] > values[i+1][1]) {
                    maxCounter += values[i][1];
                    peakChange++;
                }
                else if (values[i][1] < values[i+1][1]) {
                    minCounter += values[i][1];
                    peakChange++;
                }
            }

            //if it's the last value we just compare it to the previous
            else if (i == this.values.length-1) {
                if (values[i][1] > values[i-1][1]) {
                    maxCounter += values[i][1];
                    peakChange++;
                }
                else if (values[i][1] < values[i-1][1]) {
                    minCounter += values[i][1];
                    peakChange++;
                }
            }

            //if it's any other value we compare it to both the previous and next values
            else {
                if (values[i][1] > values[i-1][1] && values[i][1] > values[i+1][1]) {
                    maxCounter += values[i][1];
                    peakChange++;
                }
                else if (values[i][1] < values[i-1][1] && values[i][1] < values[i+1][1]) {
                    minCounter += values[i][1];
                    peakChange++;
                }
            }
        }

        if(peakChange!=0)
            return (maxCounter - minCounter) / (peakChange - 1);
        else
            return 0;
    }

    private float calcMaxAmplitude() {
        return calcMax() - calcMin();
    }

    private float calcPeriod() {
        if (this.frequencyMean != 0)
            return 1 / this.frequencyMean;
        else
            return 0;
    }

    private float calcMax() {
        float max = 0;
        for(float[] number : this.values) {
            if(number[1] > max) {
                max = number[1];
            }
        }
        return max;
    }

    private float calcMin() {
        float min = 999999;
        for(float[] number : this.values) {
            if(number[1] < min) {
                min = number[1];
            }
        }
        return min;
    }

    private float calcMean() {
        return this.frequencyMean;
    }

    private float calcMode() {
        float[] frequencies = sortedFrequencies();

        float maxValue = 0;
        int maxCount = 0;

        for (int i = 0; i < frequencies.length; i++) {
            int count = 0;
            for (int j = 0; j < frequencies.length; j++) {
                if (frequencies[j] == frequencies[i])
                    count++;
            }
            if (count > maxCount) {
                maxCount = count;
                maxValue = frequencies[i];
            }
        }

        return maxValue;
    }

    private float calcMedian() {

        float[] frequencies = sortedFrequencies();

        int middle = frequencies.length/2;
        if (frequencies.length % 2 == 1) {
            return frequencies[middle];
        } else {
            return (frequencies[middle-1] + frequencies[middle]) / 2;
        }
    }
}

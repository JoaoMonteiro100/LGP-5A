package analysis;

import java.util.Arrays;
import java.util.HashMap;

public class Calculations {
    private float[] values;
    private float[][] valuesXY;
    private TypesOfCalculations[] calcs;
    private float mean;
    private HashMap<TypesOfCalculations, Float> result;
    private final float brainwaveSpeed = 25; //phase velocity in m/s and assuming the subject is thinking. SOURCE: http://hypertextbook.com/facts/2002/DavidParizh.shtml

    /**
     * Constructor
     * @param values Values to be analysed
     * @param calcs Enums representing the types of calculations desired
     */
    public Calculations(float[] values, TypesOfCalculations[] calcs) {
    	
        this.values = values;
        this.calcs = calcs;
        this.result = new HashMap<>();

        mean();
        calc();
    }

    /**
     * Constructor
     * @param valuesXY Values to be analysed (x and y) --> y is used for single-array calculations
     * @param calcs Enums representing the types of calculations desired
     */
    public Calculations(float[][] valuesXY, TypesOfCalculations[] calcs) {

        //in case valuesXY is null
        if(valuesXY[0].length == 0 || valuesXY.length == 0) {
            this.valuesXY = new float[][] {{0,0}};
            this.values = new float[] {0};
        }
        else {
            this.valuesXY = valuesXY;
            this.values = getYValues(valuesXY);

        }

        this.calcs = calcs;
        this.result = new HashMap<>();

        mean();
        calc();
    }

    /**
     * Takes an array of arrays and returns an array made only of the values in the second position of each original sub-array
     * @return Array with values in index 1 of original sub-arrays
     */
    private float[] getYValues(float[][] xyValues) {
        float[] result = new float[xyValues.length];
        for(int i = 0; i < xyValues.length; i++) {
            result[i] = xyValues[i][1];
        }
        return result;
    }

    /**
     * Get the results of the calculations
     * @return Result of the calcs made
     */
    public HashMap<TypesOfCalculations, Float> getResult() {
        return this.result;
    }

    /**
     * See what kind of calculations are needed and for each one call the appropriate functions
     */
    private void calc() {

        for(TypesOfCalculations type : this.calcs) {
            switch(type) {
                case WAVELENGTH:
                    this.result.put(TypesOfCalculations.WAVELENGTH, calcWaveLength());
                    break;

                case WAVENUMBER:
                    this.result.put(TypesOfCalculations.WAVENUMBER, calcWaveNumber());
                    break;

                case ANG_WAVENUMBER:
                    this.result.put(TypesOfCalculations.ANG_WAVENUMBER, calcAngWaveNumber());
                    break;

                case ANG_FREQUENCY:
                    this.result.put(TypesOfCalculations.ANG_FREQUENCY, calcAngFrequency());
                    break;

                case AMPLITUDE:
                    this.result.put(TypesOfCalculations.AMPLITUDE, calcPeakToPeakAmplitude());
                    break;

                case MAX_AMPLITUDE:
                    this.result.put(TypesOfCalculations.MAX_AMPLITUDE, calcMaxAmplitude());
                    break;

                case PERIOD:
                    this.result.put(TypesOfCalculations.PERIOD, calcPeriod());
                    break;

                case MAX:
                    this.result.put(TypesOfCalculations.MAX, calcMax());
                    break;

                case MIN:
                    this.result.put(TypesOfCalculations.MIN, calcMin());
                    break;

                case MEAN:
                    this.result.put(TypesOfCalculations.MEAN, calcMean());
                    break;

                case MODE:
                    this.result.put(TypesOfCalculations.MODE, calcMode());
                    break;

                case MEDIAN:
                    this.result.put(TypesOfCalculations.MEDIAN, calcMedian());
                    break;

                case XFORMAXY:
                    this.result.put(TypesOfCalculations.XFORMAXY, calcXForMaxY());
                    break;

                case XFORMINY:
                    this.result.put(TypesOfCalculations.XFORMINY, calcXForMinY());
                    break;

                default:
                    break;
            }
        }
    }

    /**
     * Calculate the mean of the values given
     */
    private void mean() {
        float total = 0;
        for(float value : values) {
            total += value;
        }
        if(values.length>0) {
            this.mean = total / values.length;
        }
        else {
            this.mean = 0;
        }
    }

    /**
     * Sort values given by ascending order
     * @return Sorted values
     */
    private float[] sortedValues() {
        float[] sortedValues = new float[this.values.length];
        int i = 0;

        for(float value : this.values) {
            sortedValues[i] = value;
            i++;
        }

        Arrays.sort(sortedValues);
        return sortedValues;
    }

    /**
     * Calculate the wavelength (if given frequency values)
     * @return Wavelength
     */
    private float calcWaveLength() {
        if (this.mean != 0)
            return brainwaveSpeed / this.mean;
        else
            return 0;
    }

    /**
     * Calculate the wavenumber (if given frequency values)
     * @return Wavenumber
     */
    private float calcWaveNumber() {
        return this.mean / brainwaveSpeed;
    }

    /**
     * Calculate the angular wavenumber (if given frequency values)
     * @return Angular wavenumber
     */
    private float calcAngWaveNumber() {
        return (2 * (float) Math.PI * this.mean) / brainwaveSpeed;
    }

    /**
     * Calculate the angular frequency (if given frequency values)
     * @return Angular frequency
     */
    private float calcAngFrequency() {
        return (2 * (float) Math.PI * this.mean);
    }

    /**
     * Calculate the amplitude from peak to peak (relative minimums and maximums)
     * @return Amplitude
     */
    private float calcPeakToPeakAmplitude() {
        int maxCounter = 0, minCounter = 0, peakChange = 0;

        for(int i = 0; i < this.values.length; i++) {

            //if it's the first value we just compare it to the next
            if (i == 0) {
                if (values[i] > values[i+1]) {
                    maxCounter += values[i];
                    peakChange++;
                }
                else if (values[i]< values[i+1]) {
                    minCounter += values[i];
                    peakChange++;
                }
            }

            //if it's the last value we just compare it to the previous
            else if (i == this.values.length-1) {
                if (values[i] > values[i-1]) {
                    maxCounter += values[i];
                    peakChange++;
                }
                else if (values[i] < values[i-1]) {
                    minCounter += values[i];
                    peakChange++;
                }
            }

            //if it's any other value we compare it to both the previous and next values
            else {
                if (values[i] > values[i-1] && values[i] > values[i+1]) {
                    maxCounter += values[i];
                    peakChange++;
                }
                else if (values[i] < values[i-1] && values[i] < values[i+1]) {
                    minCounter += values[i];
                    peakChange++;
                }
            }
        }

        if(peakChange!=0)
            return (maxCounter - minCounter) / (peakChange - 1);
        else
            return 0;
    }

    /**
     * Calculate the maximum amplitude of the wave
     * @return Maximum amplitude
     */
    private float calcMaxAmplitude() {
        return calcMax() - calcMin();
    }

    /**
     * Calculate the period (if given frequency values)
     * @return Period
     */
    private float calcPeriod() {
        if (this.mean != 0)
            return 1 / this.mean;
        else
            return 0;
    }

    /**
     * Calculate the maximum value
     * @return Maximum
     */
    private float calcMax() {
        float max = 0;
        for(float number : this.values) {
            if(number > max) {
                max = number;
            }
        }
        return max;
    }

    /**
     * Calculate the minimum value
     * @return Minimum
     */
    private float calcMin() {
        float min = 999999;
        for(float number : this.values) {
            if(number < min) {
                min = number;
            }
        }
        return min;
    }

    /**
     * Calculate the mean value
     * @return Mean
     */
    private float calcMean() {
        return this.mean;
    }

    /**
     * Calculate the mode value
     * @return Mode
     */
    private float calcMode() {
        float[] frequencies = sortedValues();

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

    /**
     * Calculate the median value
     * @return Median
     */
    private float calcMedian() {

        float[] frequencies = sortedValues();

        int middle = frequencies.length/2;
        if (frequencies.length % 2 == 1) {
            return frequencies[middle];
        } else {
            return (frequencies[middle-1] + frequencies[middle]) / 2;
        }
    }

    /**
     * Returns the x value of the first highest Y value
     * @return X value
     */
    private float calcXForMaxY() {
        float xForMaxY = 0, maxY = 0;
        for(float[] point: this.valuesXY) {
            if(point[1] > maxY) {
                xForMaxY = point[0];
                maxY = point[1];
            }
        }
        return xForMaxY;
    }

    /**
     * Returns the x value of the first lowest Y value
     * @return X value
     */
    private float calcXForMinY() {
        float xForMinY = 0, minY = 999999;
        for(float[] point: this.valuesXY) {
            if(point[1] < minY) {
                xForMinY = point[0];
                minY = point[1];
            }
        }
        return xForMinY;
    }
}

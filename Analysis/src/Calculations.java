import java.util.HashMap;

/**
 * Created by joaom on 26/04/2016.
 */
class Calculations {
    private static HashMap<TypesOfCalculations, String[]> units;
    static
    {
        units.put(TypesOfCalculations.WAVELENGTH, new String[] {"Meter", "m"});
        units.put(TypesOfCalculations.WAVENUMBER, new String[] {"Cycles per Meter", "c/m"});
        units.put(TypesOfCalculations.AMPLITUDE, new String[] {"Volts per Meter", "V/m"});
        units.put(TypesOfCalculations.PERIOD, new String[] {"Second", "s"});
        units.put(TypesOfCalculations.VELOCITY, new String[] {"Meter per Second", "m/s"});
        units.put(TypesOfCalculations.MAX, new String[] {"Hertz", "Hz"});
        units.put(TypesOfCalculations.MIN, new String[] {"Hertz", "Hz"});
        units.put(TypesOfCalculations.MEAN, new String[] {"Hertz", "Hz"});
    }

    private float[][] values;
    private TypesOfCalculations[] calcs;
    private float frequencyMean;
    private HashMap<TypesOfCalculations, Float> result;
    private final float brainwaveSpeed = 25; //in m/s and assuming the subject is thinking. SOURCE: http://hypertextbook.com/facts/2002/DavidParizh.shtml

    Calculations(float[][] values, TypesOfCalculations[] calcs) {
        this.values = values;
        this.calcs = calcs;
        this.result = new HashMap<TypesOfCalculations, Float>();

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

    HashMap<TypesOfCalculations, Float> getResult() {
        return this.result;
    }

    public HashMap<TypesOfCalculations, String[]> getUnits() {
        return units;
    }

    //see what kind of calculations are needed and for each one call the appropriate functions
    private void calc() {

        for(TypesOfCalculations type : this.calcs) {
            switch(type) {
                case WAVELENGTH:
                    this.result.put(TypesOfCalculations.WAVELENGTH, calcWaveLength(values));

                case WAVENUMBER:
                    this.result.put(TypesOfCalculations.WAVENUMBER, calcWaveNumber(values));

                case AMPLITUDE:
                    this.result.put(TypesOfCalculations.AMPLITUDE, calcAmplitude(values));

                case PERIOD:
                    this.result.put(TypesOfCalculations.PERIOD, calcPeriod(values));

                case VELOCITY:
                    this.result.put(TypesOfCalculations.VELOCITY, calcVelocity(values));

                case MAX:
                    this.result.put(TypesOfCalculations.MAX, calcMax(values));

                case MIN:
                    this.result.put(TypesOfCalculations.MIN, calcMin(values));

                case MEAN:
                    this.result.put(TypesOfCalculations.MEAN, calcMean(values));

                default:
                    break;
            }
        }
    }

    private void frequencyMean() {
        float total = 0;
        int i = 0;

        for(float[] valueTuple : values) {
            total += valueTuple[1];
            i++;
        }

        this.frequencyMean = total / i;
    }

    private float calcWaveLength(float[][] values) {
        return brainwaveSpeed / this.frequencyMean;
    }

    private float calcWaveNumber(float[][] values) {
        return this.frequencyMean / brainwaveSpeed;
    }

    private float calcAmplitude(float[][] values) {
        //TODO
        return 0;
    }

    private float calcPeriod(float[][] values) {
        //TODO
        return 0;
    }

    private float calcVelocity(float[][] values) {
        //TODO
        return 0;
    }

    private float calcMax(float[][] values) {
        //TODO
        return 0;
    }

    private float calcMin(float[][] values) {
        //TODO
        return 0;
    }

    private float calcMean(float[][] values) {
        return this.frequencyMean;
    }
}

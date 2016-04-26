import java.util.HashMap;

/**
 * Created by joaom on 26/04/2016.
 */
public class Calculations {

    //see what kind of calculations are needed and for each one call the appropriate functions
    public HashMap<TypesOfCalculations, Double> Calculations(double[][] values, TypesOfCalculations[] calcs) {

        //hashmap of types of calcs and their respective results
        HashMap<TypesOfCalculations, Double> result = new HashMap<>();

        for(TypesOfCalculations type : calcs) {
            switch(type) {
                case WAVELENGTH:
                    result.put(TypesOfCalculations.WAVELENGTH, calcWaveLength(values));

                case WAVENUMBER:
                    result.put(TypesOfCalculations.WAVENUMBER, calcWaveNumber(values));

                case AMPLITUDE:
                    result.put(TypesOfCalculations.AMPLITUDE, calcAmplitude(values));

                case FREQUENCY:
                    result.put(TypesOfCalculations.FREQUENCY, calcFrequency(values));

                case PERIOD:
                    result.put(TypesOfCalculations.PERIOD, calcPeriod(values));

                case VELOCITY:
                    result.put(TypesOfCalculations.VELOCITY, calcVelocity(values));

                case MAX:
                    result.put(TypesOfCalculations.MAX, calcMax(values));

                case MIN:
                    result.put(TypesOfCalculations.MIN, calcMin(values));

                case MEAN:
                    result.put(TypesOfCalculations.MEAN, calcMean(values));

                default:
                    break;
            }
        }

        if(result.isEmpty()) {
            return null;
        }

        return result;
    }

    private double calcWaveLength(double[][] values) {
        //TODO
        return 0;
    }

    private double calcWaveNumber(double[][] values) {
        //TODO
        return 0;
    }

    private double calcAmplitude(double[][] values) {
        //TODO
        return 0;
    }

    private double calcFrequency(double[][] values) {
        //TODO
        return 0;
    }

    private double calcPeriod(double[][] values) {
        //TODO
        return 0;
    }

    private double calcVelocity(double[][] values) {
        //TODO
        return 0;
    }

    private double calcMax(double[][] values) {
        //TODO
        return 0;
    }

    private double calcMin(double[][] values) {
        //TODO
        return 0;
    }

    private double calcMean(double[][] values) {
        //TODO
        return 0;
    }
}

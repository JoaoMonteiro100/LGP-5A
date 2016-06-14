package Iedk;

public class Channel {
    public double sample[];
    public double fourierCos[];
    public double fourierSin[];
    public double magnitude[];
    public double delta, theta, alpha, beta, overall, meditation;

    public Channel() {
        sample = new double[128];
        fourierCos = new double[128];
        fourierSin = new double[128];
        magnitude = new double[128];
    }
}

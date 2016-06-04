package Iedk;


import java.util.HashMap;

public class Wave {
    private Double alpha, beta, delta, theta;
    private HashMap<Integer, Double> freqVals;
    private int signalQuality;
    private Channel channel;


    public Wave(Channel channel, int signalQuality) {
        this.alpha = channel.alpha;
        this.beta = channel.beta;
        this.delta = channel.delta;
        this.theta = channel.theta;
        this.signalQuality = signalQuality;

        freqVals = new HashMap<>();

        for(int i = 0; i <= 30; i++)
            freqVals.put(i, 20 * Math.log10(Math.abs(channel.magnitude[i])));
    }


    public Double getAlpha() {
        return alpha;
    }

    public Double getBeta() {
        return beta;
    }

    public Double getDelta() {
        return delta;
    }

    public Double getTheta() {
        return theta;
    }

    public HashMap<Integer, Double> getFreqVals() {
        return freqVals;
    }

    public Channel getChannel() {
        return channel;
    }

    public int getSignalQuality() {
        return signalQuality;
    }
}

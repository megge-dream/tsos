package tsos.equalizer.core;


public class IIRCoefficients {
    public double beta;
    public double alpha;
    public double gamma;

    public IIRCoefficients(double beta, double alpha, double gamma) {
        this.beta = beta;
        this.alpha = alpha;
        this.gamma = gamma;
    }
}

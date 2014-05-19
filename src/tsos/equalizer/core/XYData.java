package tsos.equalizer.core;


public class XYData {
    /**
     * X data
     */
    public double x[] = new double[3]; /* x[n], x[n-1], x[n-2] */
    /**
     * Y data
     */
    public double y[] = new double[3]; /* y[n], y[n-1], y[n-2] */

    /**
     * Constructs new XYData object
     */
    public XYData() {
        zero();
    }

    /**
     * Fills all content with zero
     */
    public void zero() {
        for (int i = 0; i < 3; i++) {
            x[i] = 0;
            y[i] = 0;
        }
    }
}

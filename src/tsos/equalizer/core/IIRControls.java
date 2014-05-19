package tsos.equalizer.core;

public class IIRControls {
    /**
     * Volume gain
     * values should be between 0.0 and 1.0
     */
    private float preamp[];
    /**
     * Gain for each band
     * values should be between -0.2 and 1.0
     */
    private float bands[][];

    /**
     * Creates new IIRControls object for given number of bands
     *
     * @param bandsnum is the number of bands
     * @param channels is the number of channels
     */
    public IIRControls(int bandsnum, int channels) {
        preamp = new float[channels];
        bands = new float[bandsnum][channels];
        for (int j = 0; j < channels; j++) {
            preamp[j] = 1.0f;
            for (int i = 0; i < bandsnum; i++)
                bands[i][j] = 0f;
        }
    }

    /**
     * Returns the maximum value for band control
     *
     * @return the maximum value for band control
     */
    public float getMaximumBandValue() {
        return 1.0f;
    }

    /**
     * Returns the minimum value for band control
     *
     * @return the minimum value for band control
     */
    public float getMinimumBandValue() {
        return -0.2f;
    }

    /**
     * Returns the maximum value for band control (in Db)
     *
     * @return the maximum value for band control
     */
    public float getMaximumBandDbValue() {
        return 12f;
    }

    /**
     * Returns the minimum value for band control (in Db)
     *
     * @return the minimum value for band control
     */
    public float getMinimumBandDbValue() {
        return -12f;
    }

    /**
     * Returns the maximum value for preamp control
     *
     * @return the maximum value for preamp control
     */
    public float getMaximumPreampValue() {
        return 1.0f;
    }

    /**
     * Returns the minimum value for preamp control
     *
     * @return the minimum value for preamp control
     */
    public float getMinimumPreampValue() {
        return 0f;
    }

    /**
     * Returns the maximum value for preamp control (in Db)
     *
     * @return the maximum value for preamp control
     */
    public float getMaximumPreampDbValue() {
        return 12f;
    }

    /**
     * Returns the minimum value for preamp control (in Db)
     *
     * @return the minimum value for preamp control
     */
    public float getMinimumPreampDbValue() {
        return -12f;
    }

    /**
     * Returns bands array
     *
     * @return bands array
     */
    float[][] getBands() {
        return bands;
    }

    /**
     * Returns preamp array
     *
     * @return preamp array
     */
    float[] getPreamp() {
        return preamp;
    }

    /**
     * Returns value of control for given band and channel
     *
     * @param band    is the index of band
     * @param channel is the index of channel
     * @return the value
     */
    public float getBandValue(int band, int channel) {
        return bands[band][channel];
    }

    /**
     * Setter for value of control for given band and channel
     *
     * @param band    is the index of band
     * @param channel is the index of channel
     * @param value   is the new value
     */
    public void setBandValue(int band, int channel, float value) {
        bands[band][channel] = value;
    }

    /**
     * Setter for value of control for given band and channel (in Db)
     *
     * @param band    is the index of band
     * @param channel is the index of channel
     * @param value   is the new value
     */
    public void setBandDbValue(int band, int channel, float value) {
        /* Map the gain and preamp values */
        /* -12dB .. 12dB mapping */
        bands[band][channel] = (float) (2.5220207857061455181125E-01 *
                Math.exp(8.0178361802353992349168E-02 * value)
                - 2.5220207852836562523180E-01);
    }

    /**
     * Returns value of preamp control for given channel
     *
     * @param channel is the index of channel
     * @return the value
     */
    public float getPreampValue(int channel) {
        return preamp[channel];
    }

    /**
     * Setter for value of preamp control for given channel
     *
     * @param channel is the index of channel
     * @param value   is the new value
     */
    public void setPreampValue(int channel, float value) {
        preamp[channel] = value;
    }

    /**
     * Setter for value of preamp control for given channel (in Db)
     *
     * @param channel is the index of channel
     * @param value   is the new value
     */
    public void setPreampDbValue(int channel, float value) {
        /* -12dB .. 12dB mapping */
        preamp[channel] = (float) (9.9999946497217584440165E-01 *
                Math.exp(6.9314738656671842642609E-02 * value)
                + 3.7119444716771825623636E-07);
    }

    /***** EFFECTS ****/

    public float[] vibrato(IIRControls con, float i, float direction) {
        i += 0.5f * direction;
        con.setPreampValue(0, i);
        if ((i >= 1.0f) || (i <= 0.11f)) {
            direction = direction*(-1.0f);
        }
        float iAndDir[] = new float[2];
        iAndDir[0] = i;
        iAndDir[1] = direction;
        return iAndDir;
    }

    public float[] distortion(IIRControls con, float[] bandDbVal) {
        for (int i = 0; i < 10; i++){
            float max = 3.0f;
            if (bandDbVal[i] > max)
                bandDbVal[i] = max;
            if (bandDbVal[i] < -max)
                bandDbVal[i] = -max;
        }
        return bandDbVal;
    }

    public byte[] reverberation(byte[] oldBuffer, byte[] buffer) {
        int delay = 10000;
        for (int i = 0; i < buffer.length-delay; i++) {
            buffer[i+delay] += buffer[i]*0.7f;
        }
        // почему-то шипит
        /*for (int i = 0; i < delay; i++)
        {
            buffer[i] += oldBuffer[oldBuffer.length - delay + i-1] * 0.7f;
        }//*/
        return buffer;
    }

    public byte[] eho(byte[] oldBuffer, byte[] buffer) {
        int delay = 30000;
        for (int i = buffer.length-1; i >= delay; i--) {
            buffer[i] += buffer[i-delay]*0.7f;
        }
        // почему-то шипит, если откомментить
       /* for (int i = 0; i < delay; i++)
        {
            buffer[i] += oldBuffer[oldBuffer.length - delay + i-1] * 0.7f;
        }//*/
        return buffer;
    }
}

package com.music.wav.fft;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FFT {
    public List<double[]> transformForward(double[] input, int windowSize) {
        List<double[]> windowList = splitWavIntoWindows(input, windowSize);

        return transformWindows(windowList, TransformType.FORWARD);
    }

    public double[] transformBackward(List<double[]> input, int windowSize) {
        List<double[]> doubles = transformWindows(input, TransformType.INVERSE);

        return concatenateListToCreateWav(windowSize, doubles);
    }

    private double[] concatenateListToCreateWav(int windowSize, List<double[]> doubles) {
        int numberOfSamples = windowSize * doubles.size();
        double[] wav = new double[numberOfSamples];
        int counter = 0;
        for (double[] aDouble : doubles) {
            for (double value : aDouble) {
                wav[counter] = value;
                counter += 1;
            }
        }
        return wav;
    }

    private List<double[]> splitWavIntoWindows(double[] input, int windowSize) {
        List<double[]> windowList = new ArrayList<double[]>();
        double[] window = new double[windowSize];

        int counter = 0;
        for (double value : input) {
            if (counter < windowSize) {
                window[counter] = value;
                counter += 1;
            } else {
                counter = 0;
                windowList.add(Arrays.copyOf(window, windowSize));
                window = new double[windowSize];
            }
        }
        return windowList;
    }

    private List<double[]> transformWindows(List<double[]> windowList, TransformType transformType) {
        List<double[]> fftTransforms = new ArrayList<double[]>();

        for (double[] doubles : windowList) {
            fftTransforms.add(transformWindow(doubles, transformType));
        }

        return fftTransforms;
    }

    private double[] transformWindow(double[] window, TransformType transformType) {
        double[] tempConversion = new double[window.length];

        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.UNITARY);
        try {
            Complex[] complex = transformer.transform(window, transformType);

            for (int i = 0; i < complex.length; i++) {
                double rr = (complex[i].getReal());
                double ri = (complex[i].getImaginary());

                tempConversion[i] = Math.sqrt((rr * rr) + (ri * ri));
            }

        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }

        return tempConversion;
    }

}

package com.music.wav.fft;

import com.music.wav.io.WavFile;
import com.music.wav.io.WavFileException;

import java.io.File;
import java.io.IOException;

public class WavTransformer {

    public void writeArrayToWavFile(double[] wavArray, File outputWavFile) throws IOException, WavFileException {
        WavFile wavFile = WavFile.newWavFile(outputWavFile, 1, wavArray.length, 16, 44100);

        wavFile.writeFrames(wavArray, wavArray.length);
        wavFile.display();
        wavFile.close();
    }

    public double[] readWavFileToArray(File waveFile) throws IOException, WavFileException {
        WavFile inputWavFile = WavFile.openWavFile(waveFile);
        inputWavFile.display();
        int numFrames = safeLongToInt(inputWavFile.getNumFrames());
        double[][] buffer = new double[2][numFrames];

        inputWavFile.readFrames(buffer, (int) numFrames);

        inputWavFile.close();

        //This just returns the first channel
        return buffer[0];
    }

    private int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
}

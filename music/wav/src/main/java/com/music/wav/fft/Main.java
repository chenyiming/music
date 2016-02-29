package com.music.wav.fft;

import com.music.wav.io.WavFileException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static ArrayWriter arrayWriter = new ArrayWriter();
    private static String file="01b";

    public static void main(String[] args) throws IOException, WavFileException {
//        testSuite();
        readWavAndWriteToAnalysis();
        readAnalysisAndWriteToWav();
    }

    private static void testSuite() throws IOException, WavFileException {
        fftIdentity();
        readAnalysisAndWriteToWav();
        wavArrayTransformationIdentity();
    }

    private static void readAnalysisAndWriteToWav() throws IOException, WavFileException {
        int windowSize = 8192;
        List<double[]> analysis = arrayWriter.readAnalysisFromFile(windowSize, new File("D:\\金山快盘\\音乐识别\\素材\\"+file+".txt"));

        FFT fft = new FFT();
        double[] wav = fft.transformBackward(analysis, windowSize);
        System.out.println("transformed backward");

        WavTransformer wavTransformer = new WavTransformer();
        wavTransformer.writeArrayToWavFile(wav, new File("D:\\金山快盘\\音乐识别\\素材\\"+file+"b.wav"));
        System.out.println("wrote wav file");
    }

    private static void readWavAndWriteToAnalysis() throws IOException, WavFileException {
        long startTime = System.currentTimeMillis();
        WavTransformer wavTransformer = new WavTransformer();
        double[] wavArray = wavTransformer.readWavFileToArray(new File("D:\\金山快盘\\音乐识别\\素材\\"+file+".wav"));

        FFT fft = new FFT();

        int windowSize = 1024;
        System.out.println("Window size: " + windowSize);
        List<double[]> analysis = fft.transformForward(wavArray, windowSize);
        System.out.println("transformed forward");

        List<int[]> compressedAnalysis = compressAnalysis(analysis, windowSize);
        System.out.println("Compressed analysis");

        arrayWriter.writeCompressedAnalysisToFile(compressedAnalysis, new File("D:\\金山快盘\\音乐识别\\素材\\"+file+".txt"));
        long endTime = System.currentTimeMillis();
        long secondsItTookToDoThis = (endTime - startTime) / 1000;
        System.out.println("Total time in seconds: " + secondsItTookToDoThis);

    }

    private static List<int[]> compressAnalysis(List<double[]> analysis, int windowSize) {
        int lowestBucketWithNonZeroValue = windowSize;
        int highestBucketWithNonZeroValue = -1;

        List<int[]> compressedAnalysis = new ArrayList<int[]>();
        for (double[] window : analysis) {
            int[] newWindow = new int[window.length];
            for (int c = 0; c < window.length; c++) {
                int valueOfBucket = Double.valueOf(window[c]).intValue();
                if (valueOfBucket > 0 && c < lowestBucketWithNonZeroValue) {
                    lowestBucketWithNonZeroValue = c;
                }
                if (valueOfBucket > 0 && highestBucketWithNonZeroValue < c) {
                    highestBucketWithNonZeroValue = c;
                }
                newWindow[c] = valueOfBucket;
            }
            compressedAnalysis.add(newWindow);
        }
        System.out.println("Lowest bucket with non zero value: " + lowestBucketWithNonZeroValue);
        System.out.println("highest bucket with non zero value: " + highestBucketWithNonZeroValue);

        return compressedAnalysis;
    }

    private static void fftIdentity() throws IOException, WavFileException {
        WavTransformer wavTransformer = new WavTransformer();
        double[] wavArray = wavTransformer.readWavFileToArray(new File("/Users/Thoughtworker/leave/wav/src/main/resources/prelude.wav"));

        FFT fft = new FFT();

        int windowSize = 8192;
        System.out.println("Window size: " + windowSize);
        List<double[]> analysis = fft.transformForward(wavArray, windowSize);
        System.out.println("transformed forward");

        double[] wav = fft.transformBackward(analysis, windowSize);
        System.out.println("transformed backward");

        wavTransformer.writeArrayToWavFile(wav, new File("/Users/Thoughtworker/leave/wav/src/main/resources/holyFuckThatWorked" + windowSize + ".wav"));
        System.out.println("wrote wav file");

        arrayWriter.writeAnalysisToFile(analysis, new File("/Users/Thoughtworker/leave/wav/src/main/resources/analysis" + windowSize + ".txt"));
        int expectedNumberOfTransforms = wavArray.length / windowSize;
        System.out.println("Number of expected transforms: " + expectedNumberOfTransforms);
        System.out.println("Number of transforms: " + analysis.size());
    }

    private static void wavArrayTransformationIdentity() throws IOException, WavFileException {
        WavTransformer wavTransformer = new WavTransformer();
        ArrayWriter arrayWriter = new ArrayWriter();

        double[] wavArray = wavTransformer.readWavFileToArray(new File("/Users/Thoughtworker/leave/wav/src/main/resources/snares.wav"));

        File intermediateText = new File("/Users/Thoughtworker/leave/wav/intermediate.txt");
        arrayWriter.writeArrayToFile(wavArray, intermediateText);

        double[] newWavArray = arrayWriter.wavArrayFromFile(intermediateText);

        wavTransformer.writeArrayToWavFile(newWavArray, new File("/Users/Thoughtworker/leave/wav/src/main/resources/test.wav"));
        wavTransformer.writeArrayToWavFile(wavArray, new File("/Users/Thoughtworker/leave/wav/src/main/resources/test2.wav"));
    }
}

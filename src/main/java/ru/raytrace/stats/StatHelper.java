package ru.raytrace.stats;

import ru.raytrace.parser.Parser;

public class StatHelper {

    private int totalWrites;
    private int stringWrites;
    private int floatWrites;
    private int integerWrites;
    private double maxOfValues;
    private double minOfValues;
    private double averageOfValues = 0;
    private double sumOfValues;
    private int minSizeOfString = 0;
    private int maxSizeOfString = 0;
    private boolean isFullStats;
    private boolean firstString = true;
    private boolean firstValue = true;

    public StatHelper(boolean isFullStats) {
        this.isFullStats = isFullStats;
    }

    public void log(String value, Parser.Type type) {
        if (type.equals(Parser.Type.STRING)) {
            logString(value);
        }
        else if (type.equals(Parser.Type.FLOAT)) {
            logFloat(Float.parseFloat(value));
        }
        else if (type.equals(Parser.Type.INT)) {
            logInteger(Integer.parseInt(value));
        }
    }

    private void logInteger(int value) {
        sumOfValues += value;
        calcMaxOfValues(value);
        calcMinOfValues(value);
        integerWrites++;
    }

    private void logFloat(float value) {
        sumOfValues += value;
        calcMaxOfValues(value);
        calcMinOfValues(value);
        floatWrites++;
    }

    private void logString(String string) {
        calcMaxOfStrings(string);
        calcMinOfStrings(string);
        stringWrites++;
    }

    private void calcMaxOfValues(float value) {
        maxOfValues = Math.max(value, maxOfValues);
    }

    private void calcMinOfValues(float value) {
        if (firstValue) {
            minOfValues = value;
            firstValue = false;
        }
        minOfValues = Math.min(minOfValues, value);
    }

    private void calcAverageOfValues() {
        averageOfValues = sumOfValues / (integerWrites + floatWrites);
    }

    private void calcTotalWrites() {
        totalWrites += stringWrites + floatWrites + integerWrites;
    }

    private void calcMaxOfStrings(String string) {
        maxSizeOfString = Math.max(string.length(), maxSizeOfString);
    }

    private void calcMinOfStrings(String string) {
        if (firstString) {
            minSizeOfString = string.length();
            firstString = false;
        }
        minSizeOfString = Math.min(string.length(), minSizeOfString);
    }

    @Override
    public String toString() {
        calcTotalWrites();
        if (isFullStats) {
            calcAverageOfValues();
            return "totalWrites=" + totalWrites +
                    ", stringWrites=" + stringWrites +
                    ", floatWrites=" + floatWrites +
                    ", integerWrites=" + integerWrites +
                    ", maxOfValues=" + maxOfValues +
                    ", minOfValues=" + minOfValues +
                    ", averageOfValues=" + averageOfValues +
                    ", sumOfValues=" + sumOfValues +
                    ", minSizeOfString=" + minSizeOfString +
                    ", maxSizeOfString=" + maxSizeOfString;
        }

        return "totalWrites=" + totalWrites +
                ", stringWrites=" + stringWrites +
                ", floatWrites=" + floatWrites +
                ", integerWrites=" + integerWrites;
    }

}

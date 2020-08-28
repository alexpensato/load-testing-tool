package org.pensatocode.loadtest.core.util;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public final class StatsUtil {

    private StatsUtil(){
        // Util
    }

    public static void printStats(DescriptiveStatistics descriptiveStatistics, String label) {
        System.out.println("*****************************");
        System.out.println(String.format("** STATS FOR %s **", label));
        System.out.println("Mean = " + descriptiveStatistics.getMean());
        System.out.println("Quadratic Mean = " + descriptiveStatistics.getQuadraticMean());
        System.out.println("Population Variance = " + descriptiveStatistics.getPopulationVariance());
        System.out.println("Std.Deviation = " + descriptiveStatistics.getStandardDeviation());
    }
}

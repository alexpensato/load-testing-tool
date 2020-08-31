package org.pensatocode.loadtest.core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.pensatocode.loadtest.core.handlers.PropertiesHandler;

@Slf4j
public final class StatsUtil {

    private StatsUtil(){
        // Util
    }

    public static void printStats(DescriptiveStatistics descriptiveStatistics, String label) {
        if (PropertiesHandler.getInstance().getInitialParamAsBoolean("print_stats")) {
            log.info("*****************************");
            log.info(String.format("** STATS FOR %s **", label));
            log.info("Mean = " + descriptiveStatistics.getMean());
            log.info("Quadratic Mean = " + descriptiveStatistics.getQuadraticMean());
            log.info("Population Variance = " + descriptiveStatistics.getPopulationVariance());
            log.info("Std.Deviation = " + descriptiveStatistics.getStandardDeviation());
        }
    }
}

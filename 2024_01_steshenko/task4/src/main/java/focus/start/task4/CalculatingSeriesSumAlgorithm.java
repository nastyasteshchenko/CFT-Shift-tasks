package focus.start.task4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class CalculatingSeriesSumAlgorithm implements SeriesAlgorithm {

    private static final Logger LOGGER = LoggerFactory.getLogger(CalculatingSeriesSumAlgorithm.class);

    private final long from;
    private final long to;

    CalculatingSeriesSumAlgorithm(long from, long to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public Double calculate() {
        LOGGER.debug("Thread with name '{}' and id = {} " +
                        "was started to calculate the algorithm for the series members from: {} to: {}",
                Thread.currentThread().getName(), Thread.currentThread().getId(), from, to);

        double resultSum = 0;
        for (long i = from; i <= to; i++) {
            resultSum += 1 / ((double) i * i);
        }

        LOGGER.debug("Task in thread with name '{}' and id = {} finished with result: {}",
                Thread.currentThread().getName(), Thread.currentThread().getId(), resultSum);
        return resultSum;
    }
}

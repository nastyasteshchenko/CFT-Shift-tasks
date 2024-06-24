package focus.start.task4;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class SeriesSumCalculator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SeriesSumCalculator.class);

    private static final int THREADS_AMOUNT = Runtime.getRuntime().availableProcessors();

    double calculateSum(long seriesLength, SeriesAlgorithmFactory seriesAlgorithmFactory) throws
            ExecutionException, InterruptedException {

        List<SeriesAlgorithm> tasks = createTasks(seriesLength, seriesAlgorithmFactory);
        LOGGER.info("All tasks was created.");

        ExecutorService executor = Executors.newFixedThreadPool(THREADS_AMOUNT);
        LOGGER.debug("Thread pool with {} threads was created.", THREADS_AMOUNT);
        List<Future<Double>> futureTasks = submitTasks(tasks, executor);
        LOGGER.info("All tasks was submitted.");

        double resultsSum = getResultsSum(futureTasks);
        LOGGER.debug("Sum of threads' results was calculated. Sum: {}", resultsSum);

        executor.shutdown();
        LOGGER.info("Thread pool was shut down.");
        return resultsSum;
    }

    private static List<SeriesAlgorithm> createTasks(long seriesLength,
                                                     SeriesAlgorithmFactory seriesAlgorithmFactory) {
        List<SeriesAlgorithm> tasks = new ArrayList<>(THREADS_AMOUNT);
        for (long i = 0; i < THREADS_AMOUNT; i++) {
            long from = seriesLength * i / THREADS_AMOUNT + 1;
            long to = seriesLength * (i + 1) / THREADS_AMOUNT;
            if (from <= to) {
                tasks.add(seriesAlgorithmFactory.createTask(from, to));
            }
        }
        return tasks;
    }

    private static List<Future<Double>> submitTasks(List<SeriesAlgorithm> tasks, ExecutorService executor) {
        List<Future<Double>> futures = new ArrayList<>(THREADS_AMOUNT);
        for (SeriesAlgorithm task : tasks) {
            Future<Double> future = executor.submit(task::calculate);
            futures.add(future);
        }
        return futures;
    }

    private static double getResultsSum(List<Future<Double>> futures) throws InterruptedException, ExecutionException {
        double resultsSum = 0;
        for (Future<Double> future : futures) {
            resultsSum += future.get();
        }
        return resultsSum;
    }
}

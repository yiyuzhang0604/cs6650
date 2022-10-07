import io.swagger.client.api.SkiersApi;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
public class Phase {
    private String name;
    private int threadNum;
    private SkiersApi skiersApi;
    private BlockingQueue queue;
    private CountDownLatch countDownLatch;
    private WriteToCSV csvWriter;
    private Queue<RequestResult> globalQueue;

    private ConcurrentCount successCount;
    private ConcurrentCount failCount;

    public Phase(String name, int threadNum, SkiersApi skiersApi, BlockingQueue queue, CountDownLatch countDownLatch, WriteToCSV csvWriter, Queue<RequestResult> globalQueue, ConcurrentCount successCount, ConcurrentCount failCount) {
        this.name = name;
        this.threadNum = threadNum;
        this.skiersApi = skiersApi;
        this.queue = queue;
        this.countDownLatch = countDownLatch;
        this.csvWriter = csvWriter;
        this.globalQueue = globalQueue;
        this.successCount = successCount;
        this.failCount = failCount;
    }

    public void startPhase() throws IOException {
        for (int i = 0; i < threadNum; i++) {
            Runnable runner = new PhaseRunner(name, threadNum, skiersApi, queue, countDownLatch, csvWriter, globalQueue, successCount, failCount);
            new Thread(runner).start();
        }
    }

    public void waitForPhaseToFinish() throws InterruptedException {
        countDownLatch.await();
    }


}

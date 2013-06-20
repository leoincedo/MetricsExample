package sample;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created with IntelliJ IDEA.
 * User: liongo
 * Date: 13. 6. 19
 * Time: 오후 4:37
 * To change this template use File | Settings | File Templates.
 */
public class QueueManager {

    private Queue queue;

    public QueueManager( MetricRegistry metrics, String name ) {
        this.queue = new ArrayDeque();

        metrics.register(MetricRegistry.name(QueueManager.class, name, "size"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        return queue.size();
                    }
                });

    }
}

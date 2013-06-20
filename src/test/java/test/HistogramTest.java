package test;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import org.junit.Test;
import sample.RequestHandler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * Created with IntelliJ IDEA.
 * User: liongo
 * Date: 13. 6. 19
 * Time: 오후 4:40
 * To change this template use File | Settings | File Templates.
 */
public class HistogramTest {
    final MetricRegistry metrics = new MetricRegistry();
    int max = 300;
    Random random = new Random();

    final Histogram responseSizes = metrics.histogram(name(RequestHandler.class, "response-sizes") );
    final Timer responses = metrics.timer(name(RequestHandler.class, "responses"));

    @Test
    public void histogram()
    {


        for( int i = 0; i < max; ++i ) {

            responseSizes.update( random.nextInt() );

        }

    }

    @Test
    public void timer() throws Exception
    {
        Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                while( true ) {
                    final Timer.Context context = responses.time();
                    try {
                        Thread.sleep( random.nextInt( 10 ) );
                    } catch( InterruptedException iex ) {
                        System.out.println("interrupt!!");
                        break;
                    }
                    context.stop();
                }
            }
        });

        thread.start();

        System.out.println("waiting for testing.. ");

        final ConsoleReporter reporter = ConsoleReporter.forRegistry( metrics )
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.SECONDS);

        Thread.sleep( 5 * 60 * 1000 );

        thread.interrupt();
    }

}

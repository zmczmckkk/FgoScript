package test;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

public class ScheduledThreadPoolExecutorDemo {
static class TimerTask implements Runnable{
    private String id;
    public TimerTask(String id){
        this.id = id;
    }
    @Override
    public void run(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(id);
    }
}

    public static void main(String[] args) throws InterruptedException{
        ThreadFactory fcc  = new BasicThreadFactory.Builder().namingPattern("FgoExcute-pool- %d").daemon(true).build();
        ScheduledExecutorService ses =  new ScheduledThreadPoolExecutor(3 , fcc);
        TimerTask t = new TimerTask("a");
        Runtime.getRuntime().addShutdownHook(new Thread(new Thread(t)));
        ScheduledFuture sfa = ses.scheduleAtFixedRate(t, 200,
                1000, TimeUnit.MILLISECONDS);
        ScheduledFuture sfb = ses.scheduleAtFixedRate(new TimerTask("b"), 400,
                1000, TimeUnit.MILLISECONDS);
        ScheduledFuture sfc = ses.scheduleAtFixedRate(new TimerTask("c"), 600,
                1000, TimeUnit.MILLISECONDS);
        ScheduledFuture sfd = ses.scheduleAtFixedRate(new TimerTask("d"), 800,
                1000, TimeUnit.MILLISECONDS);
        ScheduledFuture aaa = ses.scheduleAtFixedRate(new Runnable() {
                                                          @Override
                                                          public void run() {
                                                              System.out.println(111);
                                                          }
                                                      }, 800,
                1000, TimeUnit.MILLISECONDS);
      /*  Thread.sleep(5000);
        sfa.cancel(true);
        Thread.sleep(5000);
        ses.shutdown();*/
    }
}

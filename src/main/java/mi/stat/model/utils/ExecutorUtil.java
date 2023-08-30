package mi.stat.model.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorUtil {
   final static ExecutorService executor = Executors.newFixedThreadPool(10);

    public static ExecutorService getExecutorService(){
        return executor;
    }
}

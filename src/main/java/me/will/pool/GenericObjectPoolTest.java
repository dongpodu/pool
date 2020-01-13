package me.will.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultEvictionPolicy;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GenericObjectPoolTest {

    private static Executor executor = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        BasePooledObjectFactory factory = new MyPooledObjectFactory();
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        config.setMaxTotal(2);
        config.setMaxIdle(10);
        config.setMinIdle(1);
        config.setBlockWhenExhausted(false); //池满时，再次获取对象是否阻塞，默认true。如果为false，阻塞
        config.setEvictionPolicy(new DefaultEvictionPolicy<Connection>());//驱逐策略，早期版本使用该方法
        config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");// 驱逐策略类名，效果同setEvictionPolicy，后期版本使用该方法
        config.setEvictorShutdownTimeoutMillis(1000);//驱逐线程的超时时间
        config.setFairness(true);
        config.setJmxEnabled(true);

        GenericObjectPool<Connection> pool = new GenericObjectPool<Connection>(factory, config);

        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                try {
                    System.out.println("borrow start");
                    pool.borrowObject();
                    System.out.println("borrow end");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    public static class MyPooledObjectFactory extends BasePooledObjectFactory {

        @Override
        public Object create() throws Exception {
            System.out.println("create");
            return new Connection();
        }

        @Override
        public PooledObject wrap(Object obj) {
            System.out.println("wrap");
            return new DefaultPooledObject(obj);
        }
    }

}

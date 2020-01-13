package me.will.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.BaseGenericObjectPool;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class BaseObjectPoolTest {

    public static void main(String[] args) {
        BasePooledObjectFactory factory = new MyPooledObjectFatctory();
        BaseGenericObjectPool<Connection> pool = new GenericObjectPool<Connection>(factory);
    }


    public static class MyPooledObjectFatctory extends BasePooledObjectFactory {

        @Override
        public Object create() throws Exception {
            return new Connection();
        }

        @Override
        public PooledObject wrap(Object obj) {
            return new DefaultPooledObject(obj);
        }
    }

}

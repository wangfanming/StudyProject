package com.wfm.kryoserializer.serializer;

/**
 * @Auther: wangfanming
 * @Date: 2019/7/23 11:42
 * @Description:
 */
import com.esotericsoftware.kryo.Kryo;

public class KryoHolder
{
    private static ThreadLocal<Kryo> threadLocalKryo = new ThreadLocal<Kryo>()
    {
        protected Kryo initialValue()
        {
            Kryo kryo = new KryoReflectionFactory();

            return kryo;
        };
    };

    public static Kryo get()
    {
        return threadLocalKryo.get();
    }
}

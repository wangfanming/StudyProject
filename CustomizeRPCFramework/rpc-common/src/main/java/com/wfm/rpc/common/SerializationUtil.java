package com.wfm.rpc.common;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.Schema;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.objenesis.ObjenesisStd;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SerializationUtil {

    private static Map<Class<?>, Schema<?>> cachedSchema = new ConcurrentHashMap<Class<?>, Schema<?>>();

    private static ObjenesisStd objenesisStd = new ObjenesisStd(true);

    public SerializationUtil() {
    }

    /**
     * 获取类的 schema
     *
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> Schema<T> getSchema(Class<?> cls) {
        Schema<T> schema = (Schema<T>) cachedSchema.get(cls);

        if (schema == null) {
            schema = (Schema<T>) RuntimeSchema.createFrom(cls);
            if (schema != null) {
                cachedSchema.put(cls, schema);
            }
        }

        return schema;
    }

    /**
     * 序列化（对象 -> 字节数组）
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>) obj.getClass();
        LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
        try {
            Schema<Object> schema = getSchema(cls);
            return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        } finally {
            buffer.clear();
        }
    }

    /**
     * 反序列化（字节数组 -> 对象）
     *
     * @param data
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> cls) {
        try {
            /*
                如果一个类没有参数为空的构造方法时，直接调用newInstance方法试图得到一个实例对象的时候是会抛出异常的
                通过ObjenesisStd可以避开这个问题
             */
            T message = objenesisStd.newInstance(cls); // 实例化
            Schema<T> schema = getSchema(cls);

            ProtostuffIOUtil.mergeFrom(data, message, schema);
            return message;
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }


}

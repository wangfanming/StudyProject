package com.wfm.Generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName DataSource
 * @Descripyion TODO  重点在泛型边界的指定 T extends Producer， 不指定边界，则 T 就是一个Object
 * @date 2020/5/23 21:51
 */
public class DataSource<T extends Producer> {
    private static final Logger LOG = LoggerFactory.getLogger(DataSource.class);

    private T producer;

    public DataSource(T producer) {
        LOG.info("test");
        this.producer = producer;
    }

    public static void main(String[] args) {
        new DataSource(new CustomKafkaProducer()).saveData();
    }

    public void saveData(){
        producer.sayHi();
        // xxxxx
    }

}

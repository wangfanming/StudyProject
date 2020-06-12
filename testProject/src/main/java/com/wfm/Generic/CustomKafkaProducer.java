package com.wfm.Generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName CustomKafkaProducer
 * @Descripyion TODO
 * @date 2020/5/23 21:50
 */
public class CustomKafkaProducer implements Producer {
    private static final Logger LOG = LoggerFactory.getLogger(CustomKafkaProducer.class);

    @Override
    public void sayHi() {
        LOG.info("Hi,This's CustomKafkaProducer");
    }
}

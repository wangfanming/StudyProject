package com.wfm.Generic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName ESClient
 * @Descripyion TODO
 * @date 2020/5/23 21:49
 */
public class ESClient implements Producer {
    private static Logger LOG = LoggerFactory.getLogger(ESClient.class);

    @Override
    public void sayHi() {
        LOG.info("Hi,This's ESClient.");
    }
}

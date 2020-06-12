package com.wfm.rpc.test;

/**
 * @ClassName: JUnitTestCustomException
 * @Auther: wangfanming
 * @Date: 2019/8/31
 * @Description: TODO
 * @Version 1.0
 */
public class JUnitTestCustomException extends RuntimeException {
    private static final long serialVersionUID = 591530421634999576L;

    public JUnitTestCustomException() {
        super("CustomException");
    }
}

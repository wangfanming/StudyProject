package com.wfm.rpc.test;

import java.util.List;

/**
 * @ClassName: JUnitTestInterface
 * @Auther: wangfanming
 * @Date: 2019/8/31
 * @Description: TODO
 * @Version 1.0
 */
public interface JUnitTestInterface
{
    public String methodWithoutArg();
    public String methodWithArgs(String arg1, int arg2);
    public JUnitTestCustomObject methodWithCustomObject(JUnitTestCustomObject customObject);
    public List<String> methodReturnList(String arg1, String arg2);
    public void methodThrowException();
    public void methodTimeOut();
    public void methodReturnVoid();
    public String methodDelayOneSecond();
    public int methodForMultiThread(int threadId);
    public String methodForPerformance();
}

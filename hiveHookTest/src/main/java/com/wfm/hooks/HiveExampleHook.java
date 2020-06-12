package com.wfm.hooks;

import org.apache.hadoop.hive.ql.hooks.ExecuteWithHookContext;
import org.apache.hadoop.hive.ql.hooks.HookContext;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName HiveExampleHook
 * @Descripyion TODO
 * @date 2020/4/5 23:41
 */
public class HiveExampleHook implements ExecuteWithHookContext {
    public void run(HookContext hookContext) throws Exception {
        System.out.println("==============================Hello from the hook !!=================================");

    }
}

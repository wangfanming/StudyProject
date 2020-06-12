package com.wfm.hooks;

import org.apache.hadoop.hive.ql.exec.Task;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.HiveSemanticAnalyzerHook;
import org.apache.hadoop.hive.ql.parse.HiveSemanticAnalyzerHookContext;
import org.apache.hadoop.hive.ql.parse.SemanticException;

import java.io.Serializable;
import java.util.List;

/**
 * @author wangfanming
 * @version 1.0
 * @ClassName HiveSemanticAnalyzerHookTest
 * @Descripyion TODO
 * @date 2020/4/12 16:29
 */
public class HiveSemanticAnalyzerHookTest implements HiveSemanticAnalyzerHook {

    /**
     * 在语义分析前执行
     *
     * @param context
     * @param ast
     * @return
     * @throws SemanticException
     */
    public ASTNode preAnalyze(HiveSemanticAnalyzerHookContext context, ASTNode ast) throws SemanticException {
        String command = context.getCommand();
        String commHash = String.valueOf(command.hashCode());
        context.getConf().set("testId", commHash);
        System.out.println("======================= Command : " + command + "=======================================");
        System.out.println("======================= testId :" + commHash + "========================================");
        return ast;
    }

    public void postAnalyze(HiveSemanticAnalyzerHookContext context, List<Task<? extends Serializable>> rootTasks) throws SemanticException {
        System.out.println("======================= testId :" + context.getConf().get("testId") + "========================================");

    }
}

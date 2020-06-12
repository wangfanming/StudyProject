package com.wfm;

/**
 * @version 1.0
 * @ClassName ShortToUnicodeString
 * @Descripyion TODO
 * @date 2020/5/30 23:44
 */
public class ShortToUnicodeString extends  ArrayInitBaseListener{

    /**
     * 将 { 翻译为 “
     * @param ctx
     */
    @Override
    public void enterInit(ArrayInitParser.InitContext ctx) {
        System.out.println('"');
    }

    /**
     * 将 } 翻译为 “
     * @param ctx
     */
    @Override
    public void exitInit(ArrayInitParser.InitContext ctx) {
        System.out.println('"');
    }


    /**
     * 将两个整数翻译为十六进制形式，然后加前缀 "u"
     * @param ctx
     */
    @Override
    public void enterValue(ArrayInitParser.ValueContext ctx) {
        //假定不存在嵌套结构
        Integer value = Integer.valueOf(ctx.INT().getText());
        System.out.printf("\\u%04x",value);
    }
}

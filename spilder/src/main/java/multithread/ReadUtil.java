package multithread;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/5/22 21:38
 * @Description:
 */
public class ReadUtil {
    private int theadNum = 1 /* 线程数 */, timeReadLine = 9 /* 子线程一次循环读取字节长度 */, timeForTime = 5; /* 子线程一次循环读取数 */
    private StringBuffer sbf = new StringBuffer();

    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        ReadUtil rUtil = new ReadUtil();
        rUtil.read(new File("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\手机号前7位.txt"), 0);

        System.out.println("获取字符总长度:" + rUtil.getSbf().length() + ". 总耗时：" +(System.currentTimeMillis() - start) + "毫秒！");
    }

    /**
     * 假定：特殊场景不知道文件总长度！ 情景下的   递归+多线程   模式。 可通过File得到总长度，此时可更好的进行子线程数的分配和控制。
     * @param file 文件
     * @param start 其实位置
     */
    public void read(File file, int start){
        List<ReadItem> list = new ArrayList<ReadItem>();

        for(int i = 0; i < theadNum; i++){
            list.add(new ReadItem(file, start + i*timeReadLine*timeForTime));	//创建多个子线程
            list.get(list.size()-1).start();
        }

        for(int i = 0; i < list.size(); i++){
            try {
                list.get(i).join();
                sbf.append(list.get(i).getSb());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(list.get(list.size() - 1).getLastNum() == timeReadLine){
            read(file, start + theadNum * timeReadLine * timeForTime);
        }
    }

    public StringBuffer getSbf() {
        return sbf;
    }

    /**
     * 子线程
     * @author James
     */
    public class ReadItem extends Thread{
        private BufferedReader reader;
        private int start, lastNum;
        private StringBuffer sb;

        public ReadItem(File file, int start) {
            try {
                this.reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.start = start;
            this.sb = new StringBuffer();
        }

        @Override
        public void run() {
            char[] buf = new char[timeReadLine];
            try {
                reader.skip(this.start);
                for (int i = 0; i < timeForTime && (lastNum = reader.read(buf)) != -1; i++ ) {
                    String prefix = new String(buf, 0, lastNum);
                    generateMobile(prefix.replace("\r\n",""),"E:\\data\\mobiles\\");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        public int getLastNum() {
            return lastNum;
        }

        public StringBuffer getSb() {
            return sb;
        }
    }

    /**
     *
     * 功能描述: 将生成的手机号码存入文件
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/5/22 21:41
     */
    public static void generateMobile(String prefix,String outfile) throws IOException {
        File file = new File(outfile + prefix + ".txt");
        if (!file.exists()){
            file.createNewFile();
        }
        String mobile = "";
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        for (int k=0;k<10000; k++){
            if(k < 10) {
                mobile = prefix + "000" + k;
            } else if(k < 100){
                mobile = prefix + "00" + k;
            }else if(k < 1000){
                mobile = prefix + "0" + k;
            }else {
                mobile = prefix + k;
            }
            bufferedWriter.write(mobile+"\r\n");
            mobile = "";
        }
        bufferedWriter.flush();
        bufferedWriter.close();
    }
}

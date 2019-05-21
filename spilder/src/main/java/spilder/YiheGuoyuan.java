package spilder;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: Administrator
 * @Date: 2019/5/20 20:21
 * @Description:
 */
public class YiheGuoyuan {
    public static void main(String[] args) throws IOException, InterruptedException {
        //创建固定大小的线程池
        ExecutorService exec = Executors.newFixedThreadPool(16);
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\01.txt",new String[]{"139","138"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\02.txt",new String[]{"137","136"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\03.txt",new String[]{"135","134"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\04.txt",new String[]{"159","158"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\05.txt",new String[]{"157","150"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\06.txt",new String[]{"151","152"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\07.txt",new String[]{"147","188"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\08.txt",new String[]{"187","182"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\09.txt",new String[]{"183","184"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\10.txt",new String[]{"178","130"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\11.txt",new String[]{"131","132"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\12.txt",new String[]{"156","155"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\13.txt",new String[]{"186","185"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\14.txt",new String[]{"145","176"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\15.txt",new String[]{"166","133"}));
        exec.execute(new ParseThread("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\16.txt",new String[]{"153","189"}));

        //线程关闭
        exec.shutdown();
    }

}


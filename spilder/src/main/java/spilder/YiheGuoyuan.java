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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: Administrator
 * @Date: 2019/5/20 20:21
 * @Description:
 */
public class YiheGuoyuan {
    public static void main(String[] args) throws IOException, InterruptedException {
        File outFile = new File("E:\\workspaces\\studyProject\\spilder\\src\\main\\resources\\orders\\result.txt");
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String mobile = "";
        String[] yiDong = {"166","138","137","136","135","134","159","158","157","150","151","152","147","188","187","182","183","184","178"};
        String[] lianTong = {"130","131","132","156","155","186","185","145","176","166"};
        String[] dianXin = {"133","153","189","180","181","177","173"};

        String[][] mobiles = {yiDong,lianTong,dianXin};

        int i,j = 0;

        for(i = 0; i < mobiles.length; i++){
            for (j = 0;j<mobiles[i].length;j++){
                for(int k = 38648868; k > 0 ;k --){
                    if(k < 10){
                        mobile = mobiles[i][j] + "0000000" +k;
                    }else if(k < 100){
                        mobile = mobiles[i][j] + "000000" +k;
                    }else if(k < 1000){
                        mobile = mobiles[i][j] + "00000" +k;
                    }else if(k < 10000){
                        mobile = mobiles[i][j] + "0000" +k;
                    }else if(k < 100000){
                        mobile = mobiles[i][j] + "000" +k;
                    }else if(k < 1000000){
                        mobile = mobiles[i][j] + "00" +k;
                    }else if(k < 10000000){
                        mobile = mobiles[i][j] + "0" +k;
                    }else {
                        mobile = mobiles[i][j] + k;
                    }
                    //发起 httpGet请求，获取数据
                    System.out.println(mobile);
                    String responseStr = doGet(httpClient, mobile);
                    if(responseStr != null){
                        FileUtils.writeStringToFile(outFile,responseStr,"UTF-8",true);
                    }
                    Thread.sleep(200);
                    mobile = "";
                }
            }
        }
        httpClient.close();
    }

    /**
     *
     * 功能描述: 发出httpGet请求，获取数据
     *
     * @param:
     * @return:
     * @auther: Administrator
     * @date: 2019/5/20 21:05
     */
    private static String doGet(CloseableHttpClient httpClient,String mobile)  throws IOException  {
        HttpGet httpGet = new HttpGet("https://admin.yhguoyuan.cn/order/listCu?keyword="+ mobile + "&peopleId=10277115&lastId=&tmst=1558355229867");
        httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.157 Safari/537.36");
        httpGet.setHeader("Referer","https://wx.yhguoyuan.cn/query?peopleId=10277115");
        httpGet.setHeader("Accept","application/json, text/plain, */*");
        httpGet.setHeader("Origin","https://wx.yhguoyuan.cn");

        CloseableHttpResponse response=null;
        response=httpClient.execute(httpGet);
        HttpEntity entity=response.getEntity();//获取返回实体
//        System.out.println("网页内容："+ EntityUtils.toString(entity,"utf-8"));
        String responseStr = EntityUtils.toString(entity,"utf-8") + "\n";

        System.out.println(responseStr);

        response.close();
        if(responseStr.contains("[{")){
            return responseStr;
        }else {
            return null;
        }
    }

}


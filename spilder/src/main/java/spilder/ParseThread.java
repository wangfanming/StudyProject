package spilder;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * @Auther: Administrator
 * @Date: 2019/5/21 20:09
 * @Description:
 */
public class ParseThread extends Thread {
    private  String outputPath;
    private String[] mobiles;
    CloseableHttpClient httpClient = HttpClients.createDefault();

    public ParseThread(String outputPath, String[] mobiles){
        this.outputPath = outputPath;
        this.mobiles = mobiles;
    }

    @Override
    public void run() {
        int i,j = 0;
        String mobile = "";
        for (j = 0; j<mobiles.length ; j++){
            for(int k = 2; k > 0 ;k--){
                if(k < 10) {
                    mobile = mobiles[j] + "0000000" + k;
                }
//                }else if(k < 100){
//                    mobile = mobiles[j] + "000000" +k;
//                }else if(k < 1000){
//                    mobile = mobiles[j] + "00000" +k;
//                }else if(k < 10000){
//                    mobile = mobiles[j] + "0000" +k;
//                }else if(k < 100000){
//                    mobile = mobiles[j] + "000" +k;
//                }else if(k < 1000000){
//                    mobile = mobiles[j] + "00" +k;
//                }else if(k < 10000000){
//                    mobile = mobiles[j] + "0" +k;
//                }
                else {
                    mobile = mobiles[j] + k;
                }

                //发起 httpGet请求，获取数据
                System.out.println(Thread.currentThread().getName() + ":" + mobile);
//                String responseStr = null;
//                try {
//                    responseStr = doGet(httpClient, mobile);
//                    FileUtils.writeStringToFile(new File(this.outputPath),responseStr,"UTF-8",true);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
            mobile = "";
        }
        try {
            httpClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private static String doGet(CloseableHttpClient httpClient, String mobile)  throws IOException {
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

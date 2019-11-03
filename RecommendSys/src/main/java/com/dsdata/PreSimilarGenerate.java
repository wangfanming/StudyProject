package com.dsdata;




import java.io.*;

/**
 * @Auther: wangfanming
 * @Date: 2019/10/29 16:09
 * @Description:
 */

public class PreSimilarGenerate {
    public static void main(String[] args) throws IOException {
        File file = new File("D:\\share\\临时\\nf_prize_dataset.tar\\download\\training_set");
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for(File f:files){
                BufferedReader bufferReader = new BufferedReader(new FileReader(f));
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File("D:\\share\\临时\\nf_prize_dataset.tar\\download\\preTrain\\" + f.getName())));
                String s = bufferReader.readLine();
                String movieId = "";
                while ( s != null){
                    if(s.contains(":")){
                        movieId = s.split(":")[0];
                        s = bufferReader.readLine();
                        continue;
                    }
                    bufferedWriter.write(movieId + "," + s + "\n");
                    s = bufferReader.readLine();
                }
                bufferedWriter.close();
                bufferReader.close();
            }
        }


    }
}

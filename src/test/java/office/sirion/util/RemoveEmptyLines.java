package office.sirion.util;

import org.apache.commons.io.FileUtils;
import java.io.*;
import java.util.ArrayList;

public class RemoveEmptyLines {
    public static String content=null;
    public static File f1=new File("C:\\Users\\naveen.gupta\\Desktop\\nohup\\nohup1.txt");

    public static ArrayList list = new ArrayList();

    public static void main(String[] args) {


        try {
            File f=new File("C:\\Users\\naveen.gupta\\Desktop\\nohup\\nohup.txt");

            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));


            while((content=reader.readLine())!=null)
            {
                content = content.replaceAll("e.printStackTrace();", "");

                list.add(content);

            }
            FileUtils.writeStringToFile(f1, list.toString(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
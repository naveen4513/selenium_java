package office.sirion.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CompareTextFiles
{
    public static void main(String[] args) throws IOException
    {
        BufferedReader reader1 = new BufferedReader(new FileReader("D:\\XMLBulkData\\result19062019.txt"));
        BufferedReader reader2 = new BufferedReader(new FileReader("D:\\XMLBulkData\\MARecords.txt"));
        String line1 = reader1.readLine();
        String line2 = reader2.readLine();

        int lineNum = 1;
        HashMap<String, ArrayList> Map = new HashMap<String, ArrayList>();
        ArrayList list = new ArrayList();
        HashMap<String, ArrayList> Map1 = new HashMap<String, ArrayList>();
        ArrayList list1 = new ArrayList();
        Set set =null;
        Set set1 = null;
        while (line1 != null){
            if(line1 == null){
                break;
            }
            else {
                list.add(line1.indexOf(line1));
                Map.put(line1,list);
                set = Map.keySet();
            }
            line1 = reader1.readLine();
            lineNum++;
        }
        reader1.close();

        while (line2 != null){
            if(line2 == null){
                break;
            }
            else {
                list1.add(line2.indexOf(line2));
                Map1.put(line2,list1);
                set1=Map1.keySet();
            }
            line2 = reader2.readLine();
            lineNum++;
        }
        reader2.close();

        set.retainAll(set1);

        if(set.isEmpty()){
            System.out.println("Both Hashset does not contain duplicate records");
        }else{
            System.out.println("HashSet content:");
            System.out.println(set);
        }
    }

}
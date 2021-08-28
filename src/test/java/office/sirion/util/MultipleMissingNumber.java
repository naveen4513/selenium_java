package office.sirion.util;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MultipleMissingNumber {


    public static void main(String[] args) throws IOException {
        // given input
        //System.out.println(TimeUnit.HOURS.toMillis(5));
        List listdata = new ArrayList();

        try(BufferedReader in = new BufferedReader(new FileReader("D:\\XMLBulkData\\result18062019.txt"))) {
            String str;

            while ((str = in.readLine()) != null) {

                listdata.add(Integer.parseInt(str));

            }
            missingNumber(listdata);
        }
        catch (IOException e) {
            System.out.println("File Read Error");
        }



        List<Integer> list = new ArrayList<Integer>();
        for (int index = 0; index < listdata.size(); index++)
        {
            Object[] data = listdata.toArray();
            list.add((Integer) list.toArray(data)[index]);
        }

       System.out.println(findDuplicates(list));

    }

    public static List<Integer> missingNumber(List<Integer> input) {
        ArrayList newList = new ArrayList<Integer>();
        int[] register = new int[10000];

        for (int i : input) {
            register[i-40000] = 1;
        }
        System.out.println("missing numbers in given array");

        for (int i = 1; i < register.length; i++) {
            if (register[i] == 0) {
                newList.add(i);
            }

        }
        return newList ;
    }

    public static Set<Integer> findDuplicates(List<Integer> listContainingDuplicates)
    {
        final Set<Integer> settoreturn = new HashSet<>();
        final Set<Integer> set1 = new HashSet<>();

        for (Integer list : listContainingDuplicates)
        {
            if (!set1.add(list))
            {
                settoreturn.add(list);
            }
        }
        return settoreturn;
    }
}
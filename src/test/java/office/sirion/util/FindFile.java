package office.sirion.util;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

public class FindFile {

    public static InputStream is = null;
    public static OutputStream os = null;
    public static String absolutePath = null;

    public static void main(String[] args) throws IOException {

        File path = new File("D:\\XMLBulkData\\2001");
        String[] name = new String []{"2001-MA-2005804.xml", "2001-MA-2005510.xml", "2001-MA-2005434.xml", "2001-MA-2005395.xml",
                "2001-MA-2005393.xml", "2001-MA-2005347.xml", "2001-MA-2005345.xml", "2001-MA-2005330.xml", "2001-MA-2004886.xml",
                "2001-MA-2005042.xml", "2001-MA-2004736.xml", "2001-MA-2004622.xml", "2001-MA-2004621.xml", "2001-MA-2004611.xml",
                "2001-MA-2004610.xml", "2001-MA-2004442.xml", "2001-MA-2004493.xml", "2001-MA-2004494.xml", "2001-MA-2004490.xml",
                "2001-MA-2004028.xml", "2001-MA-2003938.xml", "2001-MA-2003940.xml", "2001-MA-2003608.xml", "2001-MA-2003607.xml",
                "2001-MA-2003606.xml", "2001-MA-2003592.xml", "2001-MA-2003442.xml", "2001-MA-2003400.xml", "2001-MA-2003387.xml",
                "2001-MA-2003197.xml", "2001-MA-2003181.xml", "2001-MA-2003180.xml", "2001-MA-2003184.xml", "2001-MA-2003182.xml",
                "2001-MA-2003186.xml", "2001-MA-2003178.xml", "2001-MA-2003185.xml", "2001-MA-2003179.xml", "2001-MA-2003183.xml"};


        findFile(path,name);
        for(String t:name){
            copyFileUsingStream(new File(absolutePath), new File("D:\\FindData\\"+t));
        }

    }

    private static String findFile(File filePath, String[] filename){
        try {
            boolean recursive = true;

            Collection files = FileUtils.listFiles(filePath, null, recursive);

            for (Iterator iterator = files.iterator(); iterator.hasNext();) {
                File file = (File) iterator.next();
                for(String Temp: filename) {
                    if (file.getName().equals(Temp)) {
                        absolutePath = file.getAbsolutePath();
                        System.out.println(file.getAbsolutePath());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return absolutePath;
    }

    private static void copyFileUsingStream(File source, File dest) throws IOException {

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }
}
package office.sirion.util;


import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import java.io.*;



public class TextFileModificationProgram
{
    static void modifyFile(String filePath, String oldString, String newString) throws IOException {
        LineIterator it = FileUtils.lineIterator(new File(filePath), "UTF-8");
        String oldContent = "";
        try {
            while (it.hasNext()) {
            String line = it.nextLine();
                oldContent = oldContent + line + System.lineSeparator();
            }
        } finally {
            LineIterator.closeQuietly(it);
        }

            //Replacing oldString with newString in the oldContent

            String newContent = oldContent.replaceAll(oldString, newString);

            //Rewriting the input text file with newContent

        FileUtils.writeStringToFile(new File(filePath),newContent);


        }



    public static void main(String[] args) throws IOException {
        modifyFile("C:\\Users\\naveen.gupta\\Desktop\\nohup\\nohup.out", "Populate Simple Api Object Time :0", "");

        System.out.println("done");
    }
}
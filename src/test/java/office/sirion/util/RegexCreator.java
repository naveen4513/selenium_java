package office.sirion.util;

import java.util.regex.*;

class Main
{
  public static void main(String[] args)
  {
    String txt="http://qa.rc.office/download/communication abcdefghij";

    String re1="(http)";	// Word 1
    String re2="(:)";	// Any Single Character 1
    String re3="(\\/)";	// Any Single Character 2
    String re4="(\\/)";	// Any Single Character 3
    String re5="(qa)";	// Word 2
    String re6="(\\.)";	// Any Single Character 4
    String re7="(rc)";	// Word 3
    String re8="(\\.)";	// Any Single Character 5
    String re9="(office)";	// Word 4
    String re10="(\\/)";	// Any Single Character 6
    String re11="(download)";	// Word 5
    String re12="(\\/)";	// Any Single Character 7
    String re13="(communication)";	// Word 6
    String re14="( )";	// White Space 1
    String re15="(abcdefghij)";	// Word 7

    Pattern p = Pattern.compile(re1+re2+re3+re4+re5+re6+re7+re8+re9+re10+re11+re12+re13+re14+re15,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    Matcher m = p.matcher(txt);
    if (m.find())
    {
        String word1=m.group(1);
        String c1=m.group(2);
        String c2=m.group(3);
        String c3=m.group(4);
        String word2=m.group(5);
        String c4=m.group(6);
        String word3=m.group(7);
        String c5=m.group(8);
        String word4=m.group(9);
        String c6=m.group(10);
        String word5=m.group(11);
        String c7=m.group(12);
        String word6=m.group(13);
        String ws1=m.group(14);
        String word7=m.group(15);
        System.out.print("("+word1.toString()+")"+"("+c1.toString()+")"+"("+c2.toString()+")"+"("+c3.toString()+")"+"("+word2.toString()+")"+"("+c4.toString()+")"+"("+word3.toString()+")"+"("+c5.toString()+")"+"("+word4.toString()+")"+"("+c6.toString()+")"+"("+word5.toString()+")"+"("+c7.toString()+")"+"("+word6.toString()+")"+"("+ws1.toString()+")"+"("+word7.toString()+")"+"\n");
    }
  }
}

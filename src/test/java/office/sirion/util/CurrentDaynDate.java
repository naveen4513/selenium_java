package office.sirion.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;

import office.sirion.base.TestBase;

import org.testng.Assert;
import org.testng.annotations.Test;


@Test
public class CurrentDaynDate extends TestBase {

    public static String[] testDateString;
    public static String currentmonth;
    public static String dayofweek;
    public static int currentdate;

    public void dayndate(String Date, String Week, String Month, String Year) {

        Date todaysDate = new Date();
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        testDateString = df.format(todaysDate).split("/");
        currentdate = Integer.parseInt(testDateString[0].replaceFirst("0", ""));
        int month = Integer.parseInt(testDateString[1].replaceFirst("0", "").toLowerCase());

        switch(month)
        {
            case 1:
                currentmonth = "january";
                System.out.println(currentmonth);
                break;
            case 2:
                currentmonth = "february";
                System.out.println(currentmonth);
                break;
            case 3:
                currentmonth = "march";
                System.out.println(currentmonth);
                break;
            case 4:
                currentmonth = "april";
                System.out.println(currentmonth);
                break;
            case 5:
                currentmonth = "may";
                System.out.println(currentmonth);
                break;
            case 6:
                currentmonth = "june";
                System.out.println(currentmonth);
                break;
            case 7:
                currentmonth = "july";
                System.out.println(currentmonth);
                break;
            case 8:
                currentmonth = "august";
                System.out.println(currentmonth);
                break;
            case 9:
                currentmonth = "september";
                System.out.println(currentmonth);
                break;
            case 10:
                currentmonth = "october";
                System.out.println(currentmonth);
                break;
            case 11:
                currentmonth = "november";
                System.out.println(currentmonth);
                break;
            case 12:
                currentmonth = "december";
                System.out.println(currentmonth);
                break;
            default:
                System.out.println("invalid Month number\nPlease try again ....\n");
                break;
        }
        LocalDate date = LocalDate.now();
        DayOfWeek dow = date.getDayOfWeek();
        dayofweek = dow.toString().toLowerCase();
        System.out.println(dayofweek);

       // int date1 = Integer.parseInt(Date);
//        compareIntegerVals(currentdate, date1);

        Assert.assertEquals(dayofweek, Week);

        Assert.assertEquals(currentmonth, Month);

        //int actyear = Integer.parseInt(testDateString[2]);
        //int expyear = Integer.parseInt(Year);
//        compareIntegerVals(actyear, expyear);
    }
}
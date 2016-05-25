package biz.home.application.myUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by admin on 2016/2/3.
 */
public class TimeChangeUtil {
    public static String time2date(String time){
        String s="";
        long t=Long.valueOf(time);
        System.out.println("毫秒值:" +t );
        Date date=new Date(t);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年MM月dd日");
        s=sdf.format(date);
        return  s;
    }

}

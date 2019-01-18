package com.open.application.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  public static int getDayBeforeSomeDay(int day){
    Calendar calendar=Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.add(Calendar.DATE,day);
    return calendar.get(Calendar.DATE);
  }
  public static String getOneDayAfterNowString(){
     SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY-MM-DD");
    Calendar calendar=Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.add(Calendar.DATE,1);
    return simpleDateFormat.format(calendar.getTime());
  }

  public static String getNowDayString(){
    SimpleDateFormat simpleDateFormat=new SimpleDateFormat("YYYY-MM-DD");
    return simpleDateFormat.format(new Date());
  }
}

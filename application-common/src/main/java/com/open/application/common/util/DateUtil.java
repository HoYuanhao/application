package com.open.application.common.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

  public static int getDayBeforeSomeDay(int day){

    Calendar calendar=Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    calendar.add(Calendar.DATE,day);
    return calendar.get(Calendar.DATE);
  }

}

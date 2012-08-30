package com.nrl.utility;

import java.util.Calendar;

public class DateUtility {
    static public String convertUNIXtoDate(long t,boolean changeLine){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(t);
    	StringBuilder str = new StringBuilder("");
    	str.append(calendar.get(Calendar.YEAR));
    	str.append("-");
    	str.append((calendar.get(Calendar.MONTH)+1));
    	str.append("-");
    	str.append(calendar.get(Calendar.DAY_OF_MONTH));
    	if(changeLine)
    		str.append("\n");
    	else
    		str.append(" ");
    	str.append(calendar.get(Calendar.HOUR_OF_DAY));
    	str.append(":");
    	str.append(calendar.get(Calendar.MINUTE));
    	str.append(":");
    	str.append(calendar.get(Calendar.SECOND));
    	return str.toString();
    }
    static public String convertUNIXtoDatePart(long t){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTimeInMillis(t);
    	StringBuilder str = new StringBuilder("");
    	str.append(calendar.get(Calendar.YEAR));
    	str.append("-");
    	str.append((calendar.get(Calendar.MONTH)+1));
    	str.append("-");
    	str.append(calendar.get(Calendar.DAY_OF_MONTH));
    	return str.toString();
    } 
}

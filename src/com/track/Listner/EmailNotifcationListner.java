package com.track.Listner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.track.job.ExpiryEmailSendJob;
import com.track.job.InvMinMaxQtyEmailSendJob;


@WebListener 
public class EmailNotifcationListner implements ServletContextListener{
	 private ScheduledExecutorService scheduler;

	//Manufact
		 @Override
		   public void contextInitialized(ServletContextEvent event) {
			 
			 try {
			 String timeToStart = "04:00:00";
			  SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss");
			  SimpleDateFormat formatOnlyDay = new SimpleDateFormat("yyyy-MM-dd");
			  Date now = new Date();
			  Date dateToStart;
				dateToStart = format.parse(formatOnlyDay.format(now) + " at " + timeToStart);
			  long diff = dateToStart.getTime() - now.getTime();
			  if (diff < 0) {
			    // tomorrow
			    Date tomorrow = new Date();
			    Calendar c = Calendar.getInstance();
			    c.setTime(tomorrow);
			    c.add(Calendar.DATE, 1);
			    tomorrow = c.getTime();
			    dateToStart = format.parse(formatOnlyDay.format(tomorrow) + " at " + timeToStart);
			    diff = dateToStart.getTime() - now.getTime();
			  }
		       //scheduler = Executors.newSingleThreadScheduledExecutor();
			    scheduler = Executors.newScheduledThreadPool(1); 
		       //scheduler.scheduleAtFixedRate(new InvMinMaxQtyEmailSendJob(),getInitialDelay(),2, TimeUnit.MINUTES);
			   //scheduler.scheduleAtFixedRate(new ExpiryEmailSendJob(),getInitialDelay(),2, TimeUnit.MINUTES);
		       scheduler.scheduleAtFixedRate(new InvMinMaxQtyEmailSendJob(),TimeUnit.MILLISECONDS.toSeconds(diff) , 24*60*60, TimeUnit.SECONDS);
		       scheduler.scheduleAtFixedRate(new ExpiryEmailSendJob(),TimeUnit.MILLISECONDS.toSeconds(diff) , 24*60*60, TimeUnit.SECONDS);
			 } catch (ParseException e) {
				 // TODO Auto-generated catch block
				 e.printStackTrace();
			 }
		    }
		    
	    @Override
	    public void contextDestroyed(ServletContextEvent event) {
	        scheduler.shutdownNow();
	    }
	    
	 public long getInitialDelayForMinMax(){
		 long initialDelay = 0;
		 SimpleDateFormat simpleDateFormat1 = null;
		 SimpleDateFormat simpleDateFormat2 = null;
		 Date date = null,next_Day = null;	
		 String next_Day_Str = "";
		 Calendar calendar = null;
		 long diff = 0;
	    try{ 
		 simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		 date = simpleDateFormat1.parse(simpleDateFormat1.format(new Date()));
		 calendar = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.DAY_OF_YEAR, 1);
		 next_Day_Str = simpleDateFormat2.format(calendar.getTime()); 
		   next_Day_Str = next_Day_Str +" 02:00:00";
		  //next_Day_Str = simpleDateFormat2.format(new Date()) +" 01:15:00";
		 next_Day = simpleDateFormat1.parse(next_Day_Str);
		//in milliseconds
		  diff = next_Day.getTime() - date.getTime();
		  /*initialDelay = (int)diff / (60 * 60 * 1000) % 24;*/
		  //initialDelay = (diff / 1000) / 60;
		  System.out.println(TimeUnit.MICROSECONDS.toMinutes(diff));
		  System.out.println(diff/60000);
	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
		 return (diff/60000);
	 }
	 
	 public int getInitialDelay(){
		 int initialDelay = 0;
		 SimpleDateFormat simpleDateFormat1 = null;
		 SimpleDateFormat simpleDateFormat2 = null;
		 Date date = null,next_Day = null;	
		 String next_Day_Str = "";
		 Calendar calendar = null;
		 long diff = 0;
	    try{ 
		 simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		 date = simpleDateFormat1.parse(simpleDateFormat1.format(new Date()));
		 calendar = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.DAY_OF_YEAR, 1);
		 next_Day_Str = simpleDateFormat2.format(calendar.getTime()); 
		 next_Day_Str = next_Day_Str +" 02:00:00";
		 next_Day = simpleDateFormat1.parse(next_Day_Str);
		//in milliseconds
		  diff = next_Day.getTime() - date.getTime();
		  initialDelay = (int)diff / (60 * 60 * 1000) % 24;

	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
		 return initialDelay;
	 }
	 
	 /*STart the code added by Jayesh on 01/03/2021*/
	 
	 public int getInitialDelay(String intitialTime){
		 int initialDelay = 0;
		 SimpleDateFormat simpleDateFormat1 = null;
		 SimpleDateFormat simpleDateFormat2 = null;
		 Date date = null,next_Day = null;	
		 String next_Day_Str = "";
		 Calendar calendar = null;
		 long diff = 0;
	    try{ 
		 simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		 simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");
		 date = simpleDateFormat1.parse(simpleDateFormat1.format(new Date()));
		 calendar = Calendar.getInstance();
		 calendar.setTime(date);
		 calendar.add(Calendar.DAY_OF_YEAR, 1);
		 next_Day_Str = simpleDateFormat2.format(calendar.getTime()); 
		 next_Day_Str = next_Day_Str +" 02:00:00";
		 next_Day = simpleDateFormat1.parse(next_Day_Str);
		//in milliseconds
		  diff = next_Day.getTime() - date.getTime();
		  initialDelay = (int)diff / (60 * 60 * 1000) % 24;

	    }catch(Exception ex){
	    	ex.printStackTrace();
	    }
		 return initialDelay;
	 }
	 
	 /*End the code added by Jayesh on 01/03/2021*/
}

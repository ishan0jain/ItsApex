package org.first.itsApex.controller;

public class ThreadPractice{
	public static void main(String [] args)  {
		ThreadClass t1= new ThreadClass("name1", 10000);
		t1.start();
		try{
			t1.join(1000);
		} catch(Exception e) {
			System.out.println("Thread name1 was enterupted after 1 sec due to above");
		}
		t1.interrupt();
		ThreadClass t2= new ThreadClass("name2",600);
		t2.start();
		ThreadClass t3= new ThreadClass("name3",300);
		t3.start();
		ThreadClass t4= new ThreadClass("name4",200);
		t4.start();
		ThreadClass t5= new ThreadClass("name5",200);
		t5.start();
		ThreadClass t6= new ThreadClass("name6",200);
		t6.start();
		
	}

}

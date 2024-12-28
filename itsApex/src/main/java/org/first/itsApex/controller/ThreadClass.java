package org.first.itsApex.controller;

public class ThreadClass extends Thread{
	public String name;
	public int wait;
	ThreadClass(String name,int wait){
		this.name = name;
		this.wait= wait;
	}
	public void run() {
		try {
			sleep(this.wait);
			System.out.println("Inside Thread Class" + this.name);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Thread name1 was enterupted after 1 sec from outside");
		}
		
	}
}

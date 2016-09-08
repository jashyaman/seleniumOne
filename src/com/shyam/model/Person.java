package com.shyam.model;

public class Person implements Comparable<Person>{

	private final int index;
	private final String name;
	private final String position;
	private final String office;
	private final int age;
	private final String startDate;
	private final double salary;
	
	public Person(int index, String name, String position, String office, int age, String startDate, double salary) {
		super();
		this.index = index;
		this.name = name;
		this.position = position;
		this.office = office;
		this.age = age;
		this.startDate = startDate;
		this.salary = salary;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public String getOffice() {
		return office;
	}

	public int getAge() {
		return age;
	}

	public String getStartDate() {
		return startDate;
	}

	public double getSalary() {
		return salary;
	}

	
	@Override
	public String toString() {
		return name + " works at " + office + " as a " + position + " earns " + salary + " and is " + age + " years old and started working on " + startDate;
	}

	@Override
	public int compareTo(Person o) {
		if( this.index > o.index) 	  return 1;
		else if(this.index < o.index) return -1;
		else 						  return 0;
		
	}

	public int getIndex() {
		return index;
	}
	
	
	
}

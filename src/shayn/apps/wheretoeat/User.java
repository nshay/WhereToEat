package shayn.apps.wheretoeat;

import java.util.ArrayList;

public class User {
	
	//======================================================
	//		CONSTRUCTORS
	//======================================================
	
	public User () {
		name = "";
	}
	
	public User (String name) {
		this.name = name;
	}
	
	public User (String name, ArrayList <String> restaurants) {
		this.name = name;
		this.restaurants = restaurants;
	}
	
	//======================================================
	//		SETTERS AND GETTERS
	//======================================================
	
	public String getName () {
		return name;
	}
	
	public ArrayList <String> getRestaurantList () {
		return restaurants;
	}
	
	public void addRestaurant (String newRestName) {
		restaurants.add (newRestName);
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public void setRestList (ArrayList<String> restList) {
		this.restaurants = restList;
	}
	
	//======================================================
	//		PRIVATE MEMBERS
	//======================================================
	
	private String name;
	private ArrayList <String> restaurants;

}

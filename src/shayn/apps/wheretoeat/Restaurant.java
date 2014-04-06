package shayn.apps.wheretoeat;

import java.util.List;

public class Restaurant {
	public enum eCategory {
		eDEFAULT,
		eHEALTHY,
		eCHEAP,
		eFAST,
	}
	
	//======================================================
	//		CONSTRUCTORS
	//======================================================
	
	public Restaurant () {
		name = "";
	}
	
	public Restaurant (String name) {
		this.name = name;
	}
	
	//======================================================
	//		SETTERS & GETTERS
	//======================================================	
	
	public String getRestaurantName () {
		return name;
	}
	
	public String toString () {
		return name;
	}
	
	public List <eCategory> getCategoryList () {
		return categoryList;
	}
	
	public void addCategory (eCategory newCategory) {
		categoryList.add(newCategory);
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	//======================================================
	//		PRIVATE MEMBERS
	//======================================================
	
	private String name;
	private List <eCategory> categoryList;

}

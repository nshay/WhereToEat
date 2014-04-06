package shayn.apps.wheretoeat;

import java.util.List;

public class Group {
	
	//======================================================
	//		CONSTRUCTORS
	//======================================================
	
	public Group () {
		groupName = "";
	}
	
	public Group (String groupName) {
		this.groupName = groupName;
	}
	
	//======================================================
	//		SETTERS AND GETTERS
	//======================================================
	
	public String getGroupName () {
		return groupName;
	}
	
	public void setGroupName (String groupName) {
		this.groupName = groupName;
	}
	
	public void addUser (User user) {
		userList.add (user);
	}
	
	public List <User> getUserList () {
		return userList;
	}
	
	//======================================================
	//		PRIVATE MEMBERS
	//======================================================
	
	private String groupName;
	private List <User> userList;

}

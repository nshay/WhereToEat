package shayn.apps.wheretoeat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserListActivity extends ListActivity {
	
	final static String TAG = "UserListActivity";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryAndUpdateListView();
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	TextView mClickedTextView = (TextView) v.findViewById(android.R.id.text1);
    	String currUser = (String) mClickedTextView.getText();
    	String userRestListStr = WhereToEat.mWhereToEatData.getUserRestaurants(currUser);
    	
    	Log.d (TAG, "userRestListStr = " + userRestListStr);
    	
    	createUserRestListDialog(currUser, userRestListStr);
    }
    
    public Boolean addUser (String name, ArrayList <String> restList) {
    	Boolean res = false;
    	Log.i(TAG, "Adding new user = " + name);
    	if (!name.isEmpty()) {
    		User newUser = new User (name, restList);
    		WhereToEat.mWhereToEatData.insert (newUser);

    		queryAndUpdateListView();

    		Toast.makeText(getBaseContext(), name + " added successfully", Toast.LENGTH_LONG).show();
    		
    		res = true;
    	}
    	else {
    		//TODO: don't close dialog here
    		Toast.makeText(getBaseContext(), "Error: Invalid Entry!", Toast.LENGTH_LONG).show();
    	}
    	
    	return res;
    }
    
    private void queryAndUpdateListView () {
    	String[] from = new String[] { "user_name" };
        int[] to = new int[] { android.R.id.text1 };

    	Cursor cursor = WhereToEat.mWhereToEatData.getUsers();
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter (this, android.R.layout.simple_list_item_1, cursor, from, to, 0);
    	
    	setListAdapter(adapter);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.users, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;

    	switch (item.getItemId()) {
    	case R.id.add_user:
    		createAddUserDialog();
    		return true;
    	case R.id.mainButton:
    		intent = new Intent (this, WhereToEat.class);
    		startActivity (intent);
    		return true;
    	case R.id.restaurantsButton:
    		intent = new Intent (this, RestaurantListActivity.class);
    		startActivity (intent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    private void createUserRestListDialog (final String user, final String restListStr) {
    	Log.i (TAG, "createUserRestListDialog() called for user " + user);
    	final List<String> restList = Arrays.asList(restListStr.split(","));
    	
    	ListView listView = new ListView (this);
    	ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, restList);
    	listView.setAdapter(adapter);

    	AlertDialog.Builder userRestListDialog = new AlertDialog.Builder(this);
    	userRestListDialog.setTitle (user + "'s Restaurant List")
    	.setView(listView)
    	.setNegativeButton("OK", null)
    	.setPositiveButton("Update Restaurants", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			createAddRestaurantDialog (user, restListStr);
    		}
    	})
    	.setNeutralButton("Delete User", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			createDeleteUserDialog (user);
    		}
    	})
    	.create()
    	.show();
    }
    
    private void createDeleteUserDialog ( final String clickedUser ) {
    	AlertDialog.Builder deleteRestAlert = new AlertDialog.Builder(this);
    	
    	deleteRestAlert.setTitle("Delete Restaurant")
    	.setMessage("Are you sure you want to delete " + clickedUser + "?")
    	.setIcon(R.drawable.warning)
    	.setNegativeButton("Cancel", null)
    	.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			deleteUser (clickedUser);
    		}
    	})
    	.create()
    	.show();
    	
    }
    
    private void deleteUser (String userName) {
    	if (userName.isEmpty()) {
    		return;
    	}
    	
    	WhereToEat.mWhereToEatData.deleteUser(userName);
    	queryAndUpdateListView();
    	Toast.makeText(getBaseContext(), userName + " deleted successfully", Toast.LENGTH_LONG).show();
    }
    
    private void createAddRestaurantDialog (final String userName, final String userRestListStr) {
    	Log.i (TAG, "createAddRestaurantDialog() called");
    	Log.i (TAG, "userRestList = " + userRestListStr);
    	final List<String> userRestList = new ArrayList <String>(Arrays.asList(userRestListStr.split(",")));
    	String restListStr = WhereToEat.mWhereToEatData.getRestaurantsAsString();
    	Log.i (TAG, "restListStr = " + restListStr);
    	List<String> restList = Arrays.asList(restListStr.split(","));
    	
    	Log.i (TAG, "restList = " + restList.toString());
    	
    	boolean [] selectedItems = new boolean [restList.size()];
    	final CharSequence [] items = new CharSequence [restList.size()];

    	String curr = null;
    	for (int i = 0; i < restList.size(); i++) {
    		curr = restList.get(i).trim();
    		if (userRestList.contains(curr)) {
    			Log.i (TAG, "found match for " + curr);
    			selectedItems[i] = true;
    		}
    		else {
    			Log.i (TAG, curr + " not found");
    			selectedItems[i] = false;
    		}
    		items[i] = curr;
    	}
    	
    	AlertDialog.Builder addRestDialog = new AlertDialog.Builder(this);
    	addRestDialog.setTitle("Update Restaurants")
    	.setMultiChoiceItems(items, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
    	    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
    	    	String restName = items[i].toString();    	    	
    	    	if (isChecked) {
    	    		Log.i (TAG, restName + " was checked");
    	    		if (!userRestList.contains(restName)) {
    	    			Log.i (TAG, "Adding " + restName);
    	    			userRestList.add(restName);
    	    		}
    	    	} else {
    	    		Log.i (TAG, "Removing " + restName);
    	    		userRestList.remove(restName);
    	    	}
    	    	
    	    	Log.i (TAG, userRestList.toString());
    	    }
    	})
    	.setPositiveButton ("Update", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			updateRestaurantListForUser (userName, (ArrayList<String>) userRestList);
    			Log.i(TAG, userRestList.toString());
    		}
    	})
    	.setNegativeButton ("Cancel", null)
    	.create()
    	.show();
    }
    
    private void updateRestaurantListForUser (final String userName, ArrayList<String> userRestList) {
    	User user = new User (userName, userRestList);
    	WhereToEat.mWhereToEatData.updateUser(user);
    	
    	Toast.makeText(getBaseContext(), userName + " successfully updated", Toast.LENGTH_LONG).show();
    }
    
    private void createAddUserDialog () {
    	Log.i (TAG, "createAddUserDialog() called");
    	final ArrayList <String> selectedRestList = new ArrayList <String>();
    	final Cursor restCursor = WhereToEat.mWhereToEatData.getRestaurants();
    	final EditText addUserEditText = new EditText(this);
    	
    	addUserEditText.setHint(R.string.dialog_enter_user_message);

    	AlertDialog.Builder addUserAlert = new AlertDialog.Builder(this);
         addUserAlert.setTitle(R.string.dialog_new_user_title)
         .setView (addUserEditText)
         .setMultiChoiceItems(restCursor, "isChecked", "rest_name", new DialogInterface.OnMultiChoiceClickListener(){
        	 public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        		 Log.i(TAG, "onClick(..) called with value " + isChecked);
        		 if (isChecked) {
        			 if (restCursor.moveToPosition(which)) {
        				 String restName = restCursor.getString(restCursor.getColumnIndex("rest_name"));
        				 Log.i (TAG, "selected " + restName);
        				 selectedRestList.add(restName);
        			 }
        		 }
        		 else {
        			 if (restCursor.moveToPosition(which)) {
        				 String restName = restCursor.getString(restCursor.getColumnIndex("rest_name"));
        				 Log.i (TAG, "un-selected " + restName);
        				 ListIterator <String> i = selectedRestList.listIterator();
        				 while (i.hasNext()) {
        					 String curr = i.next();
        					 if (curr.equalsIgnoreCase(restName)) {
        						 selectedRestList.remove(curr);
        						 break;
        					 }
        				 }
        			 }
        		 }
        	 }
         })
         .setPositiveButton("Add", new DialogInterface.OnClickListener() {
        	 public void onClick(DialogInterface dialog, int whichButton) {
        		 addUser (addUserEditText.getText().toString().toLowerCase(), selectedRestList);
        		 Log.i(TAG, selectedRestList.toString());
        	 }
         })
         .setNegativeButton("Cancel", null)
         .create()
         .show();
    }
}











package shayn.apps.wheretoeat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class WhereToEat extends Activity {
	
	final static String TAG = "WhereToEat";
	static WhereToEatData mWhereToEatData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_where_to_eat);
		
		mWhereToEatData = new WhereToEatData (this);
		
		addListenerOnButton();
	}
	
	public void addListenerOnButton() {
		 
		ImageView imageView = (ImageView) findViewById(R.id.whereToEatImage);
 
		imageView.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				createUserSelectDialog();
			}
 
		});
 
	}
	
	public void startRestaurantListActivity () {
		Intent intent = new Intent (this, RestaurantListActivity.class);
		startActivity (intent);
	}
	
	public void startUserListActivity () {
		Intent intent = new Intent (this, UserListActivity.class);
		startActivity (intent);
	}

	private void createUserSelectDialog() {
		Log.i (TAG, "createUserSelectDialog() called");
		String userListStr = mWhereToEatData.getUsersAsString();
    	final List<String> userList = Arrays.asList(userListStr.split(","));
    	boolean [] selectedItems = new boolean [userList.size()];
    	final CharSequence [] userNames = new CharSequence [userList.size()];
    	
    	for (int i = 0; i < userList.size(); i++) {
    		selectedItems[i] = true;
    		userNames[i] = userList.get(i);
    	}
    	
    	final List<String> dinerList = new ArrayList<String>(userList);

    	AlertDialog.Builder userRestListDialog = new AlertDialog.Builder(this);
    	userRestListDialog.setTitle ("Select Diners")
    	.setMultiChoiceItems(userNames, selectedItems, new DialogInterface.OnMultiChoiceClickListener() {
    	    public void onClick(DialogInterface dialogInterface, int i, boolean isChecked) {
    	    	String userName = userNames[i].toString();    	    	
    	    	if (isChecked) {
    	    		Log.i (TAG, userName + " was checked");
    	    		if (!dinerList.contains(userName)) {
    	    			Log.i (TAG, "Adding " + userName);
    	    			dinerList.add(userName);
    	    		}
    	    	} else {
    	    		Log.i (TAG, "Removing " + userName);
    	    		dinerList.remove(userName);
    	    	}
    	    	
    	    	Log.i (TAG, dinerList.toString());
    	    }
    	})
    	.setNegativeButton("Cancel", null)
    	.setPositiveButton("Where to Eat?", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			getRecommendation (dinerList);
    		}
    	})
    	.create()
    	.show();

	}
	
	private void getRecommendation (List<String> dinerList) {
		Set <String> unionedList = getUnionOfList(dinerList);
		String selection = getRandomSelectionFromUnion (unionedList);
		createRecommendationPopup (selection);
	}
	
	private void createRecommendationPopup (String selection) {
		String message = (selection == "") ? "No Restaurants in Common" : selection;
		
		AlertDialog.Builder userRestListDialog = new AlertDialog.Builder(this);
    	userRestListDialog.setTitle ("We Recommend:")
    	.setMessage(message)
    	.setNegativeButton("OK", null)
    	.create()
    	.show();
	}
	
	private String getRandomSelectionFromUnion (Set <String> unionedList) {
		String selection = "";
		
		if (unionedList.size() > 0) {
			int random = (int) (Math.random() * unionedList.size());
			String[] unionArray = new String [unionedList.size()]; 
			unionedList.toArray(unionArray);
			selection = unionArray[random];

			Log.i (TAG, "random = " + random + ", " + selection);
		}
		
		return selection;
	}
	
	private Set <String> getUnionOfList (List<String> dinerList) {
		Set <String> unionedList = new HashSet <String>();
		
		for (int i = 0; i < dinerList.size(); i++) {
			String currName = dinerList.get(i);
			String currResListStr = mWhereToEatData.getUserRestaurants(currName);
			
			List <String> restsAsList = Arrays.asList(currResListStr.split(","));
			
			if (i == 0) {
				unionedList.addAll(restsAsList);
			}
			else {
				unionedList.retainAll(restsAsList);
			}
		}
		
		Log.i (TAG, unionedList.toString());
		
		return unionedList;
	}
	
	public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    	case R.id.restaurantsButton:
    		startRestaurantListActivity();
    		return true;
    	case R.id.usersButton:
    		startUserListActivity();
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }
	
	public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}

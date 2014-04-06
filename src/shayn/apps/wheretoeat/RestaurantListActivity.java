package shayn.apps.wheretoeat;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RestaurantListActivity extends ListActivity implements OnTouchListener {
	
	final static String TAG = "RestaurantListActivity";
	OnSwipeTouchListener mSwipeListener = new OnSwipeTouchListener(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queryAndUpdateListView();
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	TextView mClickedTextView = (TextView) v.findViewById(android.R.id.text1);
    	String clickedRest = (String) mClickedTextView.getText();
    	
    	Log.d (TAG, "clickedRest = " + clickedRest);
    	
    	createDeleteRestDialog(clickedRest);
    }
    
    public Boolean addRestaurant (String name) {
    	Boolean res = false;
    	Log.i(TAG, "Adding new restraunt = " + name);
    	if (!name.isEmpty()) {
    		Restaurant newRest = new Restaurant (name);
    		WhereToEat.mWhereToEatData.insert(newRest);

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
    
    private void deleteRestaurant (String restName) {
    	if (restName.isEmpty()) {
    		return;
    	}
    	
    	WhereToEat.mWhereToEatData.deleteRestaurant(restName);
    	queryAndUpdateListView();
    	Toast.makeText(getBaseContext(), restName + " deleted successfully", Toast.LENGTH_LONG).show();
    }
    
    private void queryAndUpdateListView () {
    	String[] from = new String[] { "rest_name" };
        int[] to = new int[] { android.R.id.text1 };

    	Cursor cursor = WhereToEat.mWhereToEatData.getRestaurants();
    	
    	SimpleCursorAdapter adapter = new SimpleCursorAdapter (this, android.R.layout.simple_list_item_1, cursor, from, to, 0);
    	setListAdapter(adapter);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.restaurants, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
    	Intent intent = null;
    	
    	switch (item.getItemId()) {
    	case R.id.add_restaurant:
    		createAddRestaurantDialog();
    		return true;
    	case R.id.mainButton:
    		intent = new Intent (this, WhereToEat.class);
    		startActivity (intent);
    		return true;
    	case R.id.usersButton:
    		intent = new Intent (this, UserListActivity.class);
    		startActivity (intent);
    		return true;
    	default:
    		return super.onOptionsItemSelected(item);
    	}
    }

    private void createAddRestaurantDialog () {
    	AlertDialog.Builder addRestAlert = new AlertDialog.Builder(this);
    	final EditText addRestEditText = new EditText(this);
    	
    	addRestAlert.setTitle(R.string.dialog_new_rest_title);
    	addRestAlert.setMessage(R.string.dialog_enter_rest_message);
    	addRestAlert.setView (addRestEditText);

    	addRestAlert.setPositiveButton("Add", new DialogInterface.OnClickListener() {
    	public void onClick(DialogInterface dialog, int whichButton) {
    		addRestaurant (addRestEditText.getText().toString().toLowerCase());
    	  }
    	});

    	addRestAlert.setNegativeButton("Cancel", null);
    	
    	addRestAlert.show();
    }
    
    private void createDeleteRestDialog ( final String clickedRest ) {
    	AlertDialog.Builder deleteRestAlert = new AlertDialog.Builder(this);
    	
    	deleteRestAlert.setTitle("Delete Restaurant")
    	.setMessage("Are you sure you want to delete " + clickedRest + "?")
    	.setIcon(R.drawable.warning)
    	.setNegativeButton("Cancel", null)
    	.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
    		public void onClick(DialogInterface dialog, int whichButton) {
    			deleteRestaurant (clickedRest);
    		}
    	})
    	.create()
    	.show();
    	
    }

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return mSwipeListener.onTouch(v, event);
	}
}











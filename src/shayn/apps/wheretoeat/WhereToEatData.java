package shayn.apps.wheretoeat;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class WhereToEatData {
	public final String TAG = "WhereToEatData";
	
	public static final String DB_NAME     = "whereToEat.db";
	public static final int    DB_VERSION  = 2;
	public static final String TABLE_RESTS = "restaurants";
	public static final String TABLE_USERS = "users";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_REST_NAME      = "rest_name";
	public static final String C_USER_NAME      = "user_name";
	public static final String C_IS_CHECKED     = "isChecked"; //All zero, needed for AlertDialog
	public static final String C_USER_REST_LIST = "user_rest_list"; //All zero, needed for AlertDialog
	
	Context context;
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	public WhereToEatData (Context context) {
		this.context = context;
		dbHelper = new DbHelper();
	}
	
	public void insert (Restaurant rest) {
		ContentValues values = new ContentValues();
		values.put (C_REST_NAME, rest.getRestaurantName());
		values.put (C_IS_CHECKED, 0);

		db = dbHelper.getWritableDatabase();
		db.insert (TABLE_RESTS, null, values);
	}
	
	public void insert (User user) {
		ContentValues values = new ContentValues();
		values.put (C_USER_NAME, user.getName());
		
		String restaurants = restaurantListToString (user.getRestaurantList());
		values.put (C_USER_REST_LIST, restaurants);
		
		db = dbHelper.getWritableDatabase();
		db.insert (TABLE_USERS, null, values);
	}
	
	public void updateUser (User user) {
		ContentValues values = new ContentValues();
		String restaurants = restaurantListToString (user.getRestaurantList());
		values.put (C_USER_REST_LIST, restaurants);
		
		String [] whereArgs = {user.getName()};
		
		db = dbHelper.getWritableDatabase();
		db.update(TABLE_USERS, values, C_USER_NAME + " = ? ", whereArgs);
	}
	
	public void deleteUser (String userName) {
		String [] whereArgs = {userName};
		
		db = dbHelper.getWritableDatabase();
		db.delete(TABLE_USERS, C_USER_NAME + " =? ", whereArgs);
	}
	
	public Cursor getRestaurants () {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_RESTS, null, null, null, null, null, C_REST_NAME);
				
		return cursor;
	}
	
	public void deleteRestaurant (String restName) {
		String [] whereArgs = {restName};
		
		db = dbHelper.getWritableDatabase();
		db.delete(TABLE_RESTS, C_REST_NAME + " =? ", whereArgs);
	}
	
	public String getRestaurantsAsString () {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_RESTS, null, null, null, null, null, C_REST_NAME);
		
		return cursorToString (cursor, C_REST_NAME);
	}
	
	public Cursor getUsers () {
		db = dbHelper.getReadableDatabase();
		Cursor cursor = db.query(TABLE_USERS, null, null, null, null, null, C_USER_NAME);
		
		return cursor;
	}
	
	public String getUsersAsString () {
		return cursorToString(getUsers(), C_USER_NAME);
	}
	
	public String getUserRestaurants (String userName) {
		Log.i (TAG, "getUserRestaurants() called for " + userName);
		String [] columns = {C_USER_REST_LIST};
		String [] whereArgs = {userName};
		db = dbHelper.getReadableDatabase();
		
		Cursor cursor = db.query(TABLE_USERS, columns, C_USER_NAME + " = ? ", whereArgs, null, null, null);
		
		return cursorToString (cursor, C_USER_REST_LIST);
	}
	
	public String cursorToString (Cursor cursor, String column_name) {
		String restList = "";

		while (cursor.moveToNext()) {
			restList += cursor.getString(cursor.getColumnIndex(column_name)).trim();
			if (!cursor.isLast()) {
				restList += ",";
			}
		}

		cursor.close();
		
		Log.i (TAG, restList);

		return restList;
	}
	
	private String restaurantListToString (ArrayList<String> restList) {
		String restaurantsStr = "";
		for (int i = 0; i < restList.size(); i++) {
			String curr = restList.get(i);
			restaurantsStr += curr.trim();
			if ((i + 1) != restList.size()) {
				restaurantsStr += ",";
			}
		}
		
		return restaurantsStr;
	}
	
	class DbHelper extends SQLiteOpenHelper {

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);
			
			Log.i (TAG, "DbHelper Constructor");
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String restSql = String.format("create table if not exists %s (%s int primary key, %s text, %s int)",
					TABLE_RESTS, C_ID, C_REST_NAME, C_IS_CHECKED);
			String userSql = String.format("create table if not exists %s (%s int primary key, %s text, %s text)",
					TABLE_USERS, C_ID, C_USER_NAME, C_USER_REST_LIST);			
			
			Log.i (TAG, "onCreate with SQL: " + restSql);
			Log.i (TAG, "onCreate with SQL: " + userSql);
			
			db.execSQL(restSql);
			db.execSQL(userSql);
		}

		@Override	
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			String sql = String.format("alter table %s add column %s text default \'\'", TABLE_USERS, C_USER_REST_LIST);
			Log.i (TAG, "onUpgrade with SQL: " + sql);
			db.execSQL(sql);
		}
		
	}
}

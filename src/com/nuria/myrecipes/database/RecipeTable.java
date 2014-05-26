package com.nuria.myrecipes.database;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RecipeTable {
	// Database table
	public static final String TABLE_RECIPE = "recipe";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_INGREDIENTS = "ingredients";
	public static final String COLUMN_DIRECTIONS = "directions";

	// Database creation SQL statement
	private static final String DATABASE_CREATE_TABLE = 
			"create table " + TABLE_RECIPE 
			+ "(" + COLUMN_ID + " integer primary key autoincrement, "
			+ COLUMN_INGREDIENTS + " text null, " + COLUMN_DIRECTIONS
			+ " text null);";
	
	public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE_TABLE);
	  }
	
	//TODO review 
	 public static void onUpgrade(SQLiteDatabase database, int oldVersion,
		      int newVersion) {
		    Log.w(RecipeTable.class.getName(), "Upgrading database from version "
		        + oldVersion + " to " + newVersion
		        + ", which will destroy all old data");
		    database.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
		    onCreate(database);
		  }
}

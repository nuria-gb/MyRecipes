package com.nuria.myrecipes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyRecipesDatabaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "myRecipes.db"; //$NON-NLS-1$
    private static final int DATABASE_VERSION = 1;
    
	public MyRecipesDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		RecipeTable.onCreate(database);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
	        int newVersion) {
		RecipeTable.onUpgrade(database, oldVersion, newVersion);
		
	}

}

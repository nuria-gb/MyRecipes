package com.nuria.myrecipes.database;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class RecipeContentProvided extends ContentProvider{
	
	private static final int RECIPES = 10;
	
	private static final int RECIPES_ID = 20;
	
	private static final String AUTHORITY = "com.nuria.myrecipes.recipe"; //$NON-NLS-1$
	
	private static final String BASE_PATH = "recipes"; //$NON-NLS-1$
	
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY //$NON-NLS-1$
		      + "/" + BASE_PATH); 											 //$NON-NLS-1$
	
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
		      + "/recipe"; //$NON-NLS-1$
	
	 private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH, RECIPES);
	    sURIMatcher.addURI(AUTHORITY, BASE_PATH+ "/#", RECIPES_ID); //$NON-NLS-1$
	  }
	
	private MyRecipesDatabaseHelper dbHelper;
	
	@Override
	public boolean onCreate() {
		this.dbHelper = new MyRecipesDatabaseHelper(this.getContext()); 
		return true;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int deletedRows = 0;
		
		this.checkUri(uri);
		
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		
		deletedRows = db.delete(RecipeTable.TABLE_RECIPE, 
								 selection,  
								 selectionArgs);

	    getContext().getContentResolver().notifyChange(uri, null);
	    
	    return deletedRows;
	}

	

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		this.checkUri(uri);
		
		SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		
		long id = db.insert(RecipeTable.TABLE_RECIPE, 
				                       RecipeTable.COLUMN_DIRECTIONS, 
				                       values);
		//notify changes
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id); //$NON-NLS-1$
	}

	

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		this.checkUri(uri);
		
		 SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		 Cursor cursor = db.query(RecipeTable.TABLE_RECIPE, 
					                 projection, 
					                 selection, 
					                 selectionArgs, null, null, 
					                 sortOrder);
		 //we do this in order to make sure that any potential listenrs are notified
		 cursor.setNotificationUri(getContext().getContentResolver(), uri);
		 return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		this.checkUri(uri);
		
		 SQLiteDatabase db = this.dbHelper.getWritableDatabase();
		 int updatedRows = 0;
		 
		 updatedRows = db.update(RecipeTable.TABLE_RECIPE, 
					               values, 
					               selection,  
				                   selectionArgs);
		 getContext().getContentResolver().notifyChange(uri, null);
		return updatedRows;
	}
	
	private void checkUri(Uri uri) {
		int uriType = sURIMatcher.match(uri);
		
		if(uriType!=RECIPES && uriType!=RECIPES_ID){
			throw new IllegalArgumentException("Unknown URI: " + uri); //$NON-NLS-1$
		}
	}

}

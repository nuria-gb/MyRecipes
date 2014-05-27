package com.nuria.myrecipes.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.nuria.myrecipes.R;
import com.nuria.myrecipes.database.RecipeContentProvided;
import com.nuria.myrecipes.database.RecipeTable;

public class MainActivity extends ListActivity{

	
	private SimpleCursorAdapter mSimpleCursorAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		//I start a loader in order to load my data 
		this.getLoaderManager().initLoader(0, null, new MyCursorLoader());
		
		this.mSimpleCursorAdapter = new SimpleCursorAdapter(this, 
				                                            R.layout.row_layout, 
				                                            null,//cursor, the callback will set the cursor up once it will be loaded 
				                                            new String[] { 
				  				                                  RecipeTable.COLUMN_NAME, 
				  				                                  RecipeTable.COLUMN_DIRECTIONS}, 
				  				                            new int[] { 
						  				                          R.id.tvRowName, 
						  				                          R.id.tvRowDirections}, 
						  				                    CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);//I'm not sure of what this flag does.
				
		this.setListAdapter(this.mSimpleCursorAdapter);
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new ModeCallback());
		

	}
	
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menubar_new_recipe:
			Toast.makeText(this, "new recipe", Toast.LENGTH_SHORT).show(); //$NON-NLS-1$
			Intent intent = new Intent(this,RecipeActivity.class);
			this.startActivity(intent);
			break;
		
		default:
			break;
		}
		return true;
	}
	
	@Override
	 protected void onListItemClick(ListView l, View v, int position, long id){
		//TODO check correct action mode, if I am in CAB DO NOT GO to the detail
		Uri uri = Uri.parse(RecipeContentProvided.CONTENT_URI + "/"           //$NON-NLS-1$
		          + Long.toString(id));
		
		Intent intent = new Intent(this, RecipeActivity.class);
		intent.putExtra(RecipeContentProvided.CONTENT_ITEM_TYPE, uri);

		this.startActivity(intent);
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
	}
	
	private class MyCursorLoader implements LoaderManager.LoaderCallbacks<Cursor>{

		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			String[] projection = { RecipeTable.COLUMN_ID, 
									RecipeTable.COLUMN_NAME, 
									RecipeTable.COLUMN_DIRECTIONS};
			
		    CursorLoader cursorLoader = new CursorLoader(MainActivity.this,
		                                        RecipeContentProvided.CONTENT_URI,
		                                        projection, null, null, null);
		    
		    return cursorLoader;
		}

		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
			MainActivity.this.mSimpleCursorAdapter.swapCursor(cursor);
			
		}

		@Override
		public void onLoaderReset(Loader<Cursor> loader) {
			MainActivity.this.mSimpleCursorAdapter.swapCursor(null);
			
		}
		
	}
	
	private class ModeCallback implements ListView.MultiChoiceModeListener{

		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.rowselection, menu);
			mode.setTitle("Select Items");
			return true;
		}
		
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return true;
		}
		
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
            case R.id.menubar_delete:
                
                final long[] checkedItems = getListView().getCheckedItemIds();
                final int numRecipesChecked = getListView().getCheckedItemCount();
                
                getContentResolver().delete(RecipeContentProvided.CONTENT_URI, 
                		                    this.generateWhereClause(numRecipesChecked),
                		                    this.castLongArrayToString(checkedItems, numRecipesChecked));
                
                Toast.makeText(MainActivity.this,numRecipesChecked + getString(R.string.deleted) , Toast.LENGTH_SHORT).show();
                
                mode.finish();
                break;
            default:
                Toast.makeText(MainActivity.this, "Clicked " + item.getTitle(),
                        Toast.LENGTH_SHORT).show();
                break;
            }
            return true;
		}
		
		//this method could be moved to an utility class 
		private String[] castLongArrayToString(long[] array, int numItems) {
			String[] idsStr = new String[numItems];
			
			for(int i=0; i<numItems; i++){
				idsStr[i] = Long.toString(array[i]);
			}
			return idsStr;
		}

		private String generateWhereClause(int numItems) {
			StringBuffer whereClause = new StringBuffer(RecipeTable.COLUMN_ID);
			             whereClause.append(" in ("); //$NON-NLS-1$
			for(int i=0; i<numItems; i++){
				whereClause.append("?, "); //$NON-NLS-1$
			}
			whereClause.delete(whereClause.length()-2, whereClause.length());
			whereClause.append(")"); //$NON-NLS-1$
			return whereClause.toString();
		}


		@Override
		public void onDestroyActionMode(ActionMode mode) {
			
		}

		

		@Override
		public void onItemCheckedStateChanged(ActionMode mode, int position,
				long id, boolean checked) {
			
			final int checkedCount = getListView().getCheckedItemCount();
			
            switch (checkedCount) {
                case 0:
                    mode.setSubtitle(null);
                    break;
                case 1:
                    mode.setSubtitle("One item selected");
                    break;
                default:
                    mode.setSubtitle("" + checkedCount + " items selected");
                    break;
            }
			
		}
		
	}

}

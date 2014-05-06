package com.nuria.myrecipes.activities;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.nuria.myrecipes.R;
import com.nuria.myrecipes.adapters.ListRecipesAdapter;
import com.nuria.myrecipes.model.RecipeJB;

public class MainActivity extends ListActivity{

	private final int RECIPE_ACTIVITY_CODE = 1;
	
//	private ActionMode currentActionMode;
	public int selectedItem = -1;
	
	private ListRecipesAdapter listRecipeAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.listRecipeAdapter = new ListRecipesAdapter(this);
		
		this.setListAdapter(this.listRecipeAdapter);
		
		getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
		getListView().setMultiChoiceModeListener(new ModeCallback());
		
//		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
//
//			@Override
//			public boolean onItemLongClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				 
//				if (MainActivity.this.currentActionMode != null) {
//			          return false;
//			        }
//				 
//				MainActivity.this.selectedItem = position;
//				
//				// start the CAB using the ActionMode.Callback defined above
//				MainActivity.this.currentActionMode = MainActivity.this
//		            .startActionMode(MainActivity.this.mActionModeCallback);
//		        view.setSelected(true);
//		        return true;
//			}
//			
//		});
	}
	
//	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
//
//	    // called when the action mode is created; startActionMode() was called
//	    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
//	      // Inflate a menu resource providing context menu items
//	      MenuInflater inflater = mode.getMenuInflater();
//	      // assumes that you have "contexual.xml" menu resources
//	      inflater.inflate(R.menu.rowselection, menu);
//	      return true;
//	    }
//	    
//	    // the following method is called each time 
//	    // the action mode is shown. Always called after
//	    // onCreateActionMode, but
//	    // may be called multiple times if the mode is invalidated.
//	    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
//	      return false; // Return false if nothing is done
//	    }
//
//	    // called when the user selects a contextual menu item
//	    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
//	      switch (item.getItemId()) {
//	      case R.id.menubar_delete:
//	        Toast.makeText(MainActivity.this, "delete", Toast.LENGTH_SHORT).show();
//	        // the Action was executed, close the CAB
//	        mode.finish();
//	        return true;
//	      default:
//	        return false;
//	      }
//	    }
//
//	    // called when the user exits the action mode
//	    public void onDestroyActionMode(ActionMode mode) {
//	    	MainActivity.this.currentActionMode = null;
//	    	MainActivity.this.selectedItem = -1;
//	    }
//	    
//	  };
	  
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case R.id.menubar_new_recipe:
			Toast.makeText(this, "new recipe", Toast.LENGTH_SHORT).show(); //$NON-NLS-1$
			Intent intent = new Intent(this,RecipeActivity.class);
//			this.startActivity(intent);
//			intent.putExtra("Value1", "This value one for ActivityTwo ");
			this.startActivityForResult(intent, this.RECIPE_ACTIVITY_CODE);
			break;
		
		default:
			break;
		}
		return true;
	}
	
	//if I were using a cursorAdapter I had to have an _id field, this _id field
	//will be passed as a parameter in here
	@Override
	 protected void onListItemClick(ListView l, View v, int position, long id){
		Toast.makeText(this, "hola carabola: "+id, Toast.LENGTH_SHORT).show();
		//TODO check currect action mode, if I am in CAB DO NOT GO to the detail
		//retrieve recipe and send it to the edit recipe activity
		RecipeJB recipe = this.listRecipeAdapter.getItem(position);
		
		Intent intent = new Intent(this, RecipeActivity.class);
		intent.putExtra(Intent.EXTRA_STREAM , recipe);

		this.startActivityForResult(intent, this.RECIPE_ACTIVITY_CODE);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(requestCode == this.RECIPE_ACTIVITY_CODE){
			String operation = data.getStringExtra("com.nuria.myrecipes.activities.Mode");
			RecipeJB recipe = (RecipeJB) data.getSerializableExtra("com.nuria.myrecipes.activities.Recipe");
			
			if(operation.compareTo(RecipeActivity.CREATE_MODE)==0){
				recipe.setId(new Long(this.listRecipeAdapter.getCount()));
				this.listRecipeAdapter.add(recipe);
				
			} else {
			 this.listRecipeAdapter.editRecipe(recipe);
			}
			
			this.listRecipeAdapter.notifyDataSetChanged();
			
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
                
                final SparseBooleanArray checkedItems = getListView().getCheckedItemPositions();
                final int numRecipesChecked = getListView().getCheckedItemCount();
                
                for (int i=0; i<checkedItems.size(); i++){
                	if(checkedItems.valueAt(i)){
	                	RecipeJB recipeRemove = MainActivity.this.listRecipeAdapter.getItem(i);
	                	MainActivity.this.listRecipeAdapter.remove(recipeRemove);
                	}
                }
                
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

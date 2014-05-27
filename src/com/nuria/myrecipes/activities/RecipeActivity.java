package com.nuria.myrecipes.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.nuria.myrecipes.R;
import com.nuria.myrecipes.database.RecipeContentProvided;
import com.nuria.myrecipes.database.RecipeTable;

public class RecipeActivity extends Activity{
	public static final String EDIT_MODE   = "EDITING";  //$NON-NLS-1$ 
	public static final String CREATE_MODE = "CREATING"; //$NON-NLS-1$
	
	private String mode;
	private Long id;
	
	private EditText etName;
	private EditText etIngredients;
	private EditText etDirections;
	
	private Button bSaveButon;
	
//	private RecipeJB recipe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_layout);
		
		this.etName = (EditText) findViewById(R.id.etRecipeName);
		this.etIngredients = (EditText) findViewById(R.id.etRecipeIngredients);
		this.etDirections  = (EditText) findViewById(R.id.etRecipeDirections);
		this.bSaveButon    = (Button) findViewById(R.id.saveButton);
		
		Bundle extras = getIntent().getExtras();
		Uri recipeUri = extras==null ? null:
			                   (Uri) extras
				                   .getParcelable(RecipeContentProvided.CONTENT_ITEM_TYPE);
//		this.recipe = new RecipeJB();
		this.mode= RecipeActivity.CREATE_MODE;
		
		if(recipeUri != null){
			this.mode= RecipeActivity.EDIT_MODE;
			Cursor cursor = getContentResolver().query(recipeUri, 
									                   new String[]{RecipeTable.COLUMN_ID, 
																	RecipeTable.COLUMN_NAME,
									                                RecipeTable.COLUMN_INGREDIENTS,
									                                RecipeTable.COLUMN_DIRECTIONS},
							                           RecipeTable.COLUMN_ID+"=?", 
							                           new String[]{recipeUri.getLastPathSegment()}, 
							                          null);
			if(cursor.moveToFirst()){
//				this.recipe.setId(Long.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_ID))));
				this.id = Long.valueOf(cursor.getLong(cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_ID)));
				this.etName.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_NAME)));
				this.etIngredients.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_INGREDIENTS)));
				this.etDirections.setText(cursor.getString(cursor.getColumnIndexOrThrow(RecipeTable.COLUMN_DIRECTIONS)));
			}
		}
		

		
		this.bSaveButon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//here we send the state of our recipe to the main Activity in order to save it
				RecipeActivity.this.updateRecipeFieldFromView();
				
				setResult(RESULT_OK);
		        finish();
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	
	private void updateRecipeFieldFromView(){
		ContentValues values = new ContentValues();
		values.put(RecipeTable.COLUMN_NAME, this.etName.getText().toString());
		values.put(RecipeTable.COLUMN_INGREDIENTS, this.etIngredients.getText().toString());
		values.put(RecipeTable.COLUMN_DIRECTIONS, this.etDirections.getText().toString());

		if(this.mode == CREATE_MODE){
			this.getContentResolver().insert(RecipeContentProvided.CONTENT_URI, values);
		} else {
			this.getContentResolver().update(RecipeContentProvided.CONTENT_URI, values, 
					                         RecipeTable.COLUMN_ID+"=?",  //$NON-NLS-1$
					                         new String[] {this.id.toString()});
		}
	}
	
	//delete this method
	public void deleteME(){
		this.mode.length();
	}

}

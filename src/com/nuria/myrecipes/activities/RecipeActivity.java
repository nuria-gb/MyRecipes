package com.nuria.myrecipes.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.nuria.myrecipes.R;
import com.nuria.myrecipes.model.RecipeJB;

public class RecipeActivity extends Activity{
	public static final String EDIT_MODE   = "EDITING";  //$NON-NLS-1$ 
	public static final String CREATE_MODE = "CREATING"; //$NON-NLS-1$
	
	private String mode;
	
	private EditText etIngredients;
	private EditText etDirections;
	
	private Button bSaveButon;
	
	private RecipeJB recipe;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipe_layout);
		
		this.recipe = (RecipeJB) getIntent().getSerializableExtra(Intent.EXTRA_STREAM);
		this.mode= RecipeActivity.EDIT_MODE;
		
		if(this.recipe == null){
			this.recipe = new RecipeJB();
			this.mode= RecipeActivity.CREATE_MODE;
		}
		
		this.etIngredients = (EditText) findViewById(R.id.etRecipeIngredients);
		this.etDirections  = (EditText) findViewById(R.id.etRecipeDirections);
		this.bSaveButon    = (Button) findViewById(R.id.saveButton);
		
		this.etIngredients.setText(this.recipe.getIngredients());
		this.etDirections.setText(this.recipe.getDirections());
		this.bSaveButon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//here we send the state of our recipe to the main Activity in order to save it
				RecipeActivity.this.updateRecipeFieldFromView();
				
				Intent intent = new Intent();
				intent.putExtra("com.nuria.myrecipes.activities.Recipe", RecipeActivity.this.recipe);
				intent.putExtra("com.nuria.myrecipes.activities.Mode", RecipeActivity.this.mode);
		        setResult(RESULT_OK, intent);
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
		this.recipe.setIngredients(this.etIngredients.getText().toString());
		this.recipe.setDirections(this.etDirections.getText().toString());
	}
	
	//delete this method
	public void deleteME(){
		this.mode.length();
	}

}

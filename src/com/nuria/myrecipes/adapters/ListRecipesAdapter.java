package com.nuria.myrecipes.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nuria.myrecipes.R;
import com.nuria.myrecipes.model.RecipeJB;

public class ListRecipesAdapter extends ArrayAdapter<RecipeJB>{
	

	public ListRecipesAdapter(Context context) {
		  super(context, R.layout.row_layout);
		  
		  this.add(new RecipeJB(Long.valueOf(0),"chicken", "fry your chickens")); //$NON-NLS-1$ //$NON-NLS-2$  
		  this.add(new RecipeJB(Long.valueOf(1),"melon", "slice your melon"));//$NON-NLS-1$ //$NON-NLS-2$ 
		  this.add(new RecipeJB(Long.valueOf(2),"broccoli", "boil your broccoli"));//$NON-NLS-1$ //$NON-NLS-2$ 
		  this.add(new RecipeJB(Long.valueOf(3),"cod", "roast your cod"));//$NON-NLS-1$ //$NON-NLS-2$ 
		
   }
	
	public void editRecipe(RecipeJB editedRecipe){
		this.getItem( editedRecipe.getId().intValue()).setDirections(editedRecipe.getDirections());
		this.getItem( editedRecipe.getId().intValue()).setIngredients(editedRecipe.getIngredients());
	}
	
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) this.getContext()
		                                               .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowView = inflater.inflate(R.layout.row_layout, parent, false);
		
		RecipeJB recipe = this.getItem(position);
		
		TextView tvRowIngredients = (TextView) rowView.findViewById(R.id.tvRowIngredients);
		tvRowIngredients.setText(recipe.getIngredients());
		
		TextView tvRowDirections = (TextView) rowView.findViewById(R.id.tvRowDirections);
		tvRowDirections.setText(recipe.getDirections());
		
		return rowView;
	}

	

}

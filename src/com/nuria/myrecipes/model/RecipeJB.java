package com.nuria.myrecipes.model;

import java.io.Serializable;

public class RecipeJB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long   id; //until I swich to sql I will use id like if it was the position of the item on the list
	private String ingredients;
	private String directions;
	
	public RecipeJB(){
		this.ingredients  = ""; //$NON-NLS-1$ 
		this.directions   = ""; //$NON-NLS-1$ 
	}
	
	public RecipeJB(Long _id, String _ingredients, String _directions){
		this.id           = _id;
		this.ingredients  = _ingredients;
		this.directions   = _directions;
	}
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long _id) {
		this.id = _id;
	}

	public String getIngredients() {
		return this.ingredients;
	}
	
	public void setIngredients(String ingredients_) {
		this.ingredients = ingredients_;
	}
	
	public String getDirections() {
		return this.directions;
	}
	
	public void setDirections(String directions_) {
		this.directions = directions_;
	}
	
	

}

package com.example.olya.whattocook.presenter;


import android.util.Log;

import com.example.olya.whattocook.model.RecipeSearch;
import com.example.olya.whattocook.util.ApiUtils;
import com.example.olya.whattocook.network.FoodApi;
import com.example.olya.whattocook.ui.RecipesFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesPresenter {

    static final String API_KEY = "221a9145a7580bad1fa7ec991bc113b7";
    private RecipeSearch recipeSearch;
    private RecipesFragment recipesFragment;

    public RecipesPresenter(RecipesFragment recipesFragment){
        this.recipesFragment = recipesFragment;
    }

    public RecipeSearch loadRecipes(String ingredients, int page){
        FoodApi foodApi = ApiUtils.getFoodApiService();
        foodApi.getData(API_KEY, ingredients, page).enqueue(new Callback<RecipeSearch>() {
            @Override
            public void onResponse(Call<RecipeSearch> call, Response<RecipeSearch> response) {

                if(response.isSuccessful()) {
                    Log.d("MainActivity", "posts loaded from API");
                    recipeSearch = response.body();
                    recipesFragment.updateRecyclerView(recipeSearch);
                }else {
                    int statusCode  = response.code();
                    Log.d("q", Integer.toString(statusCode));
                    // handle request errors depending on status code
                }
            }

            @Override
            public void onFailure(Call<RecipeSearch> call, Throwable t) {
                Log.d("MainActivity", "error loading from API");

            }
        });
        return recipeSearch;
    }
}

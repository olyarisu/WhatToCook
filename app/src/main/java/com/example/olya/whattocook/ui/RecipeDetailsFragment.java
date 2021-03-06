package com.example.olya.whattocook.ui;



import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.olya.whattocook.R;
import com.example.olya.whattocook.presenter.RecipeDetailsPresenter;
import com.example.olya.whattocook.model.RecipeDetails;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RecipeDetailsFragment extends Fragment{

    ToggleButton toggleButton;
    View rootView;
    RecipeDetailsPresenter recipeDetailsPresenter;
    private ArrayList<String> shoppingCart = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        recipeDetailsPresenter = new RecipeDetailsPresenter(this, getArguments().getString("id"));

        recipeDetailsPresenter.loadRecipeDetails();

        return rootView;
    }

    public void setView(final RecipeDetails recipeDetails){
        LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.linear_layout);
        ImageView image = (ImageView) getActivity().findViewById(R.id.image_recipe);
        TextView textTitle = (TextView) getActivity().findViewById(R.id.text_title);
        TextView textPublisher = (TextView) getActivity().findViewById(R.id.text_publisher);
        TextView textRating = (TextView) getActivity().findViewById(R.id.rating);
        ListView listIngredients = (ListView) getActivity().findViewById(R.id.list_ingredients);
        Button buttonWeb = (Button) getActivity().findViewById(R.id.buttonWeb);
        ProgressBar progressBar = (ProgressBar) getActivity().findViewById(R.id.progressBar);
        toggleButton = (ToggleButton) getActivity().findViewById(R.id.toggleButton);

        Picasso.with(image.getContext()).load(recipeDetails.getImageUrl())
                .resize(1420, 720).centerCrop().into(image);
        textTitle.setText(recipeDetails.getTitle());
        textPublisher.setText(recipeDetails.getPublisher());
        textRating.setText("Rating: " + (recipeDetails.getSocialRank()).toString());
        Log.d("rating", recipeDetails.getSocialRank().toString());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_1, recipeDetails.getIngredients());
        listIngredients.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        buttonWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(recipeDetails.getSourceUrl()));
                startActivity(i);
            }
        });
        toggleButton.setChecked(recipeDetailsPresenter.searchOnDB());
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.d("but", "true");
                    recipeDetailsPresenter.insertOnDB();
                }
                else {
                    Log.d("but","false");
                    recipeDetailsPresenter.deleteFromDB();
                }
            }
        });
        listIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                shoppingCart = recipeDetailsPresenter.loadShoppingCart();
                shoppingCart.add(recipeDetails.getIngredients().get(position));
                recipeDetailsPresenter.saveShoppingCart(shoppingCart);
            }
        });
    }

    @Override
    public void onDestroy() {
        recipeDetailsPresenter.closeDB();
        super.onDestroy();
    }
}

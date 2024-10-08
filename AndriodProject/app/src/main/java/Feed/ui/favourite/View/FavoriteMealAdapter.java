package Feed.ui.favourite.View;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.sidechefproject.R;

import java.util.ArrayList;
import java.util.List;

import Feed.ui.calendar.View.onMealPlanningClick;
import Feed.ui.search.tablayout.View.CateogiresFragment.CategoriesAdapter;
import Feed.ui.search.tablayout.View.onMealClickListener;
import Model.Category;
import Model.Meal;

public class FavoriteMealAdapter extends RecyclerView.Adapter<FavoriteMealAdapter.MealViewHolder> {

    private List<Meal> meals;
    private Context context;
    private onMealClickListener.onMealClickListenerFavourite listner;
    private onClickRemoveFavourite removeListner;
    private onMealPlanningClick addMealtoPlan;

    public FavoriteMealAdapter(List<Meal> meals, Context context , onMealClickListener.onMealClickListenerFavourite listner,onClickRemoveFavourite removeListner,onMealPlanningClick addMealtoPlan) {
        this.context = context;
        this.listner=listner;
        this.removeListner=removeListner;
        this.addMealtoPlan=addMealtoPlan;
        if(null != meals)
        {
          meals = new ArrayList<>(meals.size());
          this.meals=meals;
        }
        else
        {
            this.meals = new ArrayList<>(1);
            Meal noMeal = new Meal();
            noMeal.setStrMeal("No Meal Found");
            this.meals.add(noMeal);
        }
    }

    @NonNull
    @Override
    public MealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater cusInflater = LayoutInflater.from(parent.getContext());
        View tempV=cusInflater.inflate(R.layout.container_list_card_withplan, parent,false);
        FavoriteMealAdapter.MealViewHolder tempHolder= new  FavoriteMealAdapter.MealViewHolder(tempV);
        return tempHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MealViewHolder holder, int position) {
// Set the image resource
        holder.iconImage.setImageResource(R.drawable.delete);

// Convert dp to pixels
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics());
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, context.getResources().getDisplayMetrics());

// Set the layout params specific to LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        holder.iconImage.setLayoutParams(params);
        holder.mealName.setText(meals.get(position).getStrMeal());
        Glide.with(this.context).load(meals.get(position).getStrMealThumb())
                .apply(new RequestOptions().override(80,80)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground))
                .into(holder.mealImage);

        // Handle click event to navigate to meal details
        holder.mealImage.setOnClickListener(v -> {
            listner.onMealFavoutriteClick(meals.get(position));

        });

        holder.iconImage.setOnClickListener(v -> {
            removeListner.onFavMealRemove(meals.get(position));
        });
        holder.schedualeImage.setOnClickListener(v -> {
                addMealtoPlan.onMealScheduleClicked(meals.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return meals.size();
    }
    public void updateMeals(List<Meal> newMeals) {
        meals.clear();
        meals.addAll(newMeals);
    }
    public static class MealViewHolder extends RecyclerView.ViewHolder {
        ImageView mealImage;
        ImageView iconImage;
        ImageView schedualeImage;
        TextView mealName;

        public MealViewHolder(@NonNull View itemView) {
            super(itemView);
            mealImage = itemView.findViewById(R.id.meal_picture);
            iconImage = itemView.findViewById(R.id.favIcon);
            mealName = itemView.findViewById(R.id.meal_name);
            schedualeImage=itemView.findViewById(R.id.schedualeIcon);
        }
    }
}


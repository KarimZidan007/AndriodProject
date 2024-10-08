package Feed.ui.search.tablayout.View.SearchFragment;


import static Feed.ui.favourite.Controller.FavoriteManager.isFavorite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;


import java.util.ArrayList;
import java.util.List;

import Feed.ui.calendar.View.onMealPlanningClick;
import Feed.ui.favourite.Controller.FavoriteManager;
import Feed.ui.favourite.View.onClickRemoveFavourite;
import Feed.ui.search.tablayout.View.onAddFavMealClickListner;
import Feed.ui.search.tablayout.View.onMealClickListener;
import Model.Meal;
import com.example.sidechefproject.R;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>
{
    private final Context context;
    private  List<Meal> values ;
    private  final String TAG="FirstLRecyclerView";
    private onMealClickListener.onMealClickSearchListener listener;
    private onAddFavMealClickListner addFavMealListner;
    private onMealPlanningClick addMealtoPlan;
    private onClickRemoveFavourite removeListner;

    public SearchAdapter(Context context, List<Meal> values , onMealClickListener.onMealClickSearchListener listener,onAddFavMealClickListner addFavMealListner, onClickRemoveFavourite removeListner, onMealPlanningClick addMealtoPlan) {
        this.context = context;
        this.addFavMealListner=addFavMealListner;
        this.addMealtoPlan=addMealtoPlan;
        this.listener=listener;
        this.removeListner=removeListner;

        if(null != values)
        {
            this.values = new ArrayList<>(values.size());
            this.values = values;
        }
        else
        {
            this.values = new ArrayList<>();
            Meal noMeal = new Meal();
            noMeal.setStrMeal("No Meal Found");
            noMeal.setStrMealThumb("https://png.pngtree.com/png-vector/20210221/ourmid/pngtree-error-404-not-found-neon-effect-png-image_2928214.jpg");
            this.values.add(noMeal);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageV;
        private View layoutView;
        private TextView mealNameText;
        private ImageView iconImage;
        private ImageView schedualeIcon;
        private boolean isFav=false;

        public ViewHolder(View layoutView) {
            super(layoutView);
            this.layoutView = layoutView;
            imageV=layoutView.findViewById(R.id.meal_picture);
            mealNameText=layoutView.findViewById(R.id.meal_name);
            iconImage = itemView.findViewById(R.id.favIcon);
            schedualeIcon=itemView.findViewById(R.id.schedualeIcon);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater cusInflater = LayoutInflater.from(parent.getContext());
        View tempV=cusInflater.inflate(R.layout.container_list_card_withplan, parent,false);
        ViewHolder tempHolder= new ViewHolder(tempV);

        return tempHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if(!isFavorite((values.get(position).getIdMeal())))
        {
            holder.isFav=false;
        }
        else
        {
            holder.iconImage.setImageResource(R.drawable.ic_favorite_filled);
            holder.isFav=true;
        }
        holder.mealNameText.setText(values.get(position).getStrMeal());
        Glide.with(this.context).load(values.get(position).getStrMealThumb())
                .apply(new RequestOptions().override(350,313)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_foreground))
                .into(holder.imageV);
        holder.imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onMealClick(values.get(position));
            }
        });
        holder.iconImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!holder.isFav)
                {
                    addFavMealListner.onFavMealAdd(values.get(position));
                    holder.iconImage.setImageResource(R.drawable.ic_favorite_filled);
                    holder.isFav=true;
                    FavoriteManager.toggleFavorite(values.get(position));
                }
                else
                {
                    removeListner.onFavMealRemove(values.get(position));
                    holder.iconImage.setImageResource(R.drawable.fav);
                    holder.isFav=false;
                    FavoriteManager.toggleFavorite(values.get(position));
                }
            }
        });
        holder.schedualeIcon.setOnClickListener(v -> {
            addMealtoPlan.onMealScheduleClicked(values.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}




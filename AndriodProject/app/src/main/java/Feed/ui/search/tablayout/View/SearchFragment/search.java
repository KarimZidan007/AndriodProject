package Feed.ui.search.tablayout.View.SearchFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.sidechefproject.MealDetails.MealDetailsActivity;
import com.example.sidechefproject.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import DataBase.Model.AppDataBase;
import DataBase.Model.calAppDataBase;
import DataBase.Model.localSrcImplementation;
import DataBase.controller.MealDAO;
import DataBase.controller.MealDateDao;
import Feed.Controllers.InsertingDBPresenter.addFavMealPresenter;
import Feed.Controllers.searchFragPresenter;
import Feed.ui.calendar.Controller.CalendarPresenter;
import Feed.ui.favourite.Controller.FavMealPresenter;
import Feed.ui.favourite.Controller.FavoriteManager;
import Feed.ui.favourite.View.FavouriteFragment;
import Feed.ui.favourite.View.onClickRemoveFavourite;
import Feed.ui.search.IsearchMealView;
import Feed.ui.calendar.View.onMealPlanningClick;
import Feed.ui.search.tablayout.View.CountriesFragment.country;
import Feed.ui.search.tablayout.View.IngredientsFragment.ingreident;
import Feed.ui.search.tablayout.View.onAddFavMealClickListner;
import Feed.ui.search.tablayout.View.onMealClickListener;
import Model.Meal;
import Model.MealDate;
import Network.Model.MealsRemoteDataSource;
import Repository.DataSrcRepository;

public class search extends Fragment  implements IsearchMealView.IsearchAllViewsMeals, onMealClickListener.onMealClickSearchListener, onMealPlanningClick, onClickRemoveFavourite, onAddFavMealClickListner {
    private SearchView search;
    private RecyclerView recView;
    private MealsRemoteDataSource  searchSrc ;
    private searchFragPresenter searchMealPresenter;
    private static SearchAdapter searchAdapter;
    private MealDateDao mealDateDao;
    private calAppDataBase plannedDbObj;
    private List<Meal> searchedMeals=new ArrayList<>();
    private FavMealPresenter presenter;
    private AppDataBase dataBaseObj;
    private MealDAO dao;
    private DataSrcRepository repo;
    private addFavMealPresenter favMealPresenter;
    private localSrcImplementation localSrc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search2, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(searchAdapter != null)
             searchAdapter.notifyDataSetChanged();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        search=view.findViewById(R.id.searchBar);
        recView=view.findViewById(R.id.searchRec);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new GridLayoutManager(getContext(),1));
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchSrc= MealsRemoteDataSource.getRemoteSrcClient();
                repo = new DataSrcRepository(searchSrc,null);
                searchMealPresenter = new searchFragPresenter(repo,  search.this);
                searchMealPresenter.getMealByNameRemotly(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 1) {
                    searchSrc= MealsRemoteDataSource.getRemoteSrcClient();
                    repo = new DataSrcRepository(searchSrc,null);
                    searchMealPresenter = new searchFragPresenter(repo,search.this);
                    char firstCharacter = newText.charAt(0);
                    searchMealPresenter.getMealByFirstCharRemotly(firstCharacter);
                }
                else
                {
                    List<Meal> toRemove = new ArrayList<>();

                    for (int x = 1; x < newText.length(); x++) {
                        for (int i = 0; i < searchedMeals.size(); i++) {
                            if (x >= searchedMeals.get(i).getStrMeal().length() ||
                                    newText.charAt(x) != searchedMeals.get(i).getStrMeal().charAt(x)) {
                                searchedMeals.remove(i);
                            }
                        }
                    }
                    displayFirstLMeals(searchedMeals);
                }
                return false;
            }
        });
    }
    @Override
    public void displayFirstLMeals(List<Meal> meals) {
        searchedMeals=meals;
        searchAdapter = new SearchAdapter(search.this.getContext(),meals,search.this,this,this,this);
        recView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();

    }
    @Override
    public void displayError(String errorMsg) {
        Toast.makeText(search.this.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void displayMealsByName(List<Meal> meals) {
        searchAdapter = new SearchAdapter(search.this.getContext(),meals,this,this,this,this);
        recView.setAdapter(searchAdapter);
        searchAdapter.notifyDataSetChanged();
    }
    @Override
    public void displayErrorByName(String errorMsg) {
        Toast.makeText(search.this.getContext(), errorMsg, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onMealClick(Meal meal) {
        Intent mealDetailsIntent = new Intent(this.getContext(),MealDetailsActivity.class);
        mealDetailsIntent.putExtra("MEAL",meal);
        startActivity(mealDetailsIntent);
    }
    @Override
    public void onMealScheduleClicked(Meal meal) {
        // Get the current date and time
        Calendar calendar = Calendar.getInstance();

        // Trigger a date picker dialog restricted to future dates
        DatePickerDialog datePickerDialog = new DatePickerDialog(search.this.getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Format the selected date
                    //String selectedDate = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                    String selectedDate = String.format("%04d-%02d-%02d", year, monthOfYear+1 , dayOfMonth);

                    // After selecting the date, show the time picker dialog restricted to future times
                    TimePickerDialog timePickerDialog = new TimePickerDialog(search.this.getContext(),
                            (timeView, hourOfDay, minute) -> {
                                // Format the selected time
                                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);

                                // Combine date and time

                                // Insert the meal with the selected date and time into the database
                                saveMealToDate(meal, selectedDate , selectedTime);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true); // Use 24-hour format

                    // Ensure the time picker shows future times if the selected date is today
                    if (isToday(year, monthOfYear, dayOfMonth)) {
                        timePickerDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                    }

                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        // Set the minimum date to today (restrict past dates)
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        datePickerDialog.show();
    }
    private boolean isToday(int year, int month, int day) {
        Calendar today = Calendar.getInstance();
        return year == today.get(Calendar.YEAR) &&
                month == today.get(Calendar.MONTH) &&
                day == today.get(Calendar.DAY_OF_MONTH);
    }
    private void saveMealToDate(Meal meal, String date, String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.parse(date, formatter);

        // Get the day of the week from LocalDate
        String dayOfWeek = localDate.getDayOfWeek().toString(); // e.g., "SUNDAY", "MONDAY"
        String formattedDayOfWeek = dayOfWeek.substring(0, 1).toUpperCase() + dayOfWeek.substring(1).toLowerCase(); // e.g., "Sunday"

        MealDate mealDate = new MealDate(meal, date, time,formattedDayOfWeek);
        plannedDbObj = calAppDataBase.getDbInstance(search.this.getContext());
        mealDateDao = plannedDbObj.getDateMealsDao();
        localSrcImplementation plannedLocalSrc=new localSrcImplementation (mealDateDao,null,null);
        repo =new DataSrcRepository(null,plannedLocalSrc) ;
        CalendarPresenter calendarPresenter = new CalendarPresenter(repo);
        repo.insertPlannedMeal(mealDate);
        Toast.makeText(search.this.getContext(), "Meal scheduled for " + date + " at " + time, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onFavMealRemove(Meal meal) {
        dataBaseObj = AppDataBase.getDbInstance(search.this.getContext());
        dao = dataBaseObj.getMealsDao();
        localSrc =new localSrcImplementation(null,dao,null);
        repo =new DataSrcRepository(null,localSrc);
        presenter = new FavMealPresenter(repo);
        presenter.deleteMeal(meal);
    }
    @Override
    public void onFavMealAdd(Meal meal) {
        dataBaseObj = AppDataBase.getDbInstance(search.this.getContext());
        dao = dataBaseObj.getMealsDao();
        localSrc =new localSrcImplementation(null,dao,null);
        repo = new DataSrcRepository(null,localSrc);
        favMealPresenter = new addFavMealPresenter(repo);
        favMealPresenter.insertFavMeal(meal);
    }
    public static void notifyDataChanged() {
        if (searchAdapter != null) {
            searchAdapter.notifyDataSetChanged();
        }
    }
}
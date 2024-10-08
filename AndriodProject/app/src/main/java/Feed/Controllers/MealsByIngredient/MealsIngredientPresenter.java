package Feed.Controllers.MealsByIngredient;

import java.util.List;

import Feed.Controllers.MealsIPresenter;
import Feed.ui.search.IsearchMealView;
import Model.Country;
import Model.Ingredient;
import Model.Meal;
import Network.Model.MealsRemoteDataSource;
import Network.Model.NetworkCallback.NetworkCallback;
import Repository.DataSrcRepository;

public class MealsIngredientPresenter implements NetworkCallback.NetworkCallbackGetIngredients,NetworkCallback.NetworkCallbackFilterByIngredient, MealsIPresenter.getIngredientsPresenterContract, MealsIPresenter.getMealsFilterdByIngredient {
    private DataSrcRepository searchRepo;
    private IsearchMealView.IgetMealIngredientsView iViewOne;
    private IsearchMealView.IgetMealFilterIngredientsView iViewTwo;

    public MealsIngredientPresenter(DataSrcRepository remoteSrc, IsearchMealView.IgetMealIngredientsView iViewOne)
    {
        this.iViewOne=iViewOne;
        this.searchRepo=remoteSrc;
    }
    public MealsIngredientPresenter(DataSrcRepository remoteSrc , IsearchMealView.IgetMealFilterIngredientsView iViewTwo)
    {
        this.iViewTwo=iViewTwo;
        this.searchRepo=remoteSrc;
    }
    /***********************************************************************************/

    //Ask for Meals Ingredients
    @Override
    public void reqMealsIngredients() {
        searchRepo.getMealsIngredients(this);
    }
    @Override
    public void onSuccessResultGetIngredients(List<Ingredient> Ingredients) {
        iViewOne.displayMealsIngredients(Ingredients);
    }

    @Override
    public void onFailureResultGetIngredients(String errorMsg) {
        iViewTwo.displayFilterErrorByIngredients(errorMsg);
    }
    //Ask for Meals Ingredients


/***********************************************************************************/
    //List By Ingredient

    @Override
    public void reqFilteringByIngredient(String ingredient) {
        searchRepo.FilterMealsByIngredient(ingredient,this);
    }

    @Override
    public void onSuccessResultFilterByIngredients(List<Meal> meals) {
        iViewTwo.displayFilterMealsIngredients(meals);
    }

    @Override
    public void onFailureResultFilterByIngredients(String errorMsg) {
        iViewTwo.displayFilterErrorByIngredients(errorMsg);
    }
}

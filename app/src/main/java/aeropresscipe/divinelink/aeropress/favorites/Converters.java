package aeropresscipe.divinelink.aeropress.favorites;

import aeropresscipe.divinelink.aeropress.recipe.models.Recipe;
import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class Converters {
  @TypeConverter
  public String fromOptionValuesList(Recipe recipe) {
    if (recipe == null) {
      return (null);
    }
    Gson gson = new Gson();
    Type type = new TypeToken<Recipe>() {
    }.getType();
    return gson.toJson(recipe, type);
  }

  @TypeConverter
  public Recipe toOptionValuesList(String optionValuesString) {
    if (optionValuesString == null) {
      return (null);
    }
    Gson gson = new Gson();
    Type type = new TypeToken<Recipe>() {
    }.getType();
    return gson.fromJson(optionValuesString, type);
  }
}

package aeropresscipe.divinelink.aeropress.savedrecipes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import aeropresscipe.divinelink.aeropress.generaterecipe.DiceDomain;
import androidx.room.TypeConverter;

public class Converters {
    @TypeConverter
    public String fromOptionValuesList(DiceDomain recipe) {
        if (recipe == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<DiceDomain>() {
        }.getType();
        String json = gson.toJson(recipe, type);
        return json;
    }

    @TypeConverter
    public DiceDomain toOptionValuesList(String optionValuesString) {
        if (optionValuesString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<DiceDomain>() {
        }.getType();
        DiceDomain productCategoriesList = gson.fromJson(optionValuesString, type);
        return productCategoriesList;
    }

}


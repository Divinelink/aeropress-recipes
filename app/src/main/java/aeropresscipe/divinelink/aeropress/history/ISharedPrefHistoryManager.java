package aeropresscipe.divinelink.aeropress.history;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public interface ISharedPrefHistoryManager {

    void setIsHistoryEmptyBool(Context ctx, boolean bool);
    
}

package aeropresscipe.divinelink.aeropress.util;

import androidx.annotation.StyleRes;

import aeropresscipe.divinelink.aeropress.R;

public class DynamicNoActionBarTheme extends DynamicTheme {
    protected @StyleRes
    int getTheme() {
        return R.style.Base_Theme_Aeropress;
    }
}

package aeropresscipe.divinelink.aeropress.test.util.fakes

import aeropresscipe.divinelink.aeropress.base.keyvalue.Theme
import aeropresscipe.divinelink.aeropress.ui.theme.ThemedActivityDelegate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeThemedActivityDelegate(
    override val theme: StateFlow<Theme> = MutableStateFlow(Theme.SYSTEM),
    override val currentTheme: Theme = Theme.SYSTEM,
) : ThemedActivityDelegate

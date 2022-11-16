package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean

/**
 * A sealed class defining all possible states of our Bean Tracker screen.
 */
sealed class BeanTrackerViewState(
    val showLoading: Boolean = true,
) {
    /**
     * The initial state of the screen with nothing input.
     */
    object Initial : BeanTrackerViewState()

    /**
     * The state of the screen as the application is fetching bean data from any repo.
     */
    object Active : BeanTrackerViewState()

    /**
     * The state of the screen as data are fetched and ready to be showed on the screen.
     * @property [isEmpty] A boolean that tells us whether the beans list is empty.
     * @property [beans] A list that holds all the beans related to the user.
     */
    data class Completed(
        val isEmpty: Boolean,
        val beans: List<Bean>
    ) : BeanTrackerViewState(
        showLoading = false
    )
}

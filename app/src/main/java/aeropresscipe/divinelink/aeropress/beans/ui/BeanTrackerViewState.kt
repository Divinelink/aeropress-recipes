package aeropresscipe.divinelink.aeropress.beans.ui

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.GroupedCoffeeBeans

/**
 * A sealed class defining all possible states of our Bean Tracker screen.
 */
sealed class BeanTrackerViewState(val showLoading: Boolean = true) {
  /**
   * The initial state of the screen with nothing input.
   */
  data object Initial : BeanTrackerViewState()

  /**
   * The state of the screen as the application is fetching bean data from any repo.
   */
  data class Active(val addBean: Boolean = false) : BeanTrackerViewState(showLoading = false)

  /**
   * The state of the screen as data are fetched and ready to be showed on the screen.
   * @property [coffeeBeans] A list that holds all the beans related to the user.
   * @property [goToAddBean] A boolean that tells us whether to navigate to AddBean screen or not..
   */
  data class Completed(
    val coffeeBeans: GroupedCoffeeBeans,
    val goToAddBean: Boolean = false,
    val selectedBean: Bean? = null,
  ) : BeanTrackerViewState(
    showLoading = false,
  )
}

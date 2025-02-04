package aeropresscipe.divinelink.aeropress

import aeropresscipe.divinelink.aeropress.addbeans.domain.usecase.trimFields
import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class MethodsTest {

  @Test
  fun `trim name successfully removes trailing spaces`() {
    val bean = Bean(
      id = "",
      name = "name with trailing spaces           ",
      roasterName = "roaster name    ",
      origin = "origin     ",
      roastDate = null,
      roastLevel = null,
      process = null,
      rating = 0,
      tastingNotes = "",
      additionalNotes = "",
    )

    assertThat(
      bean.trimFields(),
    ).isEqualTo(
      Bean(
        id = "",
        name = "name with trailing spaces",
        roasterName = "roaster name",
        origin = "origin",
        roastDate = null,
        roastLevel = null,
        process = null,
        rating = 0,
        tastingNotes = "",
        additionalNotes = "",
      ),
    )
  }
}

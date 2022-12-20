package aeropresscipe.divinelink.aeropress.models

import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.toProcessMethod
import com.google.common.truth.Truth.assertThat
import org.junit.Assert
import org.junit.Test
import kotlin.test.assertFailsWith

class ProcessMethodTest {

    @Test
    fun `Get Process Method from String`() {
        // Washed
        assertThat(
            "Washed".toProcessMethod()
        ).isEqualTo(
            ProcessMethod.Washed
        )

        // Natural
        assertThat(
            "Natural".toProcessMethod()
        ).isEqualTo(
            ProcessMethod.Natural
        )

        // Honey
        assertThat(
            "Honey".toProcessMethod()
        ).isEqualTo(
            ProcessMethod.Honey
        )

        // Anaerobic
        assertThat(
            "Anaerobic".toProcessMethod()
        ).isEqualTo(
            ProcessMethod.Anaerobic
        )

        // CarbonicMaceration
        assertThat(
            "CarbonicMaceration".toProcessMethod()
        ).isEqualTo(
            ProcessMethod.CarbonicMaceration
        )

        // GilingBasah
        assertThat(
            "GilingBasah".toProcessMethod()
        ).isEqualTo(
            ProcessMethod.GilingBasah
        )
    }

    @Test
    fun unknownProcessMethod() {
        assertFailsWith<IllegalArgumentException> { "Unknown".toProcessMethod() }
        Assert.assertThrows(IllegalArgumentException::class.java) { "Unknown".toProcessMethod() }

        val exception = assertFailsWith<IllegalArgumentException> { "Washed".toProcessMethod() }
        assertThat(
            exception
        ).isNotEqualTo(
            IllegalArgumentException::class
        )
    }
}

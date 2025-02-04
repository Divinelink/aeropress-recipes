package com.divinelink.aerorecipe.sample.model

import aeropresscipe.divinelink.aeropress.beans.domain.model.Bean
import aeropresscipe.divinelink.aeropress.beans.domain.model.ProcessMethod
import aeropresscipe.divinelink.aeropress.beans.domain.model.RoastLevel
import java.time.LocalDate

object BeanSample {

  fun empty() = Bean(
    id = "",
    name = "",
    roasterName = "",
    origin = "",
    roastDate = null,
    roastLevel = null,
    process = null,
    rating = 0,
    tastingNotes = "",
    additionalNotes = "",
    timestamp = "",
  )

  fun ethiopia() = Bean(
    id = "ddaf6a73-e334-4c30-9344-5c220dbd9d1d",
    name = "Ethiopia",
    roasterName = "Cup10",
    origin = "Ethiopia",
    roastDate = LocalDate.of(2021, 10, 1),
    roastLevel = RoastLevel.Medium,
    process = ProcessMethod.Natural,
    rating = 5,
    tastingNotes = "Fruity, floral, sweet",
    additionalNotes = "",
    timestamp = "1633084800",
  )

  fun colombia() = Bean(
    id = "ddaf6a73-e334-4c30-9344-5c220dbd9d2f",
    name = "Colombia",
    roasterName = "Cup10",
    origin = "Colombia",
    roastDate = LocalDate.of(2021, 10, 1),
    roastLevel = RoastLevel.Medium,
    process = ProcessMethod.Washed,
    rating = 4,
    tastingNotes = "Fruity, floral, sweet",
    additionalNotes = "",
    timestamp = "1714846566",
  )

  fun brazil() = Bean(
    id = "ddaf6a73-e334-4c30-9344-5c220dbd9d3f",
    name = "Brazil",
    roasterName = "Cup10",
    origin = "Brazil",
    roastDate = LocalDate.of(2021, 10, 1),
    roastLevel = RoastLevel.Medium,
    process = ProcessMethod.Natural,
    rating = 3,
    tastingNotes = "Fruity, floral, sweet",
    additionalNotes = "",
    timestamp = "1738692966",
  )

  fun all() = listOf(
    ethiopia(),
    colombia(),
    brazil(),
  )
}

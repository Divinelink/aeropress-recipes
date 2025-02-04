package com.divinelink.aerorecipe.sample.model

import aeropresscipe.divinelink.aeropress.beans.domain.model.GroupedCoffeeBeans

object GroupedCoffeeBeansSample {
  fun group() = GroupedCoffeeBeans(
    mapOf(
      "October 2021" to listOf(BeanSample.ethiopia()),
      "May 2024" to listOf(BeanSample.colombia()),
      "February 2025" to listOf(BeanSample.brazil()),
    ),
  )
}

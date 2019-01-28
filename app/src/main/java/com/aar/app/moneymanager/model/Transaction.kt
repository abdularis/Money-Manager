package com.aar.app.moneymanager.model

import java.math.BigDecimal
import java.util.*

data class Transaction (
        val id: Int,
        val categoryName: String,
        val categoryIconKey: String,
        val date: Date,
        val memo: String,
        val type: Type,
        val amount: BigDecimal
) {
    enum class Type {
        Expense, Income
    }
}
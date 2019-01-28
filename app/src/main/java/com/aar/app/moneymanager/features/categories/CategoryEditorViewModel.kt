package com.aar.app.moneymanager.features.categories

import android.arch.lifecycle.ViewModel
import com.aar.app.moneymanager.data.CategoryRepository
import com.aar.app.moneymanager.data.IconData
import com.aar.app.moneymanager.data.IconProvider
import com.aar.app.moneymanager.model.Category
import com.aar.app.moneymanager.model.Transaction

class CategoryEditorViewModel: ViewModel() {

    private val repo = CategoryRepository.instance

    var currentTransactionType = Transaction.Type.Expense
    var selectedIconData: IconData = IconProvider.DEFAULT_ICON

    fun saveCategory(name: String) {
        repo.saveCategory(currentTransactionType, Category(
                id = "",
                name = name,
                iconKey = selectedIconData.key
        ))
    }

}
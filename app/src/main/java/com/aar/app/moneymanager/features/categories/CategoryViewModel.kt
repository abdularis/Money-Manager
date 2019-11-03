package com.aar.app.moneymanager.features.categories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Log
import com.aar.app.moneymanager.data.CategoryRepository
import com.aar.app.moneymanager.model.Category
import com.aar.app.moneymanager.model.Transaction
import com.google.firebase.firestore.CollectionReference

class CategoryViewModel : ViewModel() {

    sealed class State {
        class Loading : State()
        class Loaded(
                val data: List<Category>
        ) : State()
    }

    private val repo = CategoryRepository.instance
    private var categoryCollection: CollectionReference? = null
    private var currentTransactionType = Transaction.Type.Expense

    val categoryState = MutableLiveData<State>()

    fun load(type: Transaction.Type) {
        categoryState.value = State.Loading()
        currentTransactionType = type
        categoryCollection = if (type == Transaction.Type.Expense) {
            repo.getExpenseCategories()
        } else {
            repo.getIncomeCategories()
        }

        categoryCollection?.addSnapshotListener { querySnapshot, _ ->
            val list = querySnapshot?.map {
                Category(
                        id = it.id,
                        name = it.getString("name")!!,
                        iconKey = it.getString("iconKey")!!
                )
            }
            categoryState.value = State.Loaded(list!!)
        }
    }

    fun deleteCategory(category: Category) {
        repo.deleteCategory(currentTransactionType, category)
    }

}
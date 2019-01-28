package com.aar.app.moneymanager.data

import android.util.Log
import com.aar.app.moneymanager.model.Category
import com.aar.app.moneymanager.model.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class CategoryRepository private constructor() {

    companion object {
        val instance: CategoryRepository by lazy { CategoryRepository() }
    }

    private val expenses: CollectionReference?
    private val incomes: CollectionReference?

    init {
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val userDoc = FirebaseFirestore.getInstance().collection("users").document(user.uid)
            expenses = userDoc.collection("cat_expenses")
            incomes = userDoc.collection("cat_incomes")
        } else {
            expenses = null
            incomes = null
        }
    }

    fun saveCategory(transactionType: Transaction.Type, category: Category) {
        if (transactionType == Transaction.Type.Expense) {
            expenses?.add(category)
        } else if (transactionType == Transaction.Type.Income) {
            incomes?.add(category)
        }
    }

    fun deleteCategory(transactionType: Transaction.Type, category: Category) {
        if (transactionType == Transaction.Type.Expense) {
            expenses?.document(category.id)?.delete()
        } else if (transactionType == Transaction.Type.Income) {
            incomes?.document(category.id)?.delete()
        }
    }

    fun getExpenseCategories() = expenses

    fun getIncomeCategories() = incomes

//    fun saveCategory(transactionType: Transaction.Type, category: Category) {
//        dataRef?.child(getChildName(transactionType))?.push()?.setValue(category)
//                ?.addOnCompleteListener {
//                    Log.v("SimpanData", "Completed!!")
//                }
//                ?.addOnCanceledListener {
//                    Log.v("SimpanData", "Cancelled")
//                }
//                ?.addOnFailureListener {
//                    Log.v("SimpanData", "Failure $it")
//                }
//                ?.addOnSuccessListener {
//                    Log.v("SimpanData", "Success")
//                }
//    }
//
//    fun deleteCategory(transactionType: Transaction.Type, category: Category) {
//        dataRef?.child(getChildName(transactionType))?.child(category.id)?.removeValue()
//    }
//
//    fun queryExpenseCategories() = dataRef?.child(getChildName(Transaction.Type.Expense))
//
//    fun queryIncomeCategories() = dataRef?.child(getChildName(Transaction.Type.Income))
//
//    private fun getChildName(transactionType: Transaction.Type) =
//            if (transactionType == Transaction.Type.Expense) "cat_expenses" else "cat_incomes"
}
package com.aar.app.moneymanager.features.categories

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.aar.app.moneymanager.R
import com.aar.app.moneymanager.common.easyadapter.MultiTypeAdapter
import com.aar.app.moneymanager.common.easyadapter.SimpleAdapterDelegate
import com.aar.app.moneymanager.data.IconProvider
import com.aar.app.moneymanager.model.Category
import com.aar.app.moneymanager.model.Transaction
import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.partial_toolbar.*

class CategoryActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY_TYPE = "cat_type"
    }

    private val adapter = MultiTypeAdapter()
    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter.addDelegate(
                Category::class.java,
                R.layout.item_category,
                object: SimpleAdapterDelegate.Binder<Category> {
                    override fun bind(model: Category, holder: SimpleAdapterDelegate.ViewHolder) {
                        holder.find<TextView>(R.id.textName).text = model.name
                        holder.find<ImageView>(R.id.icon).setImageResource(IconProvider.findIconId(model.iconKey))
                        holder.find<View>(R.id.btnDelete).setOnClickListener {
                            viewModel.deleteCategory(model)
                        }
                    }
                },
                object: SimpleAdapterDelegate.OnItemClickListener<Category> {
                    override fun onClick(model: Category, view: View) {

                    }
                }
        )
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        val transactionType = getTransactionType()

        viewModel = ViewModelProviders.of(this).get(CategoryViewModel::class.java)
        viewModel.categoryState.observe(this, Observer<CategoryViewModel.State> { state ->
            state?.let {
                if (state is CategoryViewModel.State.Loading) {
                    progressBar.visibility = View.VISIBLE
                } else if (state is CategoryViewModel.State.Loaded) {
                    progressBar.visibility = View.GONE
                    if (state.data.isEmpty()) {
                        textEmpty.visibility = View.VISIBLE
                    } else {
                        textEmpty.visibility = View.GONE
                    }
                    adapter.setItems(state.data)
                }
            }
        })
        viewModel.load(transactionType)

        supportActionBar?.title = if (transactionType == Transaction.Type.Expense)
            "Expense Category" else "Income Category"
    }

    fun onAddCategoryClick(view: View) {
        val intent = Intent(this, CategoryEditorActivity::class.java)
        intent.putExtra(CategoryEditorActivity.EXTRA_CATEGORY_TYPE, getTransactionType())
        startActivity(intent)
    }

    private fun getTransactionType() : Transaction.Type {
        val extras = intent.extras
        if (extras != null && extras.containsKey(EXTRA_CATEGORY_TYPE)) {
            return extras[EXTRA_CATEGORY_TYPE] as Transaction.Type
        }
        return Transaction.Type.Expense
    }
}

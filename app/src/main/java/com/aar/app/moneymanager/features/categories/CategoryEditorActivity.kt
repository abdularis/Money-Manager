package com.aar.app.moneymanager.features.categories

import androidx.lifecycle.ViewModelProviders
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import calcNumOfColumns
import com.aar.app.moneymanager.R
import com.aar.app.moneymanager.data.IconData
import com.aar.app.moneymanager.data.IconProvider
import com.aar.app.moneymanager.common.easyadapter.MultiTypeAdapter
import com.aar.app.moneymanager.common.easyadapter.SimpleAdapterDelegate
import com.aar.app.moneymanager.model.Transaction
import kotlinx.android.synthetic.main.activity_category_editor.*
import kotlinx.android.synthetic.main.partial_toolbar.*
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap


class CategoryEditorActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_CATEGORY_TYPE = "cat_type"
    }

    private lateinit var viewModel: CategoryEditorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_editor)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val adapter = MultiTypeAdapter()
        adapter.addDelegate(
                IconData::class.java,
                R.layout.item_category_icon,
                object: SimpleAdapterDelegate.Binder<IconData> {
                    override fun bind(model: IconData, holder: SimpleAdapterDelegate.ViewHolder) {
                        holder.find<ImageView>(R.id.icon).setImageResource(model.id)
                    }
                },
                object: SimpleAdapterDelegate.OnItemClickListener<IconData> {
                    override fun onClick(model: IconData, view: View) {
                        iconSelected.setImageResource(model.id)
                        iconSelected.startAnimation(AnimationUtils.loadAnimation(this@CategoryEditorActivity, R.anim.zoom_in_out))
                        viewModel.selectedIconData = model
                    }
                }
        )
        adapter.addDelegate(
                String::class.java,
                R.layout.item_category_header,
                object: SimpleAdapterDelegate.Binder<String> {
                    override fun bind(model: String, holder: SimpleAdapterDelegate.ViewHolder) {
                        holder.find<TextView>(R.id.textHeader).text = model
                    }
                },
                object: SimpleAdapterDelegate.OnItemClickListener<String> {
                    override fun onClick(model: String, view: View) {

                    }
                }
        )

        val data = ArrayList<Any>()
        data.addAll(IconProvider.icons)
//        data.add(5, "Food")
//        data.add(15, "Office")
//        data.add(20, "Entertainment")
//        data.add(26, "Other")

        val numColumn = calcNumOfColumns(this, 68)
        recyclerView.adapter = adapter
        val layoutManager = FlexboxLayoutManager(this)
        layoutManager.justifyContent = JustifyContent.SPACE_AROUND
        recyclerView.layoutManager = layoutManager
        adapter.setItems(data)

        viewModel = ViewModelProviders.of(this).get(CategoryEditorViewModel::class.java)
        viewModel.currentTransactionType = getTransactionType()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_category_editor, menu)
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.menu_save) {
            viewModel.saveCategory(editTextName.text.toString())
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getTransactionType() : Transaction.Type {
        val extras = intent.extras
        if (extras != null && extras.containsKey(CategoryActivity.EXTRA_CATEGORY_TYPE)) {
            return extras[CategoryActivity.EXTRA_CATEGORY_TYPE] as Transaction.Type
        }
        return Transaction.Type.Expense
    }
}

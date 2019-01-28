import android.content.Context

fun calcNumOfColumns(context: Context, gridWidth: Int): Int {
    val dispMetrics = context.resources.displayMetrics
    val dpWidth = dispMetrics.widthPixels / dispMetrics.density
    return (dpWidth / gridWidth).toInt()
}
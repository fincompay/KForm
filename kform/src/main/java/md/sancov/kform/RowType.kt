package md.sancov.kform

import android.os.Parcelable

interface RowType: Parcelable {
    val key: String get() {
        return order.toString()
    }

    val order: Int
}
package md.sancov.kform.row

import android.os.Parcelable

interface ValueRow<Params, Model: Parcelable>: Row {
    val params: Params
    val model: Model?
}
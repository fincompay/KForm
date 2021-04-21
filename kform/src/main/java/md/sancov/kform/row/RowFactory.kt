package md.sancov.kform.row

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import md.sancov.kform.RowType

interface RowFactory<out Row : ValueRow<Params, Model>, Params, Model: Parcelable> {
    fun <Type : RowType> create(
        type: Type,
        params: Params,
        flow: MutableStateFlow<Model?>,
    ): Row
}
package md.fincompay.kform.row

import kotlinx.coroutines.flow.MutableStateFlow
import md.fincompay.kform.RowType

interface RowFactory<out Row : ValueRow<Params, Model>, Params, Model> {
    fun <Type : RowType> create(
        type: Type,
        params: Params,
        flow: MutableStateFlow<Model?>,
    ): Row
}
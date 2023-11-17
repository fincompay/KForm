package md.fincompay.kform.row

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import md.fincompay.kform.RowType
import md.fincompay.utils.model.Img
import md.fincompay.utils.model.Text

data class PickerRow(
    override val type: RowType,
    override val params: Params,
    override val model: Model?,
) : ValueRow<PickerRow.Params, PickerRow.Model>, RowClickable {

    interface Model : Parcelable {
        val id: String
        val img: Img? get() = null
        val title: Text? get() = null
        val subtitle: Text? get() = null
    }

    data class Params(
        val caption: Text? = null,
        val hint: Text? = null,
        val placeholderImg: Img? = null
    )
    object Factory : RowFactory<PickerRow, Params, Model> {
        override fun <Type : RowType> create(
            type: Type,
            params: Params,
            flow: MutableStateFlow<Model?>
        ): PickerRow {
            return PickerRow(type, params, flow.value)
        }
    }
}
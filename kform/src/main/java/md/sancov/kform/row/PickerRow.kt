package md.sancov.kform.row

import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import md.sancov.kform.RowType
import md.sancov.kform.model.Img
import md.sancov.kform.model.Text

data class PickerRow(
    override val type: RowType,
    override val params: Params,
    override val flow: MutableStateFlow<Model?>
): ValueRow<PickerRow.Params, PickerRow.Model>, RowClickable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PickerRow) return false

        if (type != other.type) return false
        if (params != other.params) return false
        if (flow.value != other.flow.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + params.hashCode()
        result = 31 * result + flow.value.hashCode()
        return result
    }

    interface Model: Parcelable {
        val id: String
        val img: Img? get() = null
        val title: Text? get() = null
        val subtitle: Text? get() = null
    }

    data class Params(
        val caption: Text? = null,
        val hint: Text? = null
    )

    object Factory: RowFactory<PickerRow, Params, Model> {
        override fun <Type : RowType> create(
            type: Type,
            params: Params,
            flow: MutableStateFlow<Model?>
        ): PickerRow {
            return PickerRow(type, params, flow)
        }
    }
}
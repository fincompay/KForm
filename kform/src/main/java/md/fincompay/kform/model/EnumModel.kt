package md.fincompay.kform.model

import kotlinx.parcelize.Parcelize
import md.fincompay.kform.row.PickerRow
import md.fincompay.utils.model.Text

@Parcelize
data class EnumModel(
        override val id: String,
        override val title: Text,
        override val subtitle: Text? = null
) : PickerRow.Model

inline fun <reified T : Enum<T>> PickerRow.Model.toEnum(): T {
    return enumValueOf(id)
}

package md.fincompay.kform.row

import kotlinx.coroutines.flow.MutableStateFlow
import md.fincompay.kform.RowType
import md.fincompay.utils.model.Text

data class CheckboxRow(
    override val type: RowType,
    override val params: Params,
    override val model: Boolean?,
    private val flow: MutableStateFlow<Boolean?>
) : ValueRow<CheckboxRow.Params, Boolean> {

    fun set(value: Boolean) {
        flow.value = value
    }

    data class Params(
        val text: Text? = null,
        val interactive: Boolean = true,
        val textIsClickable: Boolean = false,
    )

    object Factory : RowFactory<CheckboxRow, Params, Boolean> {
        override fun <Type : RowType> create(
            type: Type,
            params: Params,
            flow: MutableStateFlow<Boolean?>
        ): CheckboxRow {
            return CheckboxRow(type, params, flow.value, flow)
        }
    }
}
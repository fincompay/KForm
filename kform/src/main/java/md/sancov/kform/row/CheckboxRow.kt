package md.sancov.kform.row

import kotlinx.coroutines.flow.MutableStateFlow
import md.sancov.kform.RowType
import md.sancov.utils.model.Text

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
        var text: Text? = null,
        var textIsClickable: Boolean = false,
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
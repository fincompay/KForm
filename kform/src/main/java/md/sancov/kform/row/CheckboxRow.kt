package md.sancov.kform.row

import kotlinx.coroutines.flow.MutableStateFlow
import md.sancov.kform.RowType
import md.sancov.kform.model.Text

data class CheckboxRow(
    override val type: RowType,
    override val params: Params,
    override val model: Boolean?,
    val flow: MutableStateFlow<Boolean?>
): ValueRow<CheckboxRow.Params, Boolean> {

    data class Params(
        var text: Text? = null,
        var textIsClickable: Boolean = false,
    )

    object Factory: RowFactory<CheckboxRow, Params, Boolean> {
        override fun <Type : RowType> create(
            type: Type,
            params: Params,
            flow: MutableStateFlow<Boolean?>
        ): CheckboxRow {
            return CheckboxRow(type, params, flow.value, flow)
        }
    }
}
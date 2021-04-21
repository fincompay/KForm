package md.sancov.kform.row

import kotlinx.coroutines.flow.MutableStateFlow
import md.sancov.kform.RowType
import md.sancov.kform.model.BoolModel
import md.sancov.utils.model.Text

data class CheckboxRow(
    override val type: RowType,
    override val params: Params,
    override val model: BoolModel?,
    private val flow: MutableStateFlow<BoolModel?>
) : ValueRow<CheckboxRow.Params, BoolModel> {

    fun set(value: Boolean) {
        flow.value = BoolModel(value)
    }

    data class Params(
        var text: Text? = null,
        var textIsClickable: Boolean = false,
    )

    object Factory : RowFactory<CheckboxRow, Params, BoolModel> {
        override fun <Type : RowType> create(
            type: Type,
            params: Params,
            flow: MutableStateFlow<BoolModel?>
        ): CheckboxRow {
            return CheckboxRow(type, params, flow.value, flow)
        }
    }
}
package md.sancov.kform.row

import android.content.Context
import android.os.Parcelable
import kotlinx.coroutines.flow.MutableStateFlow
import md.sancov.kform.RowType
import md.sancov.utils.format.text.TextContent
import md.sancov.utils.format.text.TextFormat
import md.sancov.utils.model.Text

data class TextRow<Model>(
    override val type: RowType,
    override val params: Params<Model>,
    override val model: Model?,
    private val flow: MutableStateFlow<Model?>
) : ValueRow<TextRow.Params<Model>, Model> {

    data class Params<Model>(
        val format: TextFormat<Model>?,
        val content: TextContent = TextContent.raw,
        val caption: Text? = null,
        val hint: Text? = null,
        val enabled: Boolean = true,
        val editable: Boolean = true,
        val animateFocus: Boolean = true,
        var suffix: String? = null
    )

    fun setText(text: String?, ctx: Context) {
        flow.value = params.format?.resolveValue(ctx, text)
    }

    fun text(ctx: Context): String? {
        return params.format?.resolveString(ctx, flow.value)
    }

    class Factory<Model> : RowFactory<TextRow<Model>, Params<Model>, Model> {
        override fun <Type : RowType> create(
            type: Type,
            params: Params<Model>,
            flow: MutableStateFlow<Model?>
        ): TextRow<Model> {
            return TextRow(type, params, flow.value, flow)
        }
    }
}

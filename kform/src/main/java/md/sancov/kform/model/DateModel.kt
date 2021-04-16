package md.sancov.kform.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.row.PickerRow
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class DateModel(val date: Date) : PickerRow.Model {
    @IgnoredOnParcel
    val formatter = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override val id: String
        get() = formatter.format(date)

    override val title: Text
        get() = Text.Characters(id)
}


fun Date.asModel(): DateModel {
    return DateModel(this)
}
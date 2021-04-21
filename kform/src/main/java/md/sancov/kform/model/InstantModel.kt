package md.sancov.kform.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.row.PickerRow
import md.sancov.utils.model.Text
import java.time.Instant
import java.time.format.DateTimeFormatter

@Parcelize
data class InstantModel(val date: Instant, val pattern: String = "dd.MM.yyyy") : PickerRow.Model {
    @IgnoredOnParcel
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)

    override val id: String get() {
        return formatter.format(date)
    }

    override val title: Text
        get() = Text.Chars(id)
}


fun Instant.asModel(): InstantModel {
    return InstantModel(this)
}
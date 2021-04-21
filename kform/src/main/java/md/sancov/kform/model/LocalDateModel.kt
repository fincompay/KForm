package md.sancov.kform.model

import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.row.PickerRow
import md.sancov.utils.model.Text
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Parcelize
data class LocalDateModel(val date: LocalDate) : PickerRow.Model {
    @IgnoredOnParcel
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    override val id: String get() {
        return formatter.format(date)
    }

    override val title: Text
        get() = Text.Chars(id)
}


fun LocalDate.asModel(): LocalDateModel {
    return LocalDateModel(this)
}
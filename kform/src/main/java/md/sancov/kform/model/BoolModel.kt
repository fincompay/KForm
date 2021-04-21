package md.sancov.kform.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BoolModel(val value: Boolean): Parcelable

val Boolean.asModel: BoolModel get() {
    return BoolModel(this)
}
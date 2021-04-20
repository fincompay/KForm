package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.Adapter
import md.sancov.kform.RowType
import md.sancov.kform.Store
import md.sancov.kform.binder.KeyBinder
import md.sancov.kform.row.CheckboxRow
import md.sancov.kform.row.PickerRow
import md.sancov.kform.vm.FormViewModel
import javax.inject.Inject

@Parcelize
enum class MainRow: RowType {
    LastName, FirstName;

    override val order: Int get() {
        return ordinal
    }
}

sealed class DynamicRow: RowType {
    @Parcelize
    data class Short(override val order: Int) : DynamicRow()

    @Parcelize
    data class Detailed(override val order: Int) : DynamicRow()
}

class MainAdapter(state: SavedStateHandle): Adapter<MainRow, KeyBinder<MainRow>>(state, KeyBinder()) {
    override fun setup(binder: KeyBinder<MainRow>) {

    }
}

@HiltViewModel
class MainViewModel @Inject constructor(state: SavedStateHandle) : FormViewModel<MainRow>() {
    init {
        set(MainAdapter(state)) {

        }

    }
}
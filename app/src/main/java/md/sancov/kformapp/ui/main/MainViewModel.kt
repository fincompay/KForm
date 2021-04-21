package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.RowType
import md.sancov.kform.TypeBinderAdapter
import md.sancov.kform.binder.GroupBinder
import md.sancov.kform.binder.TypeBinder
import md.sancov.kform.row.CheckboxRow
import md.sancov.kform.vm.FormViewModel
import javax.inject.Inject

@Parcelize
enum class MainRow: RowType {
    LastName, FirstName;

    override val order: Int get() {
        return ordinal
    }
}

class MainAdapter: TypeBinderAdapter<MainRow>() {
    override fun bindings(binder: TypeBinder<MainRow>) {
        binder.bind(CheckboxRow.Factory, MainRow.FirstName, MainRow.LastName) { _, _ ->
            CheckboxRow.Params()
        }
    }

}

@HiltViewModel
class MainViewModel @Inject constructor(state: SavedStateHandle) : FormViewModel<MainRow>(state) {
    init {
        set(MainAdapter())
    }
}
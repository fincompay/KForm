package md.fincompay.kformapp.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import md.fincompay.kform.RowType
import md.fincompay.kform.TypeBinderAdapter
import md.fincompay.kform.binder.TypeBinder
import md.fincompay.kform.row.CheckboxRow
import md.fincompay.kform.vm.FormViewModel
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
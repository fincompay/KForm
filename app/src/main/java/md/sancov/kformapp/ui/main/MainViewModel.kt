package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.FormAdapter
import md.sancov.kform.RowType
import md.sancov.kform.binder.KeyBinder
import md.sancov.kform.row.CheckboxRow
import md.sancov.kform.vm.FormViewModel
import javax.inject.Inject

@Parcelize
enum class MainRow: RowType {
    LastName, FirstName;

    override val id: Int get() {
        return ordinal
    }
}

class MainAdapter(state: SavedStateHandle): FormAdapter<MainRow>(state) {
    init {
        types {
            MainRow.values()
        }

        triggers {
            subscribe(MainRow.LastName, MainRow.FirstName) { _, _ ->
            }
        }


        binder(KeyBinder()) {
            bind(CheckboxRow.Factory, MainRow::class) { _, _ ->
                CheckboxRow.Params()
            }
        }
    }
}

@HiltViewModel
class MainViewModel @Inject constructor(state: SavedStateHandle) : FormViewModel<MainRow>() {
    init {
        set(MainAdapter(state)) {

        }

    }
}
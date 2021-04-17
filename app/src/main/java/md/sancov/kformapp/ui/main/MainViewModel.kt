package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.Parcelize
import md.sancov.kform.FormDataSource
import md.sancov.kform.RowType
import md.sancov.kform.binder.KeyBinder
import md.sancov.kform.row.CheckboxRow
import md.sancov.kform.vm.FormViewModel

@Parcelize
enum class MainRow: RowType {
    LastName, FirstName;

    override val id: Int get() {
        return ordinal
    }
}

class MainDataSource(state: SavedStateHandle): FormDataSource<MainRow>(state) {
    init {
        types {
            MainRow.values().toList()
        }

        triggers {
            subscribe(MainRow.LastName, MainRow.FirstName) { _, _ ->
            }
        }


        binder(KeyBinder()) {
            bind(MainRow::class, CheckboxRow.Factory) { _, _ ->
                CheckboxRow.Params()
            }
        }
    }
}

class MainViewModel(state: SavedStateHandle) : FormViewModel<MainRow>() {
    init {
        set(MainDataSource(state)) {

        }

    }
}
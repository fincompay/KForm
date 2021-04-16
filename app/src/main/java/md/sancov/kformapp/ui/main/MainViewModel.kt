package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.Parcelize
import md.sancov.kform.DataSource
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

class MainDataSource(state: SavedStateHandle): DataSource<MainRow>(state) {
    init {
        listeners {
            subscribe(MainRow.FirstName) {
            }
        }

        triggers {
            subscribe(MainRow.LastName, MainRow.FirstName) {
            }
        }


        binder(KeyBinder()) {
            bind(MainRow::class, CheckboxRow.Factory) { _, _ ->
                CheckboxRow.Params()
            }
        }
    }

    override suspend fun types(): Iterable<MainRow> {
        return MainRow.values().toList()
    }

}

class MainViewModel(state: SavedStateHandle) : FormViewModel<MainRow>() {
    init {
        set(MainDataSource(state)) {

        }

    }
}
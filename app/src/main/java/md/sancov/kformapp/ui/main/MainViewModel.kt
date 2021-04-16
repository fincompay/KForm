package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import md.sancov.kform.DataSource
import md.sancov.kform.RowType
import md.sancov.kform.vm.FormViewModel

enum class MainRow: RowType {

}

class MainDataSource: DataSource<MainRow> {

}

class MainViewModel(state: SavedStateHandle) : FormViewModel<MainRow>(state) {

    init {
        reload(MainDataSource())
    }
}
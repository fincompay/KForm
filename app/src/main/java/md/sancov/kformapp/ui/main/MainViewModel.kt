package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import kotlinx.parcelize.Parcelize
import md.sancov.kform.DataSource
import md.sancov.kform.RowType
import md.sancov.kform.vm.FormViewModel

@Parcelize
enum class MainRow: RowType {
    LastName, FirstName;

    override val id: Int get() {
        return ordinal
    }
}

//class MainDataSource: DataSource<MainRow> {
//
//}

class MainViewModel(state: SavedStateHandle) : FormViewModel<MainRow>() {
    init {
//        val source = DataSource<MainRow>(state) {
//            registry {
//                register(*MainRow.values())
//            }
//        }

//        setDataSource(MainDataSource())
    }
}
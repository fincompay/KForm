package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.parcelize.Parcelize
import md.sancov.kform.RowType
import md.sancov.kform.vm.FormViewModel
import javax.inject.Inject

@Parcelize
enum class MainRow: RowType {
    LastName, FirstName;

    override val order: Int get() {
        return ordinal
    }
}

//class MainAdapter(state: SavedStateHandle): FormAdapter<MainRow, GroupBinder<MainRow>>(state, GroupBinder()) {
//    override fun setup(binder: GroupBinder<MainRow>) {
//
//    }
//}

@HiltViewModel
class MainViewModel @Inject constructor(state: SavedStateHandle) : FormViewModel<MainRow>(state) {
    init {
    }
}
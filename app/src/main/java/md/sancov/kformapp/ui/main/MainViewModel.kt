package md.sancov.kformapp.ui.main

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import md.sancov.kform.FormAdapter
import md.sancov.kform.RowType
import md.sancov.kform.binder.KeyBinder
import md.sancov.kform.row.CheckboxRow
import md.sancov.kform.row.PickerRow
import md.sancov.kform.vm.FormViewModel
import javax.inject.Inject

enum class MainRow: RowType {
    LastName, FirstName;

    override val id: Int get() {
        return ordinal
    }
}

sealed class DynamicRow: RowType {
    data class Short(override val id: Int) : DynamicRow()
    data class Detailed(override val id: Int) : DynamicRow()
}

class MainAdapter(state: SavedStateHandle): FormAdapter<MainRow>(state) {
    init {
        prepare {
//            val types = mutableListOf<DynamicRow>()
//
//            for (i in 1..10) {
//                val type = if (i % 2 == 0) {
//                    DynamicRow.Short(i)
//                } else {
//                    DynamicRow.Detailed(i)
//                }
//                types.add(type)
//            }
//            register(*types.toTypedArray())
        }


        binder(KeyBinder()) {
//            bind(CheckboxRow.Factory, DynamicRow.Short::class) { _, _ ->
//                CheckboxRow.Params()
//            }
//
//            bind(PickerRow.Factory, DynamicRow.Detailed::class) { _, _ ->
//                PickerRow.Params()
//            }
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
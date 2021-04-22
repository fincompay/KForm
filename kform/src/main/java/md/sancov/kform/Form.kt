package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

class Form<Type: RowType>(state: SavedStateHandle) {
    val store: Store<Type> = Store(state)

    private val reload = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    private lateinit var adapter: Adapter<Type>

    @OptIn(ExperimentalCoroutinesApi::class)
    val rows: Flow<RowsState> = reload
        .filterNotNull()
        .transform {
            emit(State.Loading<List<Row>>())

            adapter.prepare(store)

            val triggers = listOf(refresh.map { }, adapter.collectors(store).merge()).merge()

            val rows = triggers.map {
                State.Success(adapter.resolve(store))
            }

            emitAll(rows)
        }
        .catch {
            emit(State.Error(it))
        }

    fun replaceAdapter(source: Adapter<Type>) {
        this.adapter = source
        reload()
    }

    fun refresh() {
        refresh.value = Date()
    }

    fun reload(keepState: Boolean = true) {
        if (!keepState) {
            store.clear()
        }
        reload.value = Date()
    }

    fun clear() {
        reload(false)
    }
}
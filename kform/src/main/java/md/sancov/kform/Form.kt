package md.sancov.kform

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

class Form<Type : RowType> {
    private val reload = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    private lateinit var adapter: FormAdapter<Type>

    @OptIn(ExperimentalCoroutinesApi::class)
    val items: Flow<RowsState> = reload
        .filterNotNull()
        .transform {
            emit(State.Loading<List<Row>>())

            adapter.prepare.invoke()

            val store = adapter.store
            val binder = adapter.binder

            val rows = merge(refresh.map { }, adapter.triggers).map {
                val rows = adapter.types().map { binder.resolve(it, store) }

                State.Success(rows)
            }

            emitAll(rows)
        }
        .catch {
            emit(State.Error(it))
        }

    fun replaceAdapter(source: FormAdapter<Type>) {
        this.adapter = source
        reload()
    }

    fun refresh() {
        refresh.value = Date()
    }

    fun reload(keepState: Boolean = true) {
        if (!keepState) {
            adapter.store.clear()
        }

        reload.value = Date()
    }

    fun clear() {
        reload(false)
    }

    fun<Value> get(type: Type, default: Value): Value {
        return adapter.store.get(type, default)
    }

    operator fun<Value> get(type: Type): Value? {
        return adapter.store[type]
    }

    operator fun<Value> set(type: Type, data: Value?) {
        adapter.store[type] = data
    }
}
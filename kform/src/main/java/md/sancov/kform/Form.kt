package md.sancov.kform

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import md.sancov.kform.binder.Binder
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

interface FormAdapter {
    val triggers: Flow<Unit>

    suspend fun prepare() { }

    suspend fun resolve(): List<Row>

    fun clear()
}

class Form {
    private val reload = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    private lateinit var adapter: FormAdapter

    @OptIn(ExperimentalCoroutinesApi::class)
    val rows: Flow<RowsState> = reload
        .filterNotNull()
        .transform {
            emit(State.Loading<List<Row>>())

            adapter.prepare()

            val rows = merge(refresh.map { }, adapter.triggers).map { State.Success(adapter.resolve()) }

            emitAll(rows)
        }
        .catch {
            emit(State.Error(it))
        }

    fun replaceAdapter(source: FormAdapter) {
        this.adapter = source
        reload()
    }

    fun refresh() {
        Log.v("FORM", "REFRESH")

        refresh.value = Date()
    }

    fun reload(keepState: Boolean = true) {
        Log.v("FORM", "RELOAD")

        if (!keepState) {
            adapter.clear()
        }

        reload.value = Date()
    }

    fun clear() {
        reload(false)
    }

//    fun<Value> get(type: Type, default: Value): Value {
//        return adapter.store.get(type, default)
//    }
//
//    operator fun<Value> get(type: Type): Value? {
//        return adapter.store[type]
//    }
//
//    operator fun<Value> set(type: Type, data: Value?) {
//        adapter.store[type] = data
//    }
}
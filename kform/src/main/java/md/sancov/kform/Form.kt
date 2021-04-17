package md.sancov.kform

import android.util.Log
import kotlinx.coroutines.flow.*
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

class Form<T : RowType> {
    private val reset = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    private var source: FormDataSource<T>? = null

    val items: Flow<RowsState> = reset
        .filterNotNull()
        .mapNotNull { source }
        .transform { source ->
            Log.i("FORM","ACTION: RESET")

            Log.i("FORM","STATE: EMIT LOADING")

            emit(State.Loading<List<Row>>())

            Log.i("FORM","ACTION: ON PREPARE")

            source.prepare(this@Form)

            Log.i("FORM","ACTION: BINDINGS")

            val store = source.store
            val binder = source.binder ?: throw Throwable("Binder isn't specified")

            val rows = combine(listOfNotNull(source.triggers, refresh)) { _ ->
                Log.i("FORM","STATE: EMIT SUCCESS")

                val rows = source.types().map { binder.resolve(it, store) }

                State.Success(rows)
            }
//
//            val flows = combine(source.listeners ?: emptyFlow(), rows) { _, second ->
//                Log.i("FORM","STATE: LISTENERS AND TRIGGERS")
//
//                second
//            }

            emitAll(rows)
        }
        .catch {
            emit(State.Error(it))
        }

    fun setDataSource(source: FormDataSource<T>) {
        this.source = source

//        reset()
    }

    fun refresh() {
        refresh.value = Date()
    }

    fun reset() {
        reset.value = Date()
    }
}
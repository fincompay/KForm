package md.sancov.kform

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.*
import md.sancov.kform.row.Row
import md.sancov.utils.State
import java.util.*

typealias RowsState = State<List<Row>>

class Form<T : RowType>(state: SavedStateHandle) {
    private val reset = MutableStateFlow<Date?>(null)
    private val refresh = MutableStateFlow<Date?>(null)

    private var source: DataSource<T>? = null

    val rows: Flow<RowsState> = reset
            .filterNotNull()
            .mapNotNull { source }
            .transform { source ->
                Log.i("FORM","ACTION: RESET")

                Log.i("FORM","STATE: EMIT LOADING")
                emit(State.Loading<List<Row>>())

                //            emitAll(flows.filterNotNull().merge().map {
//                val rows = types.invoke(store).map { resolver.resolve(it, store) }
//                State.Success(rows)
//            })

                Log.i("FORM","STATE: EMIT SUCCESS")
                emit(State.Success(emptyList()))

//            emit(State.Loading())
//            store.reset()

//            source.de


                //
//            defaults?.invoke(store)
//
//            prepare?.invoke(store)

//            val flows = listOf(refresh)


            }
            .catch {
//                emit(State.Error(it))
            }

    fun setDataSource(source: DataSource<T>) {
        this.source = source
        reset()
    }

    fun refresh() {
        refresh.value = Date()
    }

    fun reset() {
        reset.value = Date()
    }
}
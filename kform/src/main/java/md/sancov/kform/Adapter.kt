package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import md.sancov.kform.binder.Binder
import md.sancov.kform.row.Row

abstract class Adapter<Type: RowType, B: Binder<Type>>(
        state: SavedStateHandle,
        private val binder: B,
): FormAdapter {
    private val store: Store<Type> = Store(state)

    override val triggers: Flow<Unit>
        get() = TODO("Not yet implemented")

    override suspend fun resolve(): List<Row> {
        return types(store).map {
            binder.resolve(it, store)
        }
    }

    override fun clear() {
        store.clear()
    }

    abstract fun setup(binder: B)

    open fun triggers(group: FlowGroup<Type>) {
    }

    open fun types(store: Store<Type>): List<Type> {
        return store.types.sortedBy { it.order }
    }
}

package md.sancov.kform

import kotlinx.coroutines.flow.Flow
import md.sancov.kform.binder.Binder
import md.sancov.kform.binder.GroupBinder
import md.sancov.kform.binder.TypeBinder
import md.sancov.kform.row.Row

interface Adapter<Type: RowType> {
    suspend fun prepare(store: Store<Type>) { }

    fun triggers(store: Store<Type>): List<Flow<Unit>> {
        return emptyList()
    }

    fun types(store: Store<Type>): List<Type> {
        return store.types.sortedBy { it.order }
    }

    fun resolve(store: Store<Type>): List<Row>
}

abstract class BinderAdapter<Type: RowType, B: Binder<Type>>(private val binder: B): Adapter<Type> {
    init {
        this.bindings(binder)
    }

    override fun resolve(store: Store<Type>): List<Row> {
        return types(store).map { binder.resolve(it, store) }
    }

    abstract fun bindings(binder: B)
}

abstract class GroupBinderAdapter<Type: RowType>: BinderAdapter<Type, GroupBinder<Type>>(GroupBinder())

abstract class TypeBinderAdapter<Type: RowType>: BinderAdapter<Type, TypeBinder<Type>>(TypeBinder())

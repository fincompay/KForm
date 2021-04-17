package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import md.sancov.kform.binder.Binder

typealias Lambda = suspend () -> Unit

open class FormDataSource<Type: RowType>(state: SavedStateHandle) {
    internal val store: Store<Type> = Store(state)
    internal var prepare: Lambda = { }

    internal lateinit var types: () -> Iterable<Type>
    internal lateinit var binder: Binder<Type>

    internal var triggers: Flow<Unit>? = null

    fun prepare(lambda: Store<Type>.() -> Unit) {
        this.prepare = { lambda(store) }
    }

    fun types(lambda: Registry<Type>.() -> Unit) {
        val registry = store.registry

        this.types = {
            registry.apply(lambda)
            registry.types
        }
    }

    fun triggers(lambda: Flows<Type>.() -> Unit) {
        this.triggers = Flows(store).apply(lambda).combine()
    }

    fun<T: Binder<Type>> binder(binder: T, lambda: T.() -> Unit) {
        this.binder = binder.also(lambda)
    }
}

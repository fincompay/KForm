package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import md.sancov.kform.binder.Binder

typealias Lambda = suspend () -> Unit

open class FormAdapter<Type: RowType>(state: SavedStateHandle) {
    internal val store: Store<Type> = Store(state)

    internal var prepare: Lambda = { }

    internal lateinit var types: () -> Array<Type>
    internal lateinit var binder: Binder<Type>

    internal var triggers: Flow<Unit> = emptyFlow()

    fun prepare(lambda: Store<Type>.() -> Unit) {
        this.prepare = { lambda(store) }
    }

    fun types(lambda: Store<Type>.() -> Array<Type>) {
        this.types = {
            val registry = store.registry

            val types = lambda(store)

            registry.clear()
            registry.register(*types)


            types
        }
    }

    fun triggers(lambda: Flows<Type>.() -> Unit) {
        this.triggers = Flows(store).apply(lambda).combine()
    }

    fun<T: Binder<Type>> binder(binder: T, lambda: T.() -> Unit) {
        this.binder = binder.also(lambda)
    }
}

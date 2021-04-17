package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import md.sancov.kform.binder.Binder

open class FormDataSource<Type: RowType>(state: SavedStateHandle) {
    internal val store: Store<Type> = Store(state)

    internal var listeners: Flow<Unit>? = null
    internal var triggers: Flow<Unit>? = null
    internal var binder: Binder<Type>? = null


    fun prepare(lambda: Store<Type>.() -> Unit) {
// this.prepare =
    }

    fun types(lambda: Registry<Type>.() -> Iterable<Type>) {
// this.types =
    }

    fun listeners(lambda: Flows<Type>.() -> Unit) {
        this.listeners = Flows(store).apply(lambda).combine()
    }

    fun triggers(lambda: Flows<Type>.() -> Unit) {
        this.triggers = Flows(store).apply(lambda).combine()
    }

    fun<T: Binder<Type>> binder(binder: T, lambda: T.() -> Unit) {
        this.binder = binder.also(lambda)
    }
}

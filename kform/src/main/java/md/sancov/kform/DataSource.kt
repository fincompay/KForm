package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.Flow
import md.sancov.kform.binder.Binder

abstract class DataSource<Type: RowType>(state: SavedStateHandle) {
    internal val store: Store<Type> = Store(state)

    internal var listeners: Flow<Unit>? = null
    internal var triggers: Flow<Unit>? = null
    internal var binder: Binder<Type>? = null

    abstract suspend fun types(): Iterable<Type>

    suspend fun prepare(form: Form<Type>) {}

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

//    @Throws
//    suspend fun onSetup(form: Form<Type>) { }
//
////    fun<B: Binder<Type>> setup(): B
//
////    fun setup(binder: )
//
////    fun setupTypes()
//
////    fun returnTypes()
//
////    fun defaults()
//
////    fun triggers()
//
////    fun listeners()
//
////    fun binder(): Binder<T>
//}
//
//class DataSource<Type: RowType>(state: SavedStateHandle, lambda: DataSource<Type>.() -> Unit) {
//    val store = Store<Type>(state)
//
//    lateinit var binder: Binder<Type>
//
//    init {
//        lambda(this)
//    }
//
//    fun registry(lambda: Registry<Type>.() -> Unit) {
//        store.registry.apply(lambda)
//    }
//
////    fun binder(lambda: B.() -> Unit) {
////////        lambda()
////    }
//
//}


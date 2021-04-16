package md.sancov.kform

import androidx.lifecycle.SavedStateHandle
import md.sancov.kform.binder.Binder

//interface DataSource<Type: RowType> {
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

class DataSource<Type: RowType>(state: SavedStateHandle, lambda: DataSource<Type>.() -> Unit) {
    val store = Store<Type>(state)

    lateinit var binder: Binder<Type>

    init {
        lambda(this)
    }

    fun registry(lambda: Registry<Type>.() -> Unit) {
        store.registry.apply(lambda)
    }

//    fun binder(lambda: B.() -> Unit) {
//////        lambda()
//    }

}


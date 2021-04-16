package md.sancov.kform.binder

import md.sancov.kform.RowType
import md.sancov.kform.Store
import md.sancov.kform.row.RowFactory
import md.sancov.kform.row.ValueRow

data class Binding<T: ValueRow<Params, Model>, Params, Model, Type: RowType>(
    val factory: RowFactory<T, Params, Model>,
    val params: (Type, Store<Type>) -> Params,
) {
    fun instantiate(type: Type, store: Store<Type>): T {
        val params = params(type, store)
        return factory.create(type, params, store.flow(type))
    }
}

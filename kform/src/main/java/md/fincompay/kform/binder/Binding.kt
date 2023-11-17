package md.fincompay.kform.binder

import md.fincompay.kform.RowType
import md.fincompay.kform.Store
import md.fincompay.kform.row.RowFactory
import md.fincompay.kform.row.ValueRow

data class Binding<T: ValueRow<Params, Model>, Params, Model, Type: RowType>(
    val factory: RowFactory<T, Params, Model>,
    val params: (Type, Store<Type>) -> Params,
) {
    fun instantiate(type: Type, store: Store<Type>): T {
        val params = params(type, store)
        return factory.create(type, params, store.flowByType(type))
    }
}

package md.sancov.kform.binder

import md.sancov.kform.RowType
import md.sancov.kform.Store
import md.sancov.kform.row.RowFactory
import md.sancov.kform.row.Row
import md.sancov.kform.row.ValueRow

class TypeBinder<Type: RowType>: Binder<Type> {
    private val bindings = mutableMapOf<Type, Binding<*, *, *, Type>>()

    fun<Row: ValueRow<Params, Model>, Params, Model> bind(
        vararg types: Type,
        factory: RowFactory<Row, Params, Model>,
        params: (Type, Store<Type>) -> Params,
    ) {
        val binding = Binding(factory, params)

        types.forEach {
            bindings[it] = binding
        }
    }

    override fun resolve(type: Type, store: Store<Type>): Row {
        val binding = bindings[type] ?: throw Throwable("No binding for type = $type")
        return binding.instantiate(type, store)
    }
}
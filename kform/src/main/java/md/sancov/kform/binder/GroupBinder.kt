package md.sancov.kform.binder

import android.os.Parcelable
import md.sancov.kform.RowType
import md.sancov.kform.Store
import md.sancov.kform.row.RowFactory
import md.sancov.kform.row.Row
import md.sancov.kform.row.ValueRow
import kotlin.reflect.KClass

class GroupBinder<Type: RowType>: Binder<Type> {
    private val bindings = mutableMapOf<String, Binding<*, *, *, Type>>()

    fun<R: ValueRow<Params, Model>, Params, Model> bind(
        factory: RowFactory<R, Params, Model>,
        clazz: KClass<out Type>,
        params: (Type, Store<Type>) -> Params
    ) {
        val key = clazz.qualifiedName ?: throw Throwable("Local or anonymous types are not allowed")
        bindings[key] = Binding(factory, params)
    }

    override fun resolve(type: Type, store: Store<Type>): Row {
        val key = type::class.qualifiedName ?: throw Throwable("Local or anonymous types are not allowed")

        val binding = bindings[key] ?: throw Throwable("No binding for key = $key")

        return binding.instantiate(type, store)
    }
}


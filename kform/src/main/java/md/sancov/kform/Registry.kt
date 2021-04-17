package md.sancov.kform

class Registry<Type: RowType> {

    val types = mutableSetOf<Type>()

    fun getById(id: Int): Type? {
        return types.firstOrNull { it.id == id }
    }

    fun register(vararg types: Type) {
        this.types.addAll(types)
    }

    fun clear() {
        types.clear()
    }
}
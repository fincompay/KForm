package md.fincompay.kform.binder

import md.fincompay.kform.RowType
import md.fincompay.kform.Store
import md.fincompay.kform.row.Row

interface Binder<Type: RowType> {
    fun resolve(type: Type, store: Store<Type>): Row
}
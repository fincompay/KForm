package md.fincompay.kform.row

import md.fincompay.kform.RowType

interface Row {
    val type: RowType

    override fun equals(other: Any?): Boolean
    override fun hashCode(): Int
}

interface RowClickable: Row
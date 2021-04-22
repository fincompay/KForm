package md.sancov.kform.row

interface ValueRow<Params, Model>: Row {
    val params: Params
    val model: Model?
}
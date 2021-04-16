package md.sancov.kform.row

import kotlinx.coroutines.flow.MutableStateFlow

interface ValueRow<Params, Model>: Row {
    val params: Params
    val flow: MutableStateFlow<Model?>
}
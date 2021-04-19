package md.sancov.kformapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import md.sancov.kformapp.R

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel by viewModels<MainViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val first = view.findViewById<AppCompatCheckBox>(R.id.checkbox_test_1)
        first.isChecked = viewModel.value(MainRow.FirstName, false)

        first.setOnCheckedChangeListener { _, isChecked ->
            Log.v("CHECK", "VALUE $isChecked")
            viewModel.set(MainRow.FirstName, isChecked)
        }

        val second = view.findViewById<AppCompatCheckBox>(R.id.checkbox_test_2)
        second.isChecked = viewModel.value(MainRow.LastName, false)


        second.setOnCheckedChangeListener { _, isChecked ->
            Log.v("CHECK", "VALUE $isChecked")

            viewModel.set(MainRow.LastName, isChecked)
        }

        viewModel.rows.asLiveData(Dispatchers.IO).observe(viewLifecycleOwner) {
            Log.i("MAIN", "ROWS = $it")
        }
    }
}
package br.com.dio.coinconverter.ui.history

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.dio.coinconverter.core.extensions.createDialog
import br.com.dio.coinconverter.core.extensions.createProgressDialog
import br.com.dio.coinconverter.databinding.ActivityHistoryBinding
import br.com.dio.coinconverter.presentation.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {
    private val adapter by lazy { HistoryAdapter() }
    private val viewModel by viewModel<HistoryViewModel>()
    private val binding by lazy { ActivityHistoryBinding.inflate(layoutInflater) }
    private val dialog by lazy { createProgressDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvHistory.adapter = adapter
        binding.rvHistory.addItemDecoration(DividerItemDecoration(applicationContext,DividerItemDecoration.HORIZONTAL))


        bindObserve()


        lifecycle.addObserver(viewModel)

    }

    private fun bindObserve() {
        viewModel.state.observe(this){
            when(it){
                HistoryViewModel.State.Loading -> dialog.show()
                is HistoryViewModel.State.Error ->{ dialog.dismiss()
                createDialog {
                    setMessage(it.throwable.message)}.show()
                }
                is HistoryViewModel.State.Success -> {
                    dialog.dismiss()
                    adapter.submitList(it.exchange)
                }
            }
        }
    }
}
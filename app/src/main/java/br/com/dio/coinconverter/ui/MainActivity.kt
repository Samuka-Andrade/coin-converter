package br.com.dio.coinconverter.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import br.com.dio.coinconverter.R
import br.com.dio.coinconverter.core.extensions.*
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.databinding.ActivityMainBinding
import br.com.dio.coinconverter.presentation.MainViewModel
import br.com.dio.coinconverter.ui.history.HistoryActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.lang.NullPointerException

class MainActivity : AppCompatActivity(),AdapterView.OnItemClickListener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel by viewModel<MainViewModel>()
    private val dialog by lazy {createProgressDialog()}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        bindAdapters()
        bindListeners()
        bindingObserve()

       setSupportActionBar(binding.toolbar)

    }

    private fun bindingObserve() {
        viewModel.state.observe(this) {
            when (it) {
                is MainViewModel.State.Error -> {
                    dialog.dismiss()
                    createDialog {
                        setMessage(it.throwable.message)
                    }.show()
                }
                MainViewModel.State.Loading -> dialog.show()
                is MainViewModel.State.Success -> {
                    success(it)
                }
                is MainViewModel.State.Saved ->{
                    dialog.dismiss()
                }
            }
        }
    }

    private fun success(it: MainViewModel.State.Success) {
        binding.btnSave.isEnabled = true
        dialog.dismiss()
        val selectedCoin = binding.textTo.text
       val coin = Coin.values().find { it.name == selectedCoin } ?: Coin.BRL
        var result = it.exchange.bid * binding.textValor.text.toDouble()
        binding.tvresult.text = result.formatCurrency(coin.locale)

    }

    private fun bindListeners() {
        binding.textValor.editText?.doAfterTextChanged {
            binding.btnConverter.isEnabled = it!=null && it.toString().isNotEmpty()
            binding.btnSave.isEnabled = false
        }
        binding.btnConverter.setOnClickListener{
            it.hideSoftKeyboard()
            val search = "${binding.tvFrom.text}-${binding.tvTo.text}"
            viewModel.getExchangedValue(search)

        }
        binding.btnSave.setOnClickListener {
          val value = viewModel.state.value
            (value as? MainViewModel.State.Success)?.let {
                viewModel.salveExchange(it.exchange.copy(bid = it.exchange.bid*binding.textValor.text.toDouble()))
            }
        }
    }

    private fun bindAdapters() {
        val list = Coin.values()
      var adapter =  ArrayAdapter(this,android.R.layout.simple_list_item_1,list)
        binding.tvFrom.setAdapter(adapter)
        binding.tvFrom.setText(Coin.BRL.name,false)
        //extra*
        var l : ArrayList<Coin> = ArrayList();
        list.toCollection(l)
        l.remove(Coin.BRL)
        var adap =  ArrayAdapter(this,android.R.layout.simple_list_item_1,l)
        binding.tvTo.setAdapter(adap)
        binding.tvTo.setText(Coin.USD.name,false)
        binding.tvFrom.setOnItemClickListener(this)


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_history){
            startActivity(Intent(applicationContext,HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var ls = Coin.values()
        var adapt =  ArrayAdapter(this,android.R.layout.simple_list_item_1, ls)
        binding.tvTo.setAdapter(adapt)
            val list = Coin.values()
            var l : ArrayList<Coin> = ArrayList();
            list.toCollection(l)
            l.removeAt(p2)
            var adap =  ArrayAdapter(this,android.R.layout.simple_list_item_1,l)
            binding.tvTo.setAdapter(adap)
            if (!l.contains(Coin.getByName(binding.tvTo.text.toString()))){
                binding.tvTo.setText("");
            }




    }

}
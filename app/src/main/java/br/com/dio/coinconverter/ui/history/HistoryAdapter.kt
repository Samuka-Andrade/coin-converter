package br.com.dio.coinconverter.ui.history

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.dio.coinconverter.R
import br.com.dio.coinconverter.core.extensions.formatCurrency
import br.com.dio.coinconverter.data.model.Coin
import br.com.dio.coinconverter.data.model.ExchangeResponseValue
import br.com.dio.coinconverter.databinding.ActivityMainBinding
import br.com.dio.coinconverter.databinding.ItemHistoryBinding

class HistoryAdapter : androidx.recyclerview.widget.ListAdapter<ExchangeResponseValue,HistoryAdapter.ViewHolder>(DiffCalback()) {
    inner class ViewHolder(private val binding: ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item : ExchangeResponseValue){
            binding.tvName.text = item.name
            var coin = Coin.getByName(item.codein)
            binding.tvValue.text = item.bid.formatCurrency(coin.locale)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHistoryBinding.inflate(inflater,parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DiffCalback : DiffUtil.ItemCallback<ExchangeResponseValue>() {
    override fun areItemsTheSame(
        oldItem: ExchangeResponseValue,
        newItem: ExchangeResponseValue
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: ExchangeResponseValue,
        newItem: ExchangeResponseValue
    ) = oldItem.id == newItem.id

}

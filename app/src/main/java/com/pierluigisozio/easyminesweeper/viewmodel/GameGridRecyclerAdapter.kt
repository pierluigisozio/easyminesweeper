package com.pierluigisozio.easyminesweeper.viewmodel

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pierluigisozio.easyminesweeper.R
import com.pierluigisozio.easyminesweeper.model.Cell
import com.pierluigisozio.easyminesweeper.model.CellValues

class GameGridRecyclerAdapter (
    private var cells : List<Cell>,
    val listener : OnCellClickListener
) : RecyclerView.Adapter<GameGridRecyclerAdapter.TileViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TileViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)
        return TileViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    override fun onBindViewHolder(holder: TileViewHolder, position: Int) {
        holder.bind(cells[position])
        holder.setIsRecyclable(false)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCells(cells : List<Cell>) {
        this.cells = cells
        notifyDataSetChanged()
    }


    inner class TileViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        private val valueTextView = itemView.findViewById<TextView>(R.id.item_cell_value)

        fun bind(cell: Cell) {
            itemView.setBackgroundColor(Color.GRAY)
            itemView.setOnClickListener { listener.cellClick(cell) }
            if (cell.isRevealed()) {
                when (cell.getValue()){
                    CellValues.BOMB -> valueTextView.setText(R.string.bomb)
                    CellValues.BLANK -> {
                        valueTextView.text = ""
                        itemView.setBackgroundColor(Color.WHITE)
                    }
                    else -> {
                        valueTextView.text = cell.getValue().toString()
                        when (cell.getValue()){
                            1 -> valueTextView.setTextColor(Color.BLUE)
                            2 -> valueTextView.setTextColor(Color.GREEN)
                            3 -> valueTextView.setTextColor(Color.RED)
                            4 -> valueTextView.setTextColor(-8388480) //Violet
                            5 -> valueTextView.setTextColor(-5952982) //Brown
                            6 -> valueTextView.setTextColor(-16724271) //Turquoise
                            7 -> valueTextView.setTextColor(Color.BLACK)
                        }
                    }
                }
            } else if (cell.isFlagged()){
                valueTextView.setText(R.string.flag)
            }
        }
    }


}
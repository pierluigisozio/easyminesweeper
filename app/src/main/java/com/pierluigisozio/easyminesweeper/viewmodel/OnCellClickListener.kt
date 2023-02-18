package com.pierluigisozio.easyminesweeper.viewmodel

import com.pierluigisozio.easyminesweeper.model.Cell

interface OnCellClickListener {
    fun cellClick (cell : Cell)
}
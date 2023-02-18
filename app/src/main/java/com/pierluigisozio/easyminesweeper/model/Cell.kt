package com.pierluigisozio.easyminesweeper.model

class Cell (private val value : Int, private var isRevealed : Boolean= false, private var isFlagged : Boolean = false){
    fun getValue(): Int {
        return value
    }
    fun isRevealed() :Boolean {
        return isRevealed
    }
    fun setRevealed(revealed :Boolean){
        isRevealed = revealed
    }
    fun isFlagged() : Boolean {
        return isFlagged
    }
    fun setFlagged(flagged: Boolean) {
        isFlagged = flagged
    }
}
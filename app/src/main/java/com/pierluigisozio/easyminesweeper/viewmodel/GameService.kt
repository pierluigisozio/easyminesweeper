package com.pierluigisozio.easyminesweeper.viewmodel

import com.pierluigisozio.easyminesweeper.model.Cell
import com.pierluigisozio.easyminesweeper.model.CellValues
import com.pierluigisozio.easyminesweeper.model.GameGrid

class GameService (size : Int, totalBombs : Int){

    private val gameGrid = GameGrid(size)
    private var gameOver = false
    private var flagMode = false
    private var clearMode = true
    private var flagCount = 0
    private var numberBombs = totalBombs
    private var timeExpired = false

    init {
        gameGrid.generateGrid(totalBombs)
    }

    fun handleCellClick(cell : Cell) {
        if(!gameOver && !isGameWon() && !timeExpired && !cell.isRevealed()){
            if(clearMode){
                clear(cell)
            } else if (flagMode){
                flag(cell)
            }
        }
    }

    private fun clear(cell: Cell){
        val index = gameGrid.getCells().indexOf(cell)
        gameGrid.getCells()[index].setRevealed(true)
        if(cell.getValue() == CellValues.BOMB) {
            gameOver = true
        } else if(cell.getValue() == CellValues.BLANK){
            val toClear = mutableSetOf<Cell>()
            val toCheckAdjacent = mutableListOf<Cell>()

            toCheckAdjacent.add(cell)

            while(toCheckAdjacent.size > 0){
                val cellToCheck = toCheckAdjacent[0]
                val cellIndex = gameGrid.getCells().indexOf(cellToCheck)
                val cellPos = gameGrid.toXY(cellIndex)
                for ( adjacent in gameGrid.adjacentCells(cellPos[0], cellPos[1]) ){
                    if (adjacent.getValue() == CellValues.BLANK) {
                        if(!toClear.contains(adjacent)){
                            if(!toCheckAdjacent.contains(adjacent)) {
                                toCheckAdjacent.add(adjacent)
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)){
                            toClear.add(adjacent)
                        }
                    }
                }
                toCheckAdjacent.remove(cellToCheck)
                toClear.add(cellToCheck)
            }
            toClear.forEach { cellToClear -> cellToClear.setRevealed(true) }
        }
    }

    private fun flag(cell: Cell){
        cell.setFlagged(!cell.isFlagged())
        var count = 0
        for (c in gameGrid.getCells()) {
            if(c.isFlagged()) count++
        }
        flagCount = count
    }

    fun isGameWon() : Boolean {
        var numbersUnrevealed = 0
        for (cell in gameGrid.getCells()){
            if (cell.getValue() != CellValues.BOMB && cell.getValue() != CellValues.BLANK && !cell.isRevealed()) {
                numbersUnrevealed++
            }
        }
        return numbersUnrevealed == 0
    }

    fun switchMode() {
        clearMode = !clearMode
        flagMode = !flagMode
    }

    fun outOfTime() { timeExpired = true}

    fun isGameOver() : Boolean { return gameOver}
    fun getGameGrid() : GameGrid { return gameGrid}
    fun isFlagMode() : Boolean { return flagMode}
    fun isClearMode() : Boolean { return clearMode}
    fun getFlagCount() : Int { return flagCount}
    fun getNumberBombs() : Int { return numberBombs }

}
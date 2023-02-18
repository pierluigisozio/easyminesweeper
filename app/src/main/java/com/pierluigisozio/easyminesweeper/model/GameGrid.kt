package com.pierluigisozio.easyminesweeper.model

import java.util.*
import kotlin.collections.ArrayList

class GameGrid (private val size: Int){

    private val cells : MutableList<Cell>
    init {
        cells = ArrayList()
        for (i in 0 until size*size){
            cells.add(Cell(CellValues.BLANK))
        }
    }

    fun generateGrid (totalBombs : Int){
        var bombsPlaced = 0
        val random = Random()
        while (bombsPlaced < totalBombs){
            val x : Int = random.nextInt(size)
            val y : Int = random.nextInt(size)
            if (cellAt(x, y)?.getValue() == CellValues.BLANK){
                cells[x + y*size] = Cell(CellValues.BOMB)
                bombsPlaced++
            }
        }

        for (x in 0 until size){
            for (y in 0 until size){
                if (cellAt(x,y)?.getValue() != CellValues.BOMB){
                    val adjacentCells = adjacentCells(x,y)
                    var countBombs = 0
                    for (cell in adjacentCells){
                        if (cell.getValue() == CellValues.BOMB) countBombs++
                    }
                    if (countBombs > 0) cells[x + y*size] = Cell(countBombs)
                }
            }
        }
    }

    fun cellAt  (x: Int, y: Int) : Cell?{
        if (x < 0 || x>= size || y<0 || y >= size ){
            return null
        }
        return cells[x + y*size]
    }

    fun adjacentCells (x:Int, y:Int) : List<Cell> {
        val adjacentCells = mutableListOf<Cell>()
        val cellsList = mutableListOf<Cell?>()

        cellsList.add(cellAt(x-1, y-1))
        cellsList.add(cellAt(x, y-1))
        cellsList.add(cellAt(x+1, y-1))
        cellsList.add(cellAt(x+1, y))
        cellsList.add(cellAt(x+1, y+1))
        cellsList.add(cellAt(x, y+1))
        cellsList.add(cellAt(x-1, y+1))
        cellsList.add(cellAt(x-1, y))

        cellsList.forEach { cell ->
            cell?.let { adjacentCells.add(it) }
        }

        return adjacentCells
    }

    fun toXY(index:Int) : IntArray {
        val y = index/ size
        val x = index - (y*size)
        return intArrayOf(x,y)
    }

    fun revealAllBombs() {
        for (cell in cells){
            if(cell.getValue() == CellValues.BOMB){
                cell.setRevealed(true)
            }
        }
    }

    fun getCells() : MutableList<Cell> {
        return this.cells
    }


}
package com.geishatokyo.sqlgen

import com.geishatokyo.sqlgen.core.{Cell, Row, Sheet, Workbook}


/**
 *
 * User: takeshita
 * Create: 12/07/11 21:58
 */

class SQLGenException(address: String, message:String, e : Throwable) extends Exception(address + " | " + message,e) {


}

object SQLGenException {

  def apply(message: String, t: Throwable = null) = {
    new SQLGenException("--", message, t)
  }

  def atCell(cell: Cell, message: String, t: Throwable = null) = {
    new SQLGenException(cell.address, message, t)
  }

  def atSheet(sheet: Sheet, message: String, t: Throwable = null) = {
    new SQLGenException(sheet.address, message, t)
  }


  def atWorkbook(wb: Workbook, message: String, t: Throwable = null) = {
    new SQLGenException(wb.name, message, t)
  }


  def atRow(row: Row, message: String, t: Throwable = null) = {
    new SQLGenException(row.address, message, t)
  }

}

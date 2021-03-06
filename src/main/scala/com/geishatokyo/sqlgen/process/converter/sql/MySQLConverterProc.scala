package com.geishatokyo.sqlgen.process.converter.sql

import com.geishatokyo.sqlgen.generator.sql.{QueryType, SQLQueryGenerator}
import com.geishatokyo.sqlgen.generator.sql.mysql.MySQLQueryGenerator
import com.geishatokyo.sqlgen.logger.Logger
import com.geishatokyo.sqlgen.meta.Metadata
import com.geishatokyo.sqlgen.process.{Context, Key}

/**
 *
 * User: takeshita
 * Create: 12/07/11 23:48
 */

class MySQLConverterProc(val queryType: QueryType) extends SQLConverterProc {


  override def dbType: String = "mysql"

  override def metadataKey: Key[Metadata] = MySQLConverterProc.MetadataKey

  override def forType(queryType: QueryType): SQLConverterProc = {
    new MySQLConverterProc(queryType)
  }

  override def createSqlQueryGenerator(c: Context): SQLQueryGenerator = {
    new MySQLQueryGenerator(false)
  }
}


object MySQLConverterProc {
  val MetadataKey = Key[Metadata]("metadata.sql.mysql")
}
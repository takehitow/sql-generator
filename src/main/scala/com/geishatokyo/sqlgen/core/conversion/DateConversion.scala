package com.geishatokyo.sqlgen.core.conversion

import java.time._
import java.time.format.{DateTimeFormatter, ResolverStyle}
import java.time.temporal.ChronoField

import com.geishatokyo.sqlgen.SQLGenException

import scala.concurrent.duration.Duration
import scala.util.Try
/**
  * Created by takezoux2 on 2017/05/27.
  */
trait DateConversion {

  def dateToDouble(d: ZonedDateTime): Double
  def dateToString(d: ZonedDateTime): String
  def stringToDate(s: String): ZonedDateTime
  def doubleToDate(d: Double): ZonedDateTime
  def longToDate(l: Long): ZonedDateTime
  def dateToLong(d: ZonedDateTime): Long


  def stringToDuration(s: String): Duration

}

trait UnixTimeBaseConversion extends DateConversion{
  override def dateToDouble(d: ZonedDateTime): Double = {
    d.toInstant.toEpochMilli
  }



  override def doubleToDate(d: Double): ZonedDateTime = {
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(d.toLong), ZoneId.systemDefault())
  }

  override def longToDate(l: Long): ZonedDateTime = {
    ZonedDateTime.ofInstant(Instant.ofEpochMilli(l), ZoneId.systemDefault())
  }

  override def dateToLong(d: ZonedDateTime): Long =  {
    d.toInstant.toEpochMilli
  }
}

trait DurationConversion extends DateConversion {

  import scala.concurrent.duration._

  override def stringToDuration(s: String) = {
    s.split(" ") match{
      case Array(n, "msec") => n.toLong.millis
      case Array(n, "sec") => n.toLong.seconds
      case Array(n, "min") => n.toLong.minutes
      case Array(n, "minute") => n.toLong.minutes
      case Array(n, "minutes") => n.toLong.minutes
      case Array(n, "hour") => n.toLong.hours
      case Array(n, "hours") => n.toLong.hours
      case Array(n, "day") => n.toLong.days
      case Array(n, "days") => n.toLong.days
      case Array(n, "week") => (n.toLong * 7).days
      case Array(n, "weeks") => (n.toLong * 7).days
    }
  }
}

trait VariousStringFormatConversion extends DateConversion{

  val formats = Array(
    DateTimeFormatter.ofPattern("yyyy/M/d H:m:s"),
    DateTimeFormatter.ofPattern("yyyy/M/d H:m:s.S"),
    DateTimeFormatter.ofPattern("yyyy-M-d H:m:s"),
    DateTimeFormatter.ofPattern("yyyy-M-d H:m:s.S"),
    DateTimeFormatter.ISO_OFFSET_DATE_TIME,
    DateTimeFormatter.BASIC_ISO_DATE,
    DateTimeFormatter.ofPattern("yyyy/M/d"),
    DateTimeFormatter.ofPattern("yyyy-M-d")
  )


  override def dateToString(d: ZonedDateTime): String = {
    formats.head.format(LocalDateTime.ofInstant(d.toInstant, ZoneId.systemDefault()))
  }

  override def stringToDate(s: String): ZonedDateTime = {
    formats.view.map(f => {
      try{
        val p = f.parse(s)
        if(p.isSupported(ChronoField.HOUR_OF_DAY)) {
          Some(ZonedDateTime.of(LocalDateTime.from(p), ZoneId.systemDefault()))
        } else {
          Some(ZonedDateTime.of(
            LocalDate.from(p),
            LocalTime.MIN,
            ZoneId.systemDefault()
          ))
        }
      }catch{
        case t: Throwable => {
          None
        }
      }
    }).find(_.isDefined).map(_.get).getOrElse{
      throw new Exception(s"Wrong date format ${s}")
    }
  }
}

object DefaultDateConversion extends DateConversion
  with VariousStringFormatConversion
  with UnixTimeBaseConversion
  with DurationConversion
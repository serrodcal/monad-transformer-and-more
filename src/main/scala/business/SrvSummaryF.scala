package com.logicaalternativa.monadtransformerandmore
package business

import java.util.Optional

import monad._
import bean._
import service._
import monad.syntax.Implicits._

trait SrvSummaryF[E,P[_]] {
  
  implicit val E : Monad[E,P]
  
  import E._
  
  val srvBook : ServiceBookF[E,P]
  val srvSales : ServiceSalesF[E,P]
  val srvChapter : ServiceChapterF[E,P]
  val srvAuthor : ServiceAuthorF[E,P]
  
  def getSummary( idBook: Int) : P[Summary] = {

     for {
       book <- srvBook.getBook(idBook)
       sales <- srvSales.getSales(idBook)

     }yield(
       new Summary(book, null, Optional.of(sales), null)
     )
    
  } 
  
}

package com.logicaalternativa.monadtransformerandmore.business.impl;

import akka.dispatch.OnComplete;
import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import scala.Function1;
import scala.concurrent.Future;
import scala.concurrent.Promise;
import scala.runtime.BoxedUnit;
import scala.util.Either;
import akka.dispatch.ExecutionContexts;
import akka.dispatch.Futures;

import com.logicaalternativa.monadtransformerandmore.bean.Java8;
import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryFutureEither;
import com.logicaalternativa.monadtransformerandmore.errors.Error;
import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceAuthorFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceBookFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceChapterFutEither;
import com.logicaalternativa.monadtransformerandmore.service.future.ServiceSalesFutEither;
import scala.util.Right;

import java.util.Optional;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;
import static com.logicaalternativa.monadtransformerandmore.bean.Java8.*;

public class SrvSummaryFutureEitherImpl implements SrvSummaryFutureEither<Error> {

	private final ServiceBookFutEither<Error> srvBook;
	private final ServiceSalesFutEither<Error> srvSales;
	private final ServiceChapterFutEither<Error> srvChapter;
	private final ServiceAuthorFutEither<Error> srvAuthor;
	
	private final MonadFutEither<Error> m;
	
	
	public SrvSummaryFutureEitherImpl(ServiceBookFutEither<Error> srvBook,
			ServiceSalesFutEither<Error> srvSales,
			ServiceChapterFutEither<Error> srvChapter,
			ServiceAuthorFutEither<Error> srvAuthor,
			MonadFutEither<Error> m) {
		super();
		this.srvBook = srvBook;
		this.srvSales = srvSales;
		this.srvChapter = srvChapter;
		this.srvAuthor = srvAuthor;
		this.m= m;
	}

	@Override
	public Future<Either<Error, Summary>> getSummary(Integer idBook) {

		//Primero creamos una promesa del resultado, en este caso, de un Either<Error, Summary>

		Promise<Either<Error, Summary>> promise = Future.promise();

		Future<Either<Error, Book>> book = srvBook.getBook(idBook);
		Future<Either<Error, Sales>> sales = srvSales.getSales(idBook);

		book.onComplete(

				new OnComplete<Either<Error, Book>>() {
					
					@Override
					public void onComplete(Throwable failure, Either<Error, Book> success) throws Throwable {
						if (success != null) {
							if (success.isRight()) {
								final Book book = success.right().get();

								sales.onComplete(new OnComplete<Either<Error, Sales>>() {
									@Override
									public void onComplete(Throwable failure, Either<Error, Sales> success) throws Throwable {
										if (success != null) {
											if (success.isRight()) {
												final Sales sales = success.right().get();

												final Summary summary = new Summary(book, null, Optional.of(Sales), null);

												promise.success(new Right<Error, Summary>(summary));
											}
										}
									}

								});
							}
						}
					}
				}

		);

		return $_notYetImpl();
	}

}

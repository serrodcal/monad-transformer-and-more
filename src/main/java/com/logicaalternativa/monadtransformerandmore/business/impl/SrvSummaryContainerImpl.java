package com.logicaalternativa.monadtransformerandmore.business.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import com.logicaalternativa.monadtransformerandmore.bean.Book;
import com.logicaalternativa.monadtransformerandmore.bean.Sales;
import com.logicaalternativa.monadtransformerandmore.bean.Summary;
import com.logicaalternativa.monadtransformerandmore.business.SrvSummaryContainer;
import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceAuthorContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceBookContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceChapterContainer;
import com.logicaalternativa.monadtransformerandmore.service.container.ServiceSalesContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

import javax.swing.text.html.Option;
import java.util.Optional;

public class SrvSummaryContainerImpl implements SrvSummaryContainer<Error> {
	
	private final ServiceBookContainer<Error> srvBook;
	private final ServiceSalesContainer<Error> srvSales;
	private final ServiceChapterContainer<Error> srvChapter;
	private final ServiceAuthorContainer<Error> srvAuthor;
	
	private final MonadContainer<Error> m;
	

	public SrvSummaryContainerImpl(ServiceBookContainer<Error> srvBook,
			ServiceSalesContainer<Error> srvSales,
			ServiceChapterContainer<Error> srvChapter,
			ServiceAuthorContainer<Error> srvAuthor, MonadContainer<Error> m) {
		super();
		this.srvBook = srvBook;
		this.srvSales = srvSales;
		this.srvChapter = srvChapter;
		this.srvAuthor = srvAuthor;
		this.m = m;
	}



	@Override
	/*public Container<Error, Summary> getSummary(Integer idBook) {

		Container<Error, Book> bookC = srvBook.getBook(idBook);
		Container<Error, Sales> salesC = srvSales.getSales(idBook);

		if (bookC.isOk()) {

			final Book book = bookC.getValue();

			if (salesC.isOk()) {

				final Sales sales = salesC.getValue();

				final Summary summary = new Summary(book, null, Optional.of(sales), null);

				return Container.value(summary);

			}
		}

		return Container.error(new Error() {
			@Override
			public String getDescription() {
				return "There is an error";
			}
		});
		
	}*/

	public Container<Error, Summary> getSummary(Integer idBook) {

		Container<Error, Book> bookC = srvBook.getBook(idBook);
		Container<Error, Sales> salesC = srvSales.getSales(idBook);

		return m.map2(bookC, salesC, (book, sales) -> new Summary(book, null, Optional.of(sales), null));

	}

}

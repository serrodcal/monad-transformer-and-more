package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.function.Function;

import com.logicaalternativa.monadtransformerandmore.container.Container;
import com.logicaalternativa.monadtransformerandmore.monad.MonadContainer;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

public class MonadContainerError implements MonadContainer<Error> {

	@Override
	public <T> Container<Error, T> pure(T aValue) {
		
		return Container.value(aValue);
			
	}

	@Override
	public <A, T> Container<Error, T> flatMap(Container<Error, A> from,
			Function<A, Container<Error, T>> f) {

		if (from.isOk()) {

			A value = from.getValue();

			return f.apply(value);

		} else {

			//return Container.error( from.getError() );

			return raiseError(from.getError());

		}

	}

	@Override
	public <T> Container<Error, T> raiseError(Error aError) {
		
		return Container.error(aError);
		
	}

	@Override
	public <A, T> Container<Error, T> recoverWith(Container<Error, T> from,
			Function<Error, Container<Error, T>> f) {

		if (from.isOk()) {

			return pure(from.getValue());

		} else {

			return f.apply(from.getError());

		}
		
	}

	

}

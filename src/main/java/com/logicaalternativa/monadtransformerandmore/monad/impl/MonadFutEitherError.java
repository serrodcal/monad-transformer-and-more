package com.logicaalternativa.monadtransformerandmore.monad.impl;

import static com.logicaalternativa.monadtransformerandmore.util.TDD.$_notYetImpl;

import java.util.function.Function;

import akka.dispatch.Futures;
import akka.dispatch.Recover;
import com.logicaalternativa.monadtransformerandmore.bean.Java8;
import com.logicaalternativa.monadtransformerandmore.errors.impl.MyError;
import scala.Function1;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.util.Either;
import com.logicaalternativa.monadtransformerandmore.errors.Error;

import com.logicaalternativa.monadtransformerandmore.monad.MonadFutEither;
import scala.util.Left;
import scala.util.Right;

public class MonadFutEitherError implements MonadFutEither<Error> {
	
	final ExecutionContext ec;
	
	
	public MonadFutEitherError(ExecutionContext ec) {
		super();
		this.ec = ec;
	}

	@Override
	public <T> Future<Either<Error, T>> pure(T value) {
		
		return Futures.successful(new Right<Error, T>(value));
	}

	@Override
	public <A, T> Future<Either<Error, T>> flatMap(
			Future<Either<Error, A>> from,
			Function<A, Future<Either<Error, T>>> f) {
		
		return from.flatMap( Java8.mapperF(
				(Either<Error, T> s) -> {
					if(s.isRight()) {
						return f.apply(s.right().get());
					} else {
						return raiseError(s.left().get());
					}
				}),
				ec).recover(new Recover<Either<Error, T>>() {
					@Override
					public Either<Error, T> recover(Throwable failure) throws Throwable {
						return new Left<>(new MyError("Exception : " + failure.getMessage()));
					}
		});
	}

	@Override
	public <T> Future<Either<Error, T>> raiseError(Error error) {
		
		return Futures.successful(new Left<Error, T>(error));
	}

	@Override
	public <A, T> Future<Either<Error, T>> recoverWith(
			Future<Either<Error, A>> from,
			Function<Error, Future<Either<Error, T>>> f) {

		return from.flatMap(Java8.mapperF(
				(Either<Error, T> s) -> {
					if(s.isRight()) {
						return pure(s.right().get());
					} else {
						return f.apply(s.left().get());
					}
				}),
				ec).recover(new Recover<Either<Error, T>>() {
			@Override
			public Either<Error, T> recover(Throwable failure) throws Throwable {
				return new Left<>(new MyError("Exception : " + failure.getMessage()));
			}
		});
	}
}

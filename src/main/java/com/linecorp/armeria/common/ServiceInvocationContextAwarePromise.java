package com.linecorp.armeria.common;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.Promise;

final class ServiceInvocationContextAwarePromise<T> implements Promise<T> {

    private final ServiceInvocationContext context;
    private final Promise<T> delegate;

    ServiceInvocationContextAwarePromise(ServiceInvocationContext context, Promise<T> delegate) {
        this.context = context;
        this.delegate = delegate;
    }

    @Override
    public Promise<T> setSuccess(T result) {
        return delegate.setSuccess(result);
    }

    @Override
    public boolean trySuccess(T result) {
        return delegate.trySuccess(result);
    }

    @Override
    public Promise<T> setFailure(Throwable cause) {
        return delegate.setFailure(cause);
    }

    @Override
    public boolean tryFailure(Throwable cause) {
        return delegate.tryFailure(cause);
    }

    @Override
    public boolean setUncancellable() {
        return delegate.setUncancellable();
    }

    @Override
    public Promise<T> addListener(
            GenericFutureListener<? extends Future<? super T>> listener) {
        return delegate.addListener(context.makeContextAware(listener));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Promise<T> addListeners(
            GenericFutureListener<? extends Future<? super T>>... listeners) {
        return delegate.addListeners(
                Stream.of(listeners)
                      .map(context::makeContextAware)
                      .toArray(GenericFutureListener[]::new));
    }

    @Override
    public Promise<T> removeListener(
            GenericFutureListener<? extends Future<? super T>> listener) {
        return delegate.removeListener(listener);
    }

    @Override
    public Promise<T> removeListeners(
            GenericFutureListener<? extends Future<? super T>>... listeners) {
        return delegate.removeListeners(listeners);
    }

    @Override
    public Promise<T> await() throws InterruptedException {
        return delegate.await();
    }

    @Override
    public Promise<T> awaitUninterruptibly() {
        return delegate.awaitUninterruptibly();
    }

    @Override
    public Promise<T> sync() throws InterruptedException {
        return delegate.sync();
    }

    @Override
    public Promise<T> syncUninterruptibly() {
        return delegate.syncUninterruptibly();
    }

    @Override
    public boolean isSuccess() {
        return delegate.isSuccess();
    }

    @Override
    public boolean isCancellable() {
        return delegate.isCancellable();
    }

    @Override
    public Throwable cause() {
        return delegate.cause();
    }

    @Override
    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return delegate.await(timeout, unit);
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        return delegate.await(timeoutMillis);
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        return delegate.awaitUninterruptibly(timeout, unit);
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        return delegate.awaitUninterruptibly(timeoutMillis);
    }

    @Override
    public T getNow() {
        return delegate.getNow();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return delegate.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return delegate.isCancelled();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return delegate.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
                                                     java.util.concurrent.TimeoutException {
        return delegate.get(timeout, unit);
    }
}

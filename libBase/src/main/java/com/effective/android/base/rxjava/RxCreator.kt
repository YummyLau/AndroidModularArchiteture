package com.effective.android.base.rxjava

import android.util.Log
import androidx.annotation.NonNull

import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableOnSubscribe
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer

/**
 * 方便业务创建
 * Created by yummyLau on 2019/6/20.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
object RxCreator {

    private val EMPTY_CONSUMER: Consumer<Any> = object : Consumer<Any> {
        override fun accept(v: Any) {}
        override fun toString(): String {
            return "EmptyConsumer"
        }
    }


    private val EMPTY_THROWABLE_CONSUMER: Consumer<Throwable> = EmptyThrowable("default")


    @JvmStatic
    fun <T> createObservable(callable: Callable<T>): Observable<T> =
            Observable.create(ObservableOnSubscribe { e ->
                val t = callable.call()
                if (t == null) {
                    e.onError(RuntimeException("RxCreator#createObservable error ！Callable#call should result non-null value when create a observable！"))
                    return@ObservableOnSubscribe
                }
                e.onNext(t)
                e.onComplete()
            })

    @JvmStatic
    fun <T> createFlowable(callable: Callable<T>, backpressureStrategy: BackpressureStrategy): Flowable<T> =
            Flowable.create(FlowableOnSubscribe { e ->
                val t = callable.call()
                if (t == null) {
                    e.onError(RuntimeException("RxCreator#createFlowable error ！Callable#call should result non-null value when create a flowable！"))
                    return@FlowableOnSubscribe
                }
                e.onNext(t)
                e.onComplete()
            }, backpressureStrategy)


    @JvmStatic
    fun <T> createFlowable(callable: Callable<T>): Flowable<T> {
        return createFlowable(callable, BackpressureStrategy.BUFFER)
    }

    @JvmStatic
    fun <T> createObservableDelayed(@NonNull callable: Callable<T>, delay: Long): Observable<T> {
        return Observable.timer(delay, TimeUnit.MILLISECONDS)
                .flatMap {
                    createObservable(callable)
                }
    }

    @JvmStatic
    fun <T> createFlowableDelayed(@NonNull callable: Callable<T>, delay: Long): Flowable<T> {
        return Flowable.timer(delay, TimeUnit.MILLISECONDS)
                .flatMap {
                    createFlowable(callable)
                }
    }

    @JvmStatic
    fun emptyThrowable(): Consumer<Throwable> {
        return EMPTY_THROWABLE_CONSUMER
    }

    @JvmStatic
    fun emptyThrowable(clazz: Class<*>, method: String): Consumer<Throwable> {
        return EmptyThrowable(clazz.simpleName + "." + method)
    }

    @JvmStatic
    fun <T> emptyConsumer(): Consumer<T> {
        return EMPTY_CONSUMER as Consumer<T>
    }

    class EmptyThrowable(var tag: String) : Consumer<Throwable> {

        @Throws(Exception::class)
        override fun accept(throwable: Throwable?) {
            var throwable = throwable
            if (throwable == null) {
                throwable = NullPointerException("onError called with null. Null values are generally not allowed in 2.x operators and sources.")
            }

            Log.e(RxCreator::class.java.simpleName, "call" + "-" + toString(), throwable)
        }

        override fun toString(): String {
            return "$tag-EmptyThrowable"
        }
    }


    @JvmStatic
    fun <T> createObservableTask(callable: Callable<T>, pScheduler: Scheduler) =
            createObservable(callable)
                    .subscribeOn(pScheduler)
                    .subscribe(emptyConsumer(), emptyThrowable())

    @JvmStatic
    fun <T> createFlowableTask(callable: Callable<T>, pScheduler: Scheduler) =
            createFlowable(callable)
                    .subscribeOn(pScheduler)
                    .subscribe(emptyConsumer(), emptyThrowable())

}

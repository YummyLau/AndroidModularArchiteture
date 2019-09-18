package com.effective.android.base.rxjava

import org.reactivestreams.Publisher

import io.reactivex.Flowable
import io.reactivex.FlowableTransformer
import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


/**
 * 统一rxjava2调度
 * Created by yummyLau on 2019/6/20.
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
object RxSchedulers {

    /**
     * Don't break the chain: use RxJava's compose() operator
     */
    @JvmStatic
    fun <T> observableComputationToMain(): ObservableTransformer<T, T> =
            ObservableTransformer {
                it.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> flowableComputationToMain(): FlowableTransformer<T, T> =
            FlowableTransformer {
                it.subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
            }


    @JvmStatic
    fun <T> observableIoToMain(): ObservableTransformer<T, T> =
            ObservableTransformer {
                it.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> flowableIoToMain(): FlowableTransformer<T, T> =
            FlowableTransformer {
                it.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> observableNewThreadToMain(): ObservableTransformer<T, T> =
            ObservableTransformer {
                it.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> flowableNewThreadToMain(): FlowableTransformer<T, T> =
            FlowableTransformer {
                it.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> observableTrampolineToMain(): ObservableTransformer<T, T> =
            ObservableTransformer {
                it.subscribeOn(Schedulers.trampoline())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> flowableTrampolineToToMain(): FlowableTransformer<T, T> =
            FlowableTransformer {
                it.subscribeOn(Schedulers.trampoline())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> observableSingleToMain(): ObservableTransformer<T, T> =
            ObservableTransformer {
                it.subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
            }

    @JvmStatic
    fun <T> flowableSingleToToMain(): FlowableTransformer<T, T> =
            FlowableTransformer {
                it.subscribeOn(Schedulers.single())
                        .observeOn(AndroidSchedulers.mainThread())
            }

}

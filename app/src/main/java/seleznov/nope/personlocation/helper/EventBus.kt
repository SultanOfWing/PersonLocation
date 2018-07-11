package seleznov.nope.personlocation.helper

import io.reactivex.annotations.NonNull
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by User on 11.07.2018.
 */
class EventBus private constructor(){
    private val mSubject = PublishSubject.create<Any>()

    private object Holder { val INSTANCE = EventBus() }

    companion object {
        val instance: EventBus by lazy { Holder.INSTANCE }
    }

    fun subscribe(action: (Any) -> Unit): Disposable {
        return mSubject.subscribe(action)
    }

    fun publish(message: Any) {
        mSubject.onNext(message)
    }

}
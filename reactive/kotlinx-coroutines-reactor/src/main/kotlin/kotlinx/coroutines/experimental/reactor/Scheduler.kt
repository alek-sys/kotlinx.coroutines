package kotlinx.coroutines.experimental.reactor

import kotlinx.coroutines.experimental.CoroutineDispatcher
import kotlinx.coroutines.experimental.DisposableHandle
import reactor.core.Disposable
import reactor.core.scheduler.Scheduler
import kotlin.coroutines.experimental.CoroutineContext

/**
 * Converts an instance of [Scheduler] to an implementation of [CoroutineDispatcher].
 */
fun Scheduler.asCoroutineDispatcher() = SchedulerCoroutineDispatcher(this)

/**
 * Implements [CoroutineDispatcher] on top of an arbitrary [Scheduler].
 * @param scheduler a scheduler.
 */
open class SchedulerCoroutineDispatcher(private val scheduler: Scheduler) : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        scheduler.schedule(block)
    }

    override fun toString(): String = scheduler.toString()
    override fun equals(other: Any?): Boolean = other is SchedulerCoroutineDispatcher && other.scheduler === scheduler
    override fun hashCode(): Int = System.identityHashCode(scheduler)
}

private fun Disposable.asDisposableHandle(): DisposableHandle =
        object : DisposableHandle {
            override fun dispose() = this@asDisposableHandle.dispose()
        }
package net.uniloftsky.nukkit.foes.observer

import cn.nukkit.event.Event
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ExecutorService
import kotlin.reflect.KClass

/**
 * Events publisher that pushes different type of [Event] further to its subscribers
 */
abstract class EventPublisher(

    /**
     * Executor service to notify subscribers concurrently
     */
    private val executor: ExecutorService,

    /**
     * Map of subscribers. Key - event type (event class), value - list of subscribers related to the given event type
     */
    private val _subscribers: ConcurrentHashMap<KClass<out Event>, MutableSet<EventSubscriber>> = ConcurrentHashMap()
) {

    val subscribers get() = _subscribers.toMap()

    /**
     * Registers a given [EventSubscriber] to receive notifications for a specific event type.
     * <p>
     * The subscriber will be notified whenever an event of the specified type is published.*
     */
    fun subscribe(subscriber: EventSubscriber) {
        if (subscriber.getEventTypes().isEmpty()) {
            throw IllegalArgumentException("Subscriber should have at least one registered event type")
        }
        for (event in subscriber.getEventTypes()) {
            val subscribersByType = _subscribers.computeIfAbsent(event) { HashSet() }
            subscribersByType.add(subscriber)
        }
    }

    /**
     * Push given [Event] to publisher. Publisher notifies subscribers about the given event
     */
    fun pushEvent(event: Event) {
        _subscribers[event::class]?.forEach {
            executor.submit { it.handleEvent(event) }
        }
    }

    /**
     * Should be called on programm shutdown
     */
    @Synchronized
    fun shutdown() {
        if (!executor.isShutdown) {
            executor.shutdown()
        }
    }

}
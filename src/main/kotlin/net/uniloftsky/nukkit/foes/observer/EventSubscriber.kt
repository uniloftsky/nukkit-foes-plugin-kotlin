package net.uniloftsky.nukkit.foes.observer

import cn.nukkit.event.Event
import cn.nukkit.plugin.PluginLogger
import kotlin.reflect.KClass

/**
 * Abstract class for events subscribers.
 * <p>
 * Subclasses can subscribe for specific Nukkit [Event] classes.
 * <p>
 * For example, they can subscribe to events like [cn.nukkit.event.player.PlayerJoinEvent], [cn.nukkit.event.player.PlayerMoveEvent]
 */
abstract class EventSubscriber(

    /**
     * Plugin logger
     */
    private val logger: PluginLogger,

    /**
     * Registry for event handlers, where each [Event] type is associated with a specific handler
     */
    private val internalHandlers: Map<KClass<out Event>, EventHandler<Event>>
) {

    val handlers get() = HashMap(internalHandlers)

    /**
     * Returns list of events types the subscriber is subscribed to.
     */
    fun getEventTypes(): List<KClass<out Event>> {
        return internalHandlers.keys.toList()
    }

    /**
     * Process the given [event].
     */
    fun handleEvent(event: Event) {
        val handler: EventHandler<Event>? = internalHandlers[event::class]
        handler?.handle(event) ?: logger.warning("Cannot find the handler for event ${event.eventName}")
    }

}
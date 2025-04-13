package net.uniloftsky.nukkit.foes.observer

import cn.nukkit.event.Event
import cn.nukkit.event.player.PlayerFishEvent
import cn.nukkit.event.player.PlayerJoinEvent
import cn.nukkit.plugin.PluginLogger
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.reflect.KClass
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@ExtendWith(MockKExtension::class)
class EventSubscriberTest {

    @MockK
    private lateinit var logger: PluginLogger

    private lateinit var testSubscriber: EventSubscriber

    @BeforeEach
    fun setUp() {
        testSubscriber = TestSubscriber(logger)
        HANDLERS.clear()
    }

    @Test
    fun testGetHandlersMap() {

        // given
        val eventType = PlayerJoinEvent::class
        val eventHandler: EventHandler<Event> = mockk()
        HANDLERS[eventType] = eventHandler

        // when
        val handlers = testSubscriber.handlers

        // then
        assertNotNull(handlers)
        val actualHandler = handlers[eventType]
        assertEquals(eventHandler, actualHandler)
    }

    @Test
    fun getEventTypes() {

        // given
        val eventType = PlayerFishEvent::class
        val eventHandler: EventHandler<Event> = mockk()
        HANDLERS[eventType] = eventHandler

        // when
        val result = testSubscriber.getEventTypes()

        // then
        assertNotNull(result)
        assertEquals(eventType, result.first())
    }

//    @Test
//    fun testHandleEvent() {
//
//        // given
//        val eventType = PlayerFishEvent::class
//        val event = TestEvent()
//        val eventHandler: EventHandler<Event> = mockk()
//        HANDLERS[eventType] = eventHandler
//
//        // when
//        testSubscriber.handleEvent(event)
//
//        // then
//        verify { eventHandler.handle(event) }
//    }

    class TestSubscriber(logger: PluginLogger) : EventSubscriber(logger, HANDLERS)

    class TestEvent : Event()

    companion object {
        val HANDLERS: MutableMap<KClass<out Event>, EventHandler<Event>> = HashMap()
    }
}
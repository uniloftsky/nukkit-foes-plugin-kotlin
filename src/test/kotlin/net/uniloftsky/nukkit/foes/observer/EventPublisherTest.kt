package net.uniloftsky.nukkit.foes.observer

import cn.nukkit.event.player.PlayerJoinEvent
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.util.concurrent.ExecutorService
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@ExtendWith(MockKExtension::class)
class EventPublisherTest {

    @MockK
    private lateinit var executor: ExecutorService

    private lateinit var testPublisher: EventPublisher

    @BeforeEach
    fun setUp() {
        testPublisher = TestPublisher(executor)
    }

    @Test
    fun testSubscribe() {

        // given
        val eventType = PlayerJoinEvent::class
        val subscriber = mockkClass(EventSubscriber::class)

        every { subscriber.getEventTypes() } returns listOf(eventType)

        // when
        testPublisher.subscribe(subscriber)

        // then
        val subscribersSize = testPublisher.subscribers.size
        assertTrue { subscribersSize > 0 }
        val actualSubscribers = testPublisher.subscribers[eventType]
        assertNotNull(actualSubscribers)
        assertTrue { actualSubscribers.contains(subscriber) }
    }

    @Test
    fun testSubscribeInvalidSubscriber() {

        // given
        val subscriber = mockkClass(EventSubscriber::class)

        every { subscriber.getEventTypes() } returns emptyList()

        try {

            // when
            testPublisher.subscribe(subscriber)
        } catch (ex: IllegalArgumentException) {

            // then
            assertNotNull(ex)
            assertNotNull(ex.message)
        }
    }

    @Test
    fun testPushEvent() {

        // given
        val eventType = PlayerJoinEvent::class
        val event = mockkClass(eventType)
        val subscriber = mockkClass(EventSubscriber::class)

        every { subscriber.getEventTypes() } returns listOf(eventType)
        testPublisher.subscribe(subscriber)

        every { executor.submit(any(Runnable::class)) } returns mockk()
        every { subscriber.handleEvent(event) } just runs

        // when
        testPublisher.pushEvent(event)

        // then
        val runnableSlot = slot<Runnable>()
        verify { executor.submit(capture(runnableSlot)) }
        runnableSlot.captured.run()
        verify { subscriber.handleEvent(event) }
    }

    class TestPublisher(executor: ExecutorService) : EventPublisher(executor)

}
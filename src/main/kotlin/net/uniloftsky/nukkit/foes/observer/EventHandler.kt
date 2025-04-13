package net.uniloftsky.nukkit.foes.observer

import cn.nukkit.event.Event

fun interface EventHandler<in T :Event> {

    fun handle(event: T)

}
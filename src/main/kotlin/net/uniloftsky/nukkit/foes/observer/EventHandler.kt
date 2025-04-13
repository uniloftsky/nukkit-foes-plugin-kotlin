package net.uniloftsky.nukkit.foes.observer

import cn.nukkit.event.Event

interface EventHandler<in T :Event> {

    fun handle(event: T)

}
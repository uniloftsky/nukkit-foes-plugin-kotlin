package net.uniloftsky.nukkit.foes

import cn.nukkit.plugin.PluginBase

class PluginMainKt: PluginBase() {

    override fun onEnable() {
        this.logger.info("FoesPlugin enabled! Kotlin!!")
    }

    override fun onDisable() {
        this.logger.info("FoesPlugin disabled!")
    }

}
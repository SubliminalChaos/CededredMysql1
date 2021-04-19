package me.waqe.plugin

import me.waqe.plugin.database.MySQL
import me.waqe.plugin.database.SQLGetter
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerJoinEvent

class App : JavaPlugin(), Listener {
    companion object {
        lateinit var instance: App
    }

    lateinit var sql: MySQL
    lateinit var data: SQLGetter

    override fun onEnable() {
        instance = this

        this.sql = MySQL()
        this.data = SQLGetter()

        try {
            sql.connect()
        } catch (e: ClassNotFoundException) {
            // e.printStackTrace()
            // login info is incorrect - OR - they are not using a database
            Bukkit.getLogger().info("Database not connected.")
        }
        if (sql.isConnected()) {
            Bukkit.getLogger().info("Database is connected.")
            data.createTable()
        }
        this.server.pluginManager.registerEvents(this, this)

    }

    override fun onDisable() {
        sql.disconnect()
    }

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        e.joinMessage = "Welcome To My Server!"
        if (sql.isConnected()) {
            data.createPlayer(e.player)
        }
    }

    @EventHandler
    fun onMobKill(e: EntityDeathEvent) {
        if (sql.isConnected()) {
            if (e.entity.killer is Player) {
                val player = e.entity.killer as Player
                data.addPoints(player.uniqueId, 1)
                player.sendMessage("Point added.")
            }
        }
    }
}


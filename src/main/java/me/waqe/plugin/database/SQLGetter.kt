package me.waqe.plugin.database

import me.waqe.plugin.App.Companion.instance
import org.bukkit.entity.Player
import java.sql.PreparedStatement
import java.sql.SQLException
import java.util.*

class SQLGetter {
    fun createTable() {
        try {
            val ps = instance.sql.getConnection()?.prepareStatement("CREATE TABLE IF NOT EXISTS mobpoints (NAME VARCHAR(100), " +
                    "UUID VARCHAR(100), POINTS INT(100), PRIMARY KEY (NAME))")!!
            ps.executeUpdate( )
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun createPlayer(player: Player) {
        try {
            val uuid = player.uniqueId
            if (!isPlayerInDb(uuid)) {
                val ps2 = instance.sql.getConnection()?.prepareStatement(
                    "INSERT IGNORE INFO mobpoints (NAME, UUID) VALUES (?,?)")
                ps2?.setString(1, player.name)
                ps2?.setString(2, uuid.toString())
                ps2?.executeUpdate()

                return
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    // player exists?
    fun isPlayerInDb(uuid: UUID): Boolean {
        try {
            var ps = instance.sql.getConnection()?.prepareStatement("SELECT * FROM mobpoints WHERE UUID=?")
            ps?.setString(1, uuid.toString())
            val results = ps?.executeQuery()
            // player exists in next()
            if (results?.next() == true) {
                return true
            }
            return false
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return false
    }

    fun addPoints(uuid: UUID, points: Int) {
        try {
            val ps = instance.sql.getConnection()?.prepareStatement(
                "UPDATE mobpoints SET POINTS=? WHERE UUID=?"
            )
            ps?.setInt(1, (getPoints(uuid) + points))
            ps?.setString(2, uuid.toString())
            ps?.executeUpdate()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }

    fun getPoints(uuid: UUID): Int {
        try {
            val ps = instance.sql.getConnection()?.prepareStatement(
                "SELECT POINTS FROM mobpoints WHERE UUID=?"
            )
            ps?.setString(1, uuid.toString())
            val rs = ps?.executeQuery()
            if (rs?.next() == true) {
                val points = rs.getInt("POINTS")
                return points
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
        return 0
    }

}
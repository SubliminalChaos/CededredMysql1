package me.waqe.plugin.database

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException


class MySQL {
    // Usually in a config.yml file!  (-:
    // connection settings
    val host = "localhost"
    val port = "3306"
    val database = "codedredplugins"
    val username = "root"
    val password = ""

    var connection: Connection? = null
    fun isConnected(): Boolean {
        return (connection != null)
    }

    @Throws(ClassNotFoundException::class, SQLException::class)
    fun connect() {
        if (!isConnected()) {
            connection = DriverManager.getConnection(
                "jdbc:mysql://${host}:${port}/${database}?useSSL=false", username, password)
        }
    }

    fun disconnect() {
        if (isConnected()) {
            try {
                connection!!.close()
            } catch (e: SQLException) {
                e.printStackTrace()
            }
        }
    }

    @JvmName("getConnection1")
    fun getConnection(): Connection? {
        return connection
    }
}
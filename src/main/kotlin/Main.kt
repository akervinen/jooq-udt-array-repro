package org.example

import org.jooq.impl.DSL

fun main() {
    System.setProperty("org.jooq.no-logo", "true")
    System.setProperty("org.jooq.no-tips", "true")

    val c = DSL.using("jdbc:postgresql://localhost:55432/repro", "sa", "sa")

    println("jooq:")
    println(c.selectFrom(Tables.QUZ).fetch())

    println("jdbc:")
    c.connection {
        val query = it.prepareStatement("select * from quz")
        val res = query.executeQuery()
        while (res.next()) {
            println(res.getArray("foos"))
        }
    }
}
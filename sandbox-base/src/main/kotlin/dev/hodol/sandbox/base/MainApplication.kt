package dev.hodol.sandbox.base

import org.hibernate.reactive.mutiny.Mutiny
import javax.persistence.Persistence.createEntityManagerFactory

fun main() {
    // https://vertx.io/docs/vertx-mysql-client/java/
    // val sqlConnectOptions = MySQLConnectOptions()
    // sqlConnectOptions.charset = "utf8"
    // sqlConnectOptions.collation = "utf8_general_ci"

    // https://thorben-janssen.com/hibernate-reactive-getting-started-guide/
    val entityManagerFactory = createEntityManagerFactory("base")
    val factory = entityManagerFactory.unwrap(Mutiny.SessionFactory::class.java)

    val author = Author("hodol")

    // 트랜잭션
    factory.withTransaction { session, tx -> session.persist(author) }
        .await()
        .indefinitely()

    // Hibernate 세션
    factory.withSession { session ->
        session.find(Author::class.java, 1L)
            .chain { author -> session.fetch(author.books) }
            .invoke { books -> println(author) }
    }
        .await()
        .indefinitely()
}

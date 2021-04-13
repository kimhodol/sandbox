dependencies {
    implementation("org.hibernate.reactive:hibernate-reactive-core:1.0.0.CR1")
    implementation("io.vertx:vertx-mysql-client:3.9.6") // 최신버전은 4.x지만 Hibernate가 지원안함
    implementation("org.hibernate:hibernate-validator:7.0.1.Final")
}

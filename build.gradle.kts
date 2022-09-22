import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jooq.meta.jaxb.GeneratedSerialVersionUID
import org.jooq.meta.jaxb.Logging

plugins {
    kotlin("jvm") version "1.7.10"
    application
    id("org.flywaydb.flyway") version "9.3.1"
    id("nu.studer.jooq") version "7.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

val jooqVersion = "3.17.4"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    implementation("org.jooq:jooq:$jooqVersion")
    implementation("org.postgresql:postgresql:42.5.0")

    jooqGenerator("org.postgresql:postgresql:42.5.0")
}

buildscript {
    dependencies {
        classpath("org.postgresql:postgresql:42.5.0")
    }
}

flyway {
    url = "jdbc:postgresql://localhost:55432/repro"
    user = "sa"
    password = "sa"
    schemas = arrayOf("public")
    locations = arrayOf("filesystem:./src/main/resources")
    cleanDisabled = false
}

jooq {
    version.set(jooqVersion)

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN
                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = "jdbc:postgresql://localhost:55432/repro"
                    user = "sa"
                    password = "sa"
                }
                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"
                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"
                    }
                    generate.apply {
                        generatedSerialVersionUID = GeneratedSerialVersionUID.OFF
                    }
                    target.apply {
                        packageName = "org.example"
                        directory = "build/generated-src/jooq/main"
                    }
                    strategy.name = "org.jooq.codegen.DefaultGeneratorStrategy"
                }
            }
        }
    }
}

tasks.named("generateJooq").configure {
    dependsOn(tasks.named("flywayMigrate"))

    inputs.files(fileTree("src/main/resources"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}

application {
    mainClass.set("org.example.MainKt")
}

plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Driver do MySQL para conexão via JDBC
    implementation("com.mysql:mysql-connector-j:8.3.0")
    // Leitor de variáveis de ambiente (.env)
    implementation("io.github.cdimascio:dotenv-java:3.0.0")

    testImplementation(platform("org.junit:junit-bom:6.0.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("initDb") {
    mainClass.set("br.com.kombuskini.util.DbInitializer")
    classpath = sourceSets["main"].runtimeClasspath
}
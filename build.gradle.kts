plugins {
    java
    id("org.springframework.boot") version "3.0.5"
    id("io.spring.dependency-management") version "1.1.0"
    id("org.hibernate.orm") version "6.2.0.Final"
    id("io.swagger.core.v3.swagger-gradle-plugin") version "2.2.9"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["snippetsDir"] = file("build/generated-snippets")

configurations {
    all { exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
        exclude(group = "com.google")
        exclude(group = "com.android")
        exclude(group = "org.glassfish.jaxb")
        exclude(group = "io.smallrye")
        exclude(group = "org.hamcrest")
        exclude(group = "com.vaadin")}
}

dependencies {
    testImplementation("org.projectlombok:lombok:1.18.26")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "junit")
        exclude(module = "junit-vintage-engine")
        //exclude(module = "mockito-core")
    }
    testImplementation("org.mockito:mockito-inline:4.8.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.4.2")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("org.springframework.security:spring-security-test")
    testCompileOnly("org.junit.jupiter:junit-jupiter:5.4.2")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    compileOnly("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-parent:3.0.5")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-rest")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
    implementation("io.jsonwebtoken:jjwt-jackson:0.11.5")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-hibernate5:2.13.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.1.0")
    implementation("org.springframework.boot:spring-boot-starter-log4j2:3.0.6")
    implementation ("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("com.google.googlejavaformat:google-java-format:1.15.0") //requirements from Iryna Afanasieva
}

tasks.withType<Test> {
    useJUnitPlatform()
    systemProperty("ENV_DB_USER", "test_user")
    systemProperty("ENV_DB_URL", "jdbc:postgresql://0.0.0.0:5433/test")
    systemProperty("ENV_DB_PASS", "password")
    environment("ENV_JWT", "3hLlrwrZS8NwKAlhV72NY3M7mmp74Y86/ZPS9wWMsO70BBude+UGxGo1oqC52VRP7P6fG1925mveNn1ta6xAPnywcQ5U+2xw6kqiHQqRf/1GT7dCvialwBucPJiwkf/lTL18GZDsXbuZEsLIuQp+LeZ1bcYgnG4qIS+vg74Da5MNHZV/elYGZPmtmMyITvFgmAGYof5nxpE4Vt8GG8oGkhstgMa4wXYpt8Zo2Q==")
}
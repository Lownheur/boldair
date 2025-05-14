plugins {
	java
	id("org.springframework.boot") version "3.4.4"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "boldair"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(22)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	implementation("org.springframework.boot:spring-boot-starter-actuator")

	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
	runtimeOnly("org.postgresql:postgresql")

	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
  implementation("nz.net.ultraq.thymeleaf:thymeleaf-layout-dialect:3.3.0")

  implementation("jakarta.inject:jakarta.inject-api:2.0.1")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")

//	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<JavaCompile> {
	options.compilerArgs.add("-parameters")
  options.encoding = "UTF-8"
}

tasks.withType<Javadoc>{
  options.encoding = "UTF-8"
}

tasks.withType<Test> {
	useJUnitPlatform()
}


tasks.register("init_config") {
  group = "_Custom tasks"
  description = "Initializes project configuration"
  doLast {
    ant.withGroovyBuilder {
      "copy"("todir" to ".") {
        "fileset"("dir" to "_default-config")
      }
    }
  }
}


tasks.register("UtilServeurs") {
  group = "_Custom tasks"
  description = "Initializes project configuration"
  doLast {
    ant.withGroovyBuilder {
      "exec"("executable" to "CMD", "spawn" to true) {
        "arg"("value" to "/C")
        "arg"("value" to "UtilServeurs.bat")
      }
    }
  }
}

task<Exec>("UtilServeurs0") {
  group = "_Custom tasks"
  description = "Run UtilServeurs"
  commandLine( "CMD", "/C", "UtilServeurs.bat")
}

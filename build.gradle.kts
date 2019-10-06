import com.techshroom.inciseblue.commonLib

plugins {
    id("org.rivierarobotics.gradlerioredux") version "0.6.0"
}

gradleRioRedux {
    robotClass = "org.rivierarobotics.robot.Robot"
    teamNumber = 5818
}

dependencies {
	implementation("org.slf4j:slf4j-api:1.7.25")
	commonLib("ch.qos.logback", "logback", "1.2.3") {
		implementation(lib("classic"))
		implementation(lib("core"))
	}
}

repositories {
	jcenter()
}

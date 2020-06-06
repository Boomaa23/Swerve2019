import com.techshroom.inciseblue.commonLib

plugins {
    id("org.rivierarobotics.gradlerioredux") version "0.7.8"
}

gradleRioRedux {
    robotClass = "org.rivierarobotics.robot.Robot"
    teamNumber = 5818
}


val includeDesktopSupport = true
val platform = wpi.platforms.javaClass.getDeclaredField("desktop").get(null) as String

tasks.register("windowsLaunchSim") {
	doLast {
		project.exec{
			workingDir = file("./build/")
			commandLine("cmd", "/C", "start", "gradlerio_simulateJava.bat")
		}
	}
}
if (platform.contains("windows")) {
	tasks.getByName("simulateJava").finalizedBy(tasks.getByName("windowsLaunchSim"))
}

dependencies {
	for (depJni: String in (wpi.deps.wpilibJni(platform) + wpi.deps.vendor.jni(platform))) {
		nativeDesktopZip(depJni)
	}
	simulation("edu.wpi.first.halsim:halsim_gui:${wpi.wpilibVersion}:$platform@zip")

	implementation("org.rivierarobotics.apparjacktus:apparjacktus:0.1.1")
	commonLib("net.octyl.apt-creator", "apt-creator", "0.1.4") {
		compileOnly(lib("annotations"))
		annotationProcessor(lib("processor"))
	}
	commonLib("com.google.dagger", "dagger", "2.25.4") {
		implementation(lib())
		annotationProcessor(lib("compiler"))
	}
}

repositories {
	jcenter()
}

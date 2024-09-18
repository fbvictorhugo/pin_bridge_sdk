import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    `maven-publish`
}

val localProperties = loadLocalProperties()

android {
    namespace = "dev.fbvictorhugo.pin_bridge_sdk"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        aarMetadata {
            minCompileSdk = 24
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

fun getVersionName(): String {
    return "0.0.5"
}

fun getArtificatId(): String {
    return "pin_bridge_sdk"
}

fun loadLocalProperties(): Properties {
    val properties = Properties()
    val localPropertiesFile = file("..\\local.properties")

    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use { properties.load(it) }
    }
    return properties
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/fbvictorhugo/pin_bridge_sdk")
            credentials {
                //username = localProperties.getProperty("gpr.user") ?: System.getenv("USERNAME")
                //password = localProperties.getProperty("gpr.key") ?: System.getenv("TOKEN")
                username = System.getenv("USERNAME") ?: error("USERNAME not found")
                password = System.getenv("TOKEN") ?: error("TOKEN not found")
            }
        }
    }
    publications {
        create<MavenPublication>("gpr") {
            run {
                groupId = "dev.fbvictorhugo"
                artifactId = getArtificatId()
                version = getVersionName()
                artifact("$projectDir/build/outputs/aar/${getArtificatId()}-release.aar")

                pom {
                    name.set("PinBridge SDK")
                    description.set("Android library for Pinterest REST API")
                    url.set("https://github.com/fbvictorhugo/pin_bridge_sdk")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("fbvictorhugo")
                            name.set("Victor Hugo")
                        }
                    }

                    scm {
                        url.set("https://github.com/fbvictorhugo/pin_bridge_sdk")
                    }
                }
            }
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.androidx.datastore.preferences)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
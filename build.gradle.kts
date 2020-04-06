plugins {
    id("com.jfrog.bintray") version "1.8.4"
    `maven-publish`
    kotlin("jvm") version "1.3.71"
    id("kotlinx-serialization") version "1.3.71"
}

group = "com.julianjarecki"
version = "1.0.0-alpha-1"

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.20.0")
    implementation("no.tornado:tornadofx:1.7.20")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }


    val artifactName = project.name
    val artifactGroup = project.group.toString()
    val artifactVersion = project.version.toString()
    val gitHubRepo = "IARI/tfxserializer"
    val gitURL = "https://github.com/$gitHubRepo"
    val issueURL = "$gitURL/issues"

    publishing {
        publications {
            create<MavenPublication>("tfxserializer") {
                groupId = artifactGroup
                artifactId = artifactName
                version = artifactVersion
                from(project.components["java"])

                pom {
                    name.set("My Library")
                    description.set("A concise description of my library")
                    url.set("http://www.example.com/library")
                    /*
                    properties.apply {
                        put("myProp", "value")
                    }
                    */
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("jj")
                            name.set("Julian Jarecki")
                            email.set("julian.jarecki@gmail.com")
                        }
                    }
                    scm {
                        //connection.set("scm:git:git://example.com/my-library.git")
                        //developerConnection.set("scm:git:ssh://example.com/my-library.git")
                        url.set(gitURL)
                    }
                }
            }
        }
    }

    bintray {
        user = project.findProperty("bintrayUser").toString()
        key = project.findProperty("bintrayKey").toString()
        publish = true

        setPublications("tfxserializer")

        pkg.apply {
            repo = "maven"
            name = artifactName
            //userOrg = "julianjarecki"
            githubRepo = gitHubRepo
            vcsUrl = gitURL
            description = "a collection of kotlin serializers for javafx properties and utilities to use them with tornadofx"
            setLabels("kotlin", "tornadofx", "javafx", "serialization", "utils")
            setLicenses("Apache-2.0")
            desc = description
            websiteUrl = gitURL
            issueTrackerUrl = issueURL
            //githubReleaseNotesFile = githubReadme

            version.apply {
                name = artifactVersion
                //desc = pomDesc
                //released = Date().toString()
                vcsTag = artifactVersion
            }
        }
    }
}
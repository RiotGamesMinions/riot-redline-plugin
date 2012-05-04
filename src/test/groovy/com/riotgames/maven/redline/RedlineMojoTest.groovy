package com.riotgames.maven.redline

import org.junit.Test
import org.junit.Before
import org.apache.maven.plugin.MojoExecutionException

class RedlineMojoTest extends GroovyTestCase {

    def redlineMojo

    @Before
    void setUp() {
        redlineMojo = new RedlineMojo()
    }

    @Test
    void testGoodParsedVersion() {
        def result = redlineMojo.parsePackagingVersion("1.0-SNAPSHOT")
        assertEquals result, "1.0SNAPSHOT"
    }

    @Test
    void testGoodPlatform() {
        redlineMojo.platform = createGoodPlatform()
        redlineMojo.setupPlatform()
    }

    @Test
    void testPlatformWithBadOS() {
        redlineMojo.platform = createBadPlatform("bad_os", "X86_64")
        shouldFail(MojoExecutionException) {
            redlineMojo.setupPlatform()
        }
     }

    @Test
    void testPlatformWithBadArchitecture() {
        redlineMojo.platform = createBadPlatform("LINUX", "bad_architecture")
        shouldFail(MojoExecutionException) {
            redlineMojo.setupPlatform()
        }
    }

    def Platform createGoodPlatform() {
        def platform = new Platform()
        platform.os = "Linux"
        platform.architecture = "X86_64"
        return platform
    }

    def Platform createBadPlatform(def os, def architecture) {
        def platform = new Platform()
        platform.os = os
        platform.architecture = architecture
        return platform
    }
}
package com.riotgames.maven.redline

class RpmDependency {

    /**
     * The package name of the RPM package that this package depends on
     * (see: RPMTAG_REQUIRENAME)
     */
    def String name

    /**
     * The version string of the RPM package this this package depends on
     * (see: RPMTAG_REQUIREVERSION)
     */
    def String version

    @Override
    def String toString() {
        """
        Dependent RPM package name: $name
        Dependent RPM package version: $version
        """
    }
}
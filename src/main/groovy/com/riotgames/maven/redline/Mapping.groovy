package com.riotgames.maven.redline

class Mapping {

    /**
     * The directory where the source will be installed
     */
    def String directory

    /**
     * A List of source files that will be installed into the rpm
     */
    def List sources

    @Override
    def String toString() {
        """
        Directory: $directory
        Sources: $sources
        """
    }
}
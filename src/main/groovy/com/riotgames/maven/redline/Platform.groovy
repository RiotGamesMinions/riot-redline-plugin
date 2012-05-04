package com.riotgames.maven.redline

/**
 * The underlying platform information that will be associated with an rpm
 */
class Platform {

    /**
     * The operating system's architecture
     */
    def String architecture

    /**
     * The operating system itself
     */
    def String os

    /**
     * Default to NOARCH / Linux
     */
    public Platform() {
        this.architecture = "NOARCH"
        this.os = "LINUX"
    }

    /**
     * Sets the architecture to all uppercase characters
     * @param architecture
     */
    public void setArchitecture(String architecture) {
        this.architecture = architecture.toUpperCase()
    }

    /**
     * Sets the os to all uppercase characters
     * @param os
     */
    public void setOs(String os) {
        this.os = os.toUpperCase()
    }

    /**
     * Pretty implementation of the toString method
     * @return
     */
    @Override
    def String toString() {
        """
        Architecture: $architecture
        OS: $os
        """
    }
}
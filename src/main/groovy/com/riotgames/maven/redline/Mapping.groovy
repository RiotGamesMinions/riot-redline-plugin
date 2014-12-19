package com.riotgames.maven.redline

import org.redline_rpm.payload.CpioHeader

class Mapping {

    /**
     * The directory where the source will be installed
     */
    def String directory

    def String username

    def String groupname

    def int filemode

    def int dirmode
	
	def boolean config
	
	def boolean noreplace
	

    /**
     * A List of source files that will be installed into the rpm
     */
    def List sources

    public Mapping() {
        this.username = CpioHeader.DEFAULT_USERNAME
        this.groupname = CpioHeader.DEFAULT_GROUP
        this.filemode = CpioHeader.DEFAULT_FILE_PERMISSION
        this.dirmode = CpioHeader.DEFAULT_DIRECTORY_PERMISSION
		this.config = false;
		this.noreplace = false;
    }

    @Override
    def String toString() {
        """
        Directory: $directory
        Username: $username
        Groupname: $groupname
        Filemode: $filemode
        Dirmode: $dirmode
        Sources: $sources
        """
    }
}
package com.riotgames.maven.redline

class Packaging {
	
	/**
	 * The name of the packaging for this rpm
	 */
	def String name
	
	/**
	 * The version of the packaging for this rpm
	 */
	def String version
	
	/**
	 * The release (timestamp) of the packaging for this rpm
	 */
	def String release

	/**
	 * Pretty implementation of the toString method
	 */
	@Override
	def String toString() {
		"""
		Name: $name
		Version: $version
		Release: $release
		"""
	}
}
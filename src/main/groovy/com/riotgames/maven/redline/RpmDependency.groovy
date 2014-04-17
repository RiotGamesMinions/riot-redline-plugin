package com.riotgames.maven.redline

class RpmDependency {

	/**
	 * The name ... FIXME
	 */
	def String name

	/**
	 * The name ... FIXME
	 */
	def String version

	@Override
	def String toString() {
		"""
        Directory: $directory
        Sources: $sources
        """
	}
}
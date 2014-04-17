package com.riotgames.maven.redline

import groovy.io.FileType
import org.codehaus.gmaven.mojo.GroovyMojo
import org.freecompany.redline.Builder
import org.freecompany.redline.header.Architecture
import org.freecompany.redline.header.Header
import org.freecompany.redline.header.Os
import org.apache.maven.project.MavenProjectHelper
import org.apache.maven.project.MavenProject
import org.apache.maven.artifact.Artifact
import org.apache.maven.artifact.DefaultArtifact
import org.apache.maven.artifact.handler.DefaultArtifactHandler
import org.apache.maven.artifact.handler.ArtifactHandler

/**
 * The rpm goal creates an RPM out of a project
 *
 * @goal rpm
 * @phase package
 */
class RedlineMojo extends GroovyMojo {

    /**
     * The group of the rpm
     *
     * @parameter
     * @required
     */
    def String group

    /**
     * The vendor of the rpm
     *
     * @parameter
     * @required
     */
    def String vendor

    /**
     * The license of the rpm
     *
     * @parameter
     * @required
     */
    def String license

    /**
     * The summary of the rpm
     *
     * @parameter
     * @required
     */
    def String summary

    /**
     * The url of the rpm
     *
     * @parameter
     * @required
     */
    def String url

    /**
     * The output destination where the rpm will be created
     *
     * @parameter
     * @required
     */
    def String destination

    /**
     * A nested type where the packaging of this rpm can be defined
     *
     * @parameter
     * @required
     */
    def Packaging packaging

    /**
     * A nested type where the platform of this rpm can be defined
     *
     * @parameter
     */
    def Platform platform

    /**
     * @parameter
     * @required
     */
    def List mappings

    /**
     * @parameter
     */
    def List rpmDependencies

    /**
     * Determines if this rpm shall be attached to the Maven install and deploy phases
     *
     * @parameter
     */
    def boolean attach

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     * @readonly
     */
    def MavenProjectHelper mavenProjectHelper

    /**
     * A postInstallScript to be executed after the installation of an rpm
     * Note: This parameter is optional
     * @parameter
     */
    def String postInstallScript

    public void execute() {

        Builder builder = new Builder()
        builder.group = group
        builder.license = license
        builder.vendor = vendor
        builder.summary = summary

        //Setup the Platform instance and set it on the builder
        setupPlatform()
        def architecture = Architecture.valueOf(platform.architecture)
        def os = Os.valueOf(platform.os)
        builder.setPlatform(architecture, os)

        //Finish setting the general properties of the Builder
        def parsedVersion = parsePackagingVersion(packaging.version)
        def sourcePackage = "${packaging.name}-$parsedVersion-${packaging.release}.src.rpm"
        builder.setPackage(packaging.name, parsedVersion, packaging.release)
        builder.addHeaderEntry(Header.HeaderTag.SOURCERPM, sourcePackage)


        if(postInstallScript != null)
            builder.setPostInstallScript(new File(postInstallScript))

        //Parse the mappings
        parseMappings(builder)

		//Parse the RPM dependencies
		parseRpmDependencies(builder)
		
        //Make sure the destination exists and build the rpm
        def rpmDestination = new File(destination)
        rpmDestination.mkdir()

        //Build the rpm
        String rpmName = builder.build(rpmDestination)
        log.info("Writing rpm to: ${rpmDestination.absolutePath}/$rpmName")

        //Attach the created rpm to the install or deploy phase
        if(attach) {
            ArtifactHandler artifactHandler = new DefaultArtifactHandler()
            artifactHandler.setExtension("rpm")
            Artifact artifact = new DefaultArtifact(project.groupId, project.artifactId, project.version, "", "rpm", "", artifactHandler)
            artifact.setFile(new File("${rpmDestination.absolutePath}/$rpmName"))
            mavenProjectHelper.attachArtifact(this.project, artifact.type, artifact.classifier, artifact.file)
        }
    }

    /**
     * Works on the platform instance to ensure it is configured correctly for usage
     */
    def void setupPlatform() {

        //If no configuration was given, default to NOARCH / LINUX, otherwise make sure the values given are valid
        if (!platform) {
            platform = new Platform()
        }
        else {
            def architectureMatch = Architecture.values().any {it.toString() == platform.architecture}
            if (!architectureMatch) {
                fail("${platform.architecture} was entered and was not a valid value. Please use one of the following: ${Architecture.values()}")
            }
            def osMatch = Os.values().any {it.toString() == platform.os}
            if (!osMatch) {
                fail("${platform.os} was entered and was not a valid value. Please use one of the following: ${Os.values()}")
            }
        }
    }

    /**
     * Parses the version parameter to ensure that there are no additional hyphens
     * in the rpm's version which will be displayed in the rpm's filename. The linux rpm
     * command has had issues in the past with too many hyphens.
     *
     * @param version
     * @return
     */
    def String parsePackagingVersion(String version) {
        if(version.contains("-SNAPSHOT")) {
            log.info("Version contains '-SNAPSHOT.' Removing the extra hyphen.")
            version = version.replaceAll("-SNAPSHOT", "SNAPSHOT")
        }
        return version
    }

    /**
     * Parses the mappings member to begin adding files to the builder parameter
     *
     * @param builder
     * @return
     */
    def parseMappings(Builder builder) {
        mappings.each {mapping ->
            def directoryInRpm = mapping.directory
            if (!directoryInRpm.endsWith("/"))
                directoryInRpm += "/"
            parseMapping(builder, directoryInRpm, mapping)
        }
    }

    /**
     * Iterates the mapping parameter's sources, finding each file or directory
     * inside and adding it to the builder parameter.
     *
     * @param builder
     * @param directoryInRpm
     * @param mapping
     * @return
     */
    def parseMapping(Builder builder, String directoryInRpm, Mapping mapping) {
        mapping.sources.each {source ->
            def sourceFile = new File(source)
            def absoluteRpmPath
            //If a directory was mapped, the entire contents of that tree will be added
            if (sourceFile.isDirectory()) {
                sourceFile.eachFileRecurse(FileType.FILES, {file ->
                    absoluteRpmPath = directoryInRpm + file.name
                    log.info("Adding file ${file.absolutePath} to rpm at path $absoluteRpmPath")
                    builder.addFile(absoluteRpmPath, file)
                })
            }
            //else, only a single file was mapped
            else {
                absoluteRpmPath = directoryInRpm + sourceFile.name
                log.info("Adding file $sourceFile to rpm at path $absoluteRpmPath")
                builder.addFile(absoluteRpmPath, sourceFile)
            }
        }
    }

	/**
	 * Parses the rpmDependencies member to ... FIXME
	 *
	 * @param builder
	 * @return
	 */
	def parseRpmDependencies(Builder builder) {
		rpmDependencies.each {rpmDependency ->
			def name = rpmDependency.name
			def version = rpmDependency.version
			builder.addDependencyMore(name,version)
		}
	}
	
}
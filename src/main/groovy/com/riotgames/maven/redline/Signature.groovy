package com.riotgames.maven.redline

class Signature {

    /**
     * The name of the secring.gpg file which contains the private key
     * used to sign the RPM file
     */
    def String privateKeyRing

    /**
     * The ID of the private key (optional)
     * In case there are more then one private key in the privateKeyRing file
     * one can specify the key to be used for signing by setting this parameter.
     * In case the parameter is omitted, the first (possibly only) private key
     * found in the privateKeyRing file is used to sign the RPM file
     */
    def String privateKeyId

    /**
     * The passphrase of the the private key
     */
    def String privateKeyPassphrase

    /**
     * Pretty implementation of the toString method
     */
    @Override
    def String toString() {
        """
        GPG private keyring file: $privateKeyRing
        Key ID: $privateKeyId
        """
    }
}
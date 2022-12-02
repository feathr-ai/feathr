---
layout: default
title: Developer Guide for publishing to maven
parent: Developer Guides
---

# Developer Guide for publishing to maven

## Manual Publishing
---

### Prerequisites
- Install JDK8, for macOS: 
  `brew tap adoptopenjdk/openjdk
   brew install --cask adoptopenjdk8`
- Install Gradle, for macOS: `brew install gradle`
- Install GPG, for macOS: `brew install gpg`
- Sonatype account credential

### Publishing
1. Set up GPG for signing artifacts.
    * Generate the key, use the name and email you would like to use to identify who uploaded the artifacts. An example email address would be your github email, and name could be `Feathr Developer`, `Feathr Contributor`, your real name, etc.
        *   ```
            $ gpg --gen-key
            ...
            Real name: Central Repo Test
            Email address: central@example.com
            You selected this USER-ID:
                "Central Repo Test <central@example.com>"
            Change (N)ame, (E)mail, or (O)kay/(Q)uit? O
            ```
     * Save key passphrase, which is needed during the gradle publishSigned step
     * Verify your gpg metadata, and note the uid. In this example it is `CA925CD6C9E8D064FF05B4728190C4130ABA0F98`
        *   ```
            $ gpg --list-keys
            /home/mylocaluser/.gnupg/pubring.kbx
            ...
            pub   rsa3072 2021-06-23 [SC] [expires: 2023-06-23]
                CA925CD6C9E8D064FF05B4728190C4130ABA0F98
            uid           [ultimate] Central Repo Test <central@example.com>
            sub   rsa3072 2021-06-23 [E] [expires: 2023-06-23]
            ```
    * Upload gpg keys to a key server
        * [Recommended] Upload manually
            * Run the following command to generate the ASCII-armored public key needed by the key server. Replaced the {uid} with the uid noted from the earlier step.
                *   ```
                    gpg --armor --export {uid} > pubkey.asc
                    ```
            * upload to http://keyserver.ubuntu.com/ via `submit key`

        * Upload via command line. Currently this hasn't succeeded, if succeeded, please alter the steps here with your fix.
            * ```
                $ gpg --keyserver keyserver.ubuntu.com --recv-keys CA925CD6C9E8D064FF05B4728190C4130ABA0F98
                ```
        * Export your keyring file to somewhere on your disk (not to be checked in).
          * ```
              $ gpg --export-secret-keys --armor <key id> <path to secring.gpg>
              ```
---

2.  Set up `Sonatype` credentials    
    * Get account details to login to https://oss.sonatype.org/. Reachout to Feathr team, such as @jaymo001, @hangfei or @blrchen
      * Setup the credentials locally
                  ```
          * Paste the following with the sonatype credentials to your gradle.properties file
              *   ```
                  signing.keyId=<Last 8 characters from your gpg key>
                  signing.password=<Password used to created your gpg key>
                  signing.secretKeyRingFile=<path to secring.gpg>
                  mavenCentralUsername=<sonatype user name>
                  mavenCentralPassword=<sonatype password>

                  ```
---
3. Increase version number in gradle.properties and build.gradle files, and replace the version number with the next version number.
    *   ```
        version="0.6.0"
        ```
4. Publish to sonatype/maven via gradle
    * In your feathr directory, clear your cache to prevent stale errors
        *   ```
            rm -rf build/
            ```
    * Execute command in your terminal to publish to sonatype staging
        * ```
            ./gradlew publish -Dorg.gradle.java.home=<jdk/path>
            ```
    * Execute command in your terminal release the staged artifact into central maven.
        * ```
             ./gradlew closeAndReleaseRepository -Dorg.gradle.java.home=<jdk/path>
    * To publish to local maven, execute the below command
      * ```
           ./gradlew publishToMavenLocal -Dorg.gradle.java.home=<jdk/path>
           ```
---

5. Upon release, new version will be published to Central: this typically occurs within 30 minutes, though updates to search can take up to 24 hours. See the [Sonatype documentation](https://central.sonatype.org/publish/publish-guide/#releasing-to-central) for more information.

---

6. After new version is released via Maven, use the released version to run a test to ensure it actually works. You can do this by running a codebase that imports Feathr scala code.

## Troubleshooting
- If you get something like `[error] gpg: signing failed: Inappropriate ioctl for device`, run `export GPG_TTY=$(tty)` in your terminal and restart console.
- If the published jar fails to run in Spark with error `java.lang.UnsupportedClassVersionError: com/feathr-ai/feathr/common/exception/FeathrInputDataException has been compiled by a more recent version of the Java Runtime (class file version 62.0), this version of the Java Runtime only recognizes class file versions up to 52.0`, 
  make sure you complied with the right Java version with -Dorg.gradle.java.home parameter in your console.

## CI Automatic Publishing
There is a Github Action that automates the above process, you can find it [here](../../.github/workflows/publish-to-maven.yml). This action is triggered anytime a new tag is created, which is usually for release purposes. To manually trigger the pipeline for testing purposes tag can be created using following commands

```bash

git tag -a <version> -m "Test tag"
git push --tags

```

Following are some of the things to keep in mind while attempting to do something similar, since signing issues are hard to debug.

1. There are four secrets that needs to be set for the Github workflow action to work
    ```bash
    PGP_PASSPHRASE: This is the passphrase that you provided during GPG key pair creation.
    PGP_SECRET: The Private Key from GPG key pair created above.
    SONATYPE_PASSWORD: Password for oss sonatype repository.
    SONATYPE_USERNAME: Username for oss sonatype repository.
    ```
    
1. As noted in previous steps, you need to use gpg to create a public-private key pair on your dev machine. The public key is uploaded to a Key server for verification purpose. The private gpg key is used to sign the package being uploaded to maven. We export this private key to be used for signing on Github agent using the following command

    ```bash

    gpg --export-secret-keys --armor YOUR_PRIVATE_KEY_ID > privatekey.asc
    ```
    Copy everything from the privatekey.asc file and put it as Github secret with name PGP_SECRET
    
    To get the private key id you can run the following command and use id under section sec (stands for secret)

    ```bash
    $ gpg --list-secret-keys
    /Users/myuser/.gnupg/pubring.kbx
    -------------------------------

    sec   abc123 2022-08-24 [SC] [expires: 2024-08-23]
        3203203SD.......  
    uid           [ultimate] YOUR NAME <YOUR_EMAIL>
    ssb   abc123 2022-08-24 [E] [expires: 2024-08-23]
    ```
1. Make sure you are using the right credential host in [build.gradle](../../build.gradle)
    - For accounts created before Feb 2021 use __oss.sonatype.org__ 
    - For accounts created after Feb 2021 use __s01.oss.sonatype.org__
    ```
### References

- https://github.com/johnsonlee/sonatype-publish-plugin

- https://www.linuxbabe.com/security/a-practical-guide-to-gpg-part-1-generate-your-keypair

- https://central.sonatype.org/publish/publish-guide/#deployment

- https://blog.sonatype.com/new-sonatype-scan-gradle-plugin

- https://docs.github.com/en/actions/automating-builds-and-tests/building-and-testing-java-with-gradle

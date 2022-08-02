---
layout: default
title: Developer Guide for publishing to maven
parent: Developer Guides
---

# Developer Guide for publishing to maven

## Manual Publishing
---

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
     * Verify your gpg metadata, and not the uid. In this example it is `CA925CD6C9E8D064FF05B4728190C4130ABA0F98`
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
            * Run the following command to generated the ASCII-armored public key needed by the key server. Replaced the {uid} with the uid noted from the earlier step.
                *   ```
                    gpg --armor --export {uid} > pubkey.asc
                    ```
            * upload to http://keyserver.ubuntu.com/ via `submit key`

        * Upload via command line. Currently this hasn't succeeded, if succeeded, please alter the steps here with your fix.
            *   ```
                $ gpg --keyserver keyserver.ubuntu.com --recv-keys CA925CD6C9E8D064FF05B4728190C4130ABA0F98
                ```
---

2.  Set up `Sonatype` credentials
    * Get account details to login to https://oss.sonatype.org/. Reachout to feathr team, such as @jaymo001 or @hangfei
    * Setup the credentials locally
        * Create sonatype configuration file
            *   ```
                vim $HOME/.sbt/1.0/sonatype.sbt
                ```
        * Paste the following with the sonatype credentials
            *   ```
                credentials += Credentials("Sonatype Nexus Repository Manager",
                        "oss.sonatype.org",
                        "(Sonatype user name)",
                        "(Sonatype password)")
                ```
---

3. Publish to sonatype/maven via sbt
    * In your feathr directory, clear your cache to prevent stale errors
        *   ```
            rm -rf target/sonatype-staging/
            ```
    * Start sbt console by running
        *   ```
            sbt
            ```
        * if experiencing java issues try setting the java version like so:
            *   ```
                sbt -java-home /Library/Java/JavaVirtualMachines/jdk1.8.0_282-msft.jdk/Contents/Home
                ```
    * Execute command in sbt console to publish to maven
        *   ```
            reload
            ; publishSigned; sonatypeBundleRelease
            ```
---

4. "Upon release, your component will be published to Central: this typically occurs within 30 minutes, though updates to search can take up to four hours."
https://central.sonatype.org/publish/publish-guide/#releasing-to-central

---

5. After new version is released via Maven, use the released version to run a test to ensure it actually works. You can do this by running a codebase that imports Feathr scala code.

## CI Automatic Publishing

(TBD)

### References

https://github.com/xerial/sbt-sonatype

https://www.linuxbabe.com/security/a-practical-guide-to-gpg-part-1-generate-your-keypair

https://central.sonatype.org/publish/publish-guide/#deployment

https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html

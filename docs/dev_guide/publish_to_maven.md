---
layout: default
title: Developer Guide for publishing to maven
parent: Feathr Developer Guides
---
# Developer Guide for publishing to maven

## Manual Publishing

1. Get account details to login to https://oss.sonatype.org/
2. Install GPG, setup keys, and export to a key server
```
$ gpg --gen-key
...
Real name: Central Repo Test
Email address: central@example.com
You selected this USER-ID:
    "Central Repo Test <central@example.com>"

Change (N)ame, (E)mail, or (O)kay/(Q)uit? O
...
$ gpg --list-keys
/home/mylocaluser/.gnupg/pubring.kbx
---------------------------------
pub   rsa3072 2021-06-23 [SC] [expires: 2023-06-23]
      CA925CD6C9E8D064FF05B4728190C4130ABA0F98
uid           [ultimate] Central Repo Test <central@example.com>
sub   rsa3072 2021-06-23 [E] [expires: 2023-06-23]
$ gpg --keyserver keyserver.ubuntu.com --recv-keys CA925CD6C9E8D064FF05B4728190C4130ABA0F98
```

if failing to programmatically export to key server, you can export it manually and upload to http://keyserver.ubuntu.com/ via `submit key`

run the following command to generated the ASCII-armored public key needed by the key server
```
gpg --armor --export user-id > pubkey.asc
```
https://www.linuxbabe.com/security/a-practical-guide-to-gpg-part-1-generate-your-keypair

3. Setup your credentials locally at `$HOME/.sbt/0.13/sonatype.sbt`
```
credentials += Credentials("Sonatype Nexus Repository Manager",
        "oss.sonatype.org",
        "(Sonatype user name)",
        "(Sonatype password)")
```
(ref, https://github.com/xerial/sbt-sonatype)

4. Start sbt console by running
```
sbt -java-home /Library/Java/JavaVirtualMachines/jdk1.8.0_282-msft.jdk/Contents/Home
```

5. Execute command in sbt console to publish to maven
```
 ; publishSigned; sonatypeBundleRelease
```


6. "Upon release, your component will be published to Central: this typically occurs within 30 minutes, though updates to search can take up to four hours."
https://central.sonatype.org/publish/publish-guide/#releasing-to-central


## CI Automatic Publishing

(TBD)

### References



https://central.sonatype.org/publish/publish-guide/#deployment

https://www.scala-sbt.org/1.x/docs/Using-Sonatype.html


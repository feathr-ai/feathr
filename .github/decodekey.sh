#!/bin/bash
gpg --version
echo "Hello, this is decode file"
GPG_TTY=$(tty)
GPG_OPTIONS="--no-show-photos --pinentry-mode loopback"
echo $PGP_SECRET | base64 --decode | gpg  --import
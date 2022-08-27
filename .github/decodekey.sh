#!/bin/bash

echo $PGP_SECRET | base64 --decode | gpg  --batch --import
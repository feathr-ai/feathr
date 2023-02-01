#!/bin/sh

FUNC_SRC="https://github.com/feathr-ai/feathr-online/raw/main/piper/src/pipeline/function/mod.rs"
AGG_FUNC_SRC="https://github.com/feathr-ai/feathr-online/blob/main/piper/src/pipeline/aggregation/mod.rs"

echo "SUPPORTED_FUNCTIONS = [" > functions.py
curl -Lq $FUNC_SRC | grep -v '^\s*//' | grep '^\s*function_map.insert' | cut -d '"' -f2 | sed -e 's/\(.*\)/"\1",/' > /tmp/functions.txt
curl -Lq $AGG_FUNC_SRC | grep -v '^\s*//' | grep '^\s*agg.insert' | cut -d '"' -f2 | sed -e 's/\(.*\)/"\1",/' >> /tmp/functions.txt
cat /tmp/functions.txt | sort | uniq >> functions.py
echo "]" >> functions.py
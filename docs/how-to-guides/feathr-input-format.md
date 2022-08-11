---
layout: default
title: Input File Format for Feathr
parent: How-to Guides
---

# Input File Format for Feathr

Feathr supports multiple file formats, including Parquet, ORC, Avro, JSON, Delta Lake, and CSV. The format are recognized in the following order:

1. If the input path has a suffix, that will be honored. For example, `wasb://demodata@demodata/user_profile.csv` will be recognized as csv, while `wasb://demodata@demodata/product_id.parquet` will be recognized as parquet. Note that this is a per file behavior.
2. If the input file doesn't have a name, say `wasb://demodata@demodata/user_click_stream`, users can optionally set a parameter to let Feathr know which format to read those files. Refer to the `spark.feathr.inputFormat` setting in [Feathr Job Configuration](./feathr-configuration-and-env.md) for more details on how to set those. Note that this is a global setting that will apply to every input which the format isn't recognized.
3. If all the above conditions are not recognized, Feathr will use `avro` as the default format.

Special note for spark outputs:

![Spark Output](../images/spark-output.png)
#!/bin/sh

spark-submit \
        --master local[*] \
        --name $jobName \
        --packages $packages \
        --conf "spark.driver.extraClassPath=../target/scala-2.12/classes:jars/config-1.3.4.jar:jars/jackson-dataformat-hocon-1.1.0.jar:jars/jackson-module-caseclass_2.12-1.1.1.jar:jars/mvel2-2.2.8.Final.jar:jars/fastutil-8.1.1.jar" \
        --conf "spark.hadoop.fs.wasbs.impl=org.apache.hadoop.fs.azure.NativeAzureFileSystem" \
        --class com.linkedin.feathr.offline.job.FeatureJoinJob \
        $mainJar \
        --join-config $joinConfig \
        --input $input \
        --output $output \
        --feature-config $featureConfig\
        --num-parts $numParts \
---
layout: default
title: Lookup Features
parent: Feathr How-to Guides
---

The syntax contains the following parts:-
key - This is the key identifier for the sequential join feature. This should be a column/columns in the observation data. While computing multi-level sequential join, we store theirthere columns to retrieve the sequential join feature value.
base -
key - Key identifier of the base feature. This should match the number of keys in the base feature definition (anchor or derivation). Also, this should be a subset of the sequential join key.
Feature - Name of the base feature. This can be a derived feature or an anchored feature.
outputKey - Name of the output key column. This is made optional in offline, though it is mandatory in online. Currently, in offline we only support a single output key - this should be the name of the feature value column of the base feature. Future work is to support multiple output keys which would require support of user defined class.
transformation - mvel transformation function applied on the base feature value/output key column.
3. expansion - 
key - key identifier of the expansion feature. This should match the number of keys in the expansion feature definition (anchor or derivation). One of the keys here should belong to the output key/feature value column of the base. The remaining key columns, if any, must come from the seq join key.
feature - name of the expansion feature. Can be derived/anchored feature.
4. aggregation - This is the aggregation function to be applied on the final result. In offline, we always group by using the entire observation data. This is because we have a contract that we should never exceed/reduce the number of rows from our observation data.


Till date, we have been computing sequential join feature like any other derived feature. The existing strategy for other derived features is to join all the constituent features of the derived feature to the observation dataframe using the derived feature key.


Here is order of computation:-
Join base feature with observation, using base feature join key.
Join expansion feature with (observation+base) using the output key/feature value key, during sequential join stage.
Remove the base and expansion feature columns.

Aggregation 

After the final computation of expansion feature, we might want to apply some aggregation function to the results. For this aggregation purpose, we are always going to group by the entire observation dataframe. This is because we do not want to reduce the number of rows from our original observation dataframe.

We support all the aggregation functions available in online, ie - sum, union, min, max, elementwise-min, elementwise-max. We also support elementwise-avg which is not supported in online.

Apart from these, we also support custom class aggregation (Recruiter search had put up this request). In frame common, we have created an abstract class called “SeqJoinCustomAggregation”. In the method, “applyAggregation” the user can write their custom aggregation function on top of the final results. This functionality is not available in online.

Here is the summary on the aggregation and associated feature values.
Feature Type
Supported Aggregation
NUMERIC
SUM, AVG, MAX,ELEMENTWISE_MAX, ELEMENTWIZE_MIN
CATEGORICAL
UNION
DENSE_VECTOR
UNION,ELEMENTWISE_MAX, EKEMENTWIZE_MIN


TERM_VECTOR
UNION,ELEMENTWISE_MAX, EKEMENTWIZE_MIN
CATEGORICAL_SET
UNION


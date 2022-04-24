---
layout: default
title: Feathr Expression Language
parent: Feathr How-to Guides
---

# Feathr Expression Language

> **Do not use expressions not listed here. Expressions not listed here will not be supported.**

# What's Feathr Expression Language

Feathr expression language is used to provide common data transformations along with other Feathr APIs for feature engineering. It can be used in feature transformation, entity key transformation and some other places. Feathr expression language is a SQL dialect that provides a subset of SQL functionalities with some Feathr built-in UDFs.

## When Not to Use Feathr Expression Language

If the feature transformation can't be accomplished with a short line of expression, then you should not use Feathr expression language. You should contact us to see if it can be supported via UDFs(see later section). If not(this is pretty rare), you should do some preprocessing via other tools first.

# Usage Guide

Your data transformation can be composed of one or a few smaller tasks. Divide and conquer! For each individual task, check the following sections on how to acheive them. Then combine them. For example, we have a trip mileage column but it's in string form. We want to compare if it's a long trip(> 30 miles). So we need to cast it into string and then compare with 30. We can do `cast_double(mile_column) > 30`.

## Field accessing

If your data is in nested record, then you can access them via `a.b` syntax.

For example, my data schema is like this:

```
{
  user : name
}
```

then I can access it via `user.name`.

## Type Cast

You can cast your data to the desired type with our cast functions:

- `cast_double(input)`: cast to double
- `cast_float(input)`: cast to float
- `cast_int(input)`: cast to int

## Concatenate String

You can concatenate string with `concat(str1, str2)`. For exmample, `concat("apple", "orage")` produces appleorange.

## Arithmetic Operations

For data of numeric types, you can use arithmetic operators to perform opterations. Here are the supported operators: `+,-,*,/`

## Logical Operators

> If the logical operator you need is not here, please raise a github issue with us.

Logical operators combines multiple true and false and returns respective true or false. We support three logical operators(here x and y are two expression):

- `and(x, y)`: returns true when both expressions are true
- `or(x, y)`: returns true when either expressions are true
- `not(x)`: negation of x

## Equals and not Equals

- `==`: equals
- `!=`: not equals

## Check null or empty string

- `isnull`: return true when it's null
- `isnotnull`: return false when it's null
- `input == ''`: return true if input is empty string
- `input != ''`: return true if input is not empty string

## Ternary Operator

`if_else(exp, a, b)` returns a if exp is true else return b.

# Feathr Built-in UDFs

> If the UDFs you need is not here, please raise an github issue with us.

Feathr built-in UDFs(user-defined-function) provide useful feature transformation or feature engineering functionalities that might not be easy to achieve with existing expression.

| UDF name                        | UDF description                                            | Example                                                                           |
| ------------------------------- | ---------------------------------------------------------- | --------------------------------------------------------------------------------- |
| dayofweek(input)                | get day of the week from the input                         | dayofweek('2009-07-30') returns 5                                                 |
| dayofmonth(input)               | get day of the month from the input                        | dayofmonth('2009-07-30') returns 30                                               |
| hourofday(input)                | get hour of the day from the input                         | hourofday('2021-01-01 23:45:57') returns 23                                       |
| time_duration(start, end, unit) | get the duration in the desired unit between start and end | time_duration('2021-01-01 23:45:57', '2021-01-01 23:55:57', 'minutes') returns 10 |

# How Feathr Expressions Works Under the Hood

Feathr expressions relies on [Mvel](http://mvel.documentnode.com/) and Spark SQL as the underlying engine. The supported
functionalities are a subset of Mvel and Spark SQL to ensure cross-platform compatibility. The underlying engine may be
updated or replaced in the future but the supported operators in this doc will stay. If you are using some operators
that are not mentioned here, it might work in one platform but might not work on others.

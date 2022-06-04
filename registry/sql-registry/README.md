# SQL-Based Registry for Feathr

This is the reference implementation of [the Feathr API spec](./api-spec.md), base on SQL databases instead of PurView.

Please note that this implementation uses iterations of `select` to retrieve graph lineages, this approach is very inefficient and should **not** be considered as production-ready. We only suggest to use this implementation for testing/researching purposes.
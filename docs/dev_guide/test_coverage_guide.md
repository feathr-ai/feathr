---
layout: default
title: Feathr Scala Project Developer Guide
parent: Developer Guides
---

# Feathr Test Coverage Check Guide

## Background
To maintain and improve codes quality of feathr, we expect test coverage ratio to be equal to or above 90% in general. For any code changes, please make sure related test cases were added and check test coverage can meet our requests.

## How to conduct test coverage
1. Through github workflows pipeline:
   We already added this coverage checking into our CI pipeline. For each pull request, push and scheduled jobs, github will check the coverage when runing 'pytest' automatically. You can find the result for 'azure_synapse', 'databricks' and 'local spark', respectively from each PR and commit.

   An example of test coverage result:

2. Test locally:
   We can also check the coverage locally
   
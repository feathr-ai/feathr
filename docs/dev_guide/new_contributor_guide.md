---
layout: default
title: Feathr Developer Contribution Guide
parent: Feathr Developer Guides
---

# What can I contribute?
All forms of contributions are welcome, including and not limited to:
* Improve or contribute new [notebook samples](https://github.com/feathr-ai/feathr/tree/main/docs/samples)
* Add tutorial, blog posts, tech talks etc
* Increase media coverage and exposure
* Improve user-facing documentation or developer-facing documentation
* Add testing code
* Add new features
* Refactor and improve architecture
* For any other forms of contribution and collaboration, don't hesitate to reach out to us.

# I am interested, how can I start?
If you are new to this project, we recommend start with [`good-first-issue`](https://github.com/feathr-ai/feathr/issues?q=is%3Aissue+is%3Aopen+label%3A%22good+first+issue%22).

The issues are also labled with what types of programming language the task need.
* [`good-first-issue` and `Python`](https://github.com/feathr-ai/feathr/issues?q=is%3Aopen+label%3A%22good+first+issue%22+label%3Apython)
* [`good-first-issue` and `Scala`](https://github.com/feathr-ai/feathr/issues?q=is%3Aopen+label%3A%22good+first+issue%22+label%3Ascala)
* [`good-first-issue` and `Java`](https://github.com/feathr-ai/feathr/issues?q=is%3Aopen+label%3A%22good+first+issue%22+label%3Ajava)

If you are familiar with this project, you can just start to pick up our github issues.

## I am not familiar with XYZ, can I still contribute?
If you are willing to learn, no problem at all!

You can ping your questions in the community Slack channel and Feathr developers will give you more guidance. For example:
* "I am not familiar with Scala, but I want to start to contribute to Scala code. Can someone give me some guidance?"
* "I am not sure how to setup the cluster and test my code. Can someone help me out?"

## My pull request(PR) requires testing against the database or cluster that I dont' have, how can I test?
Develop your implementation locally first and use unit tests to ensure correctness. Later, you can ask PR reviewers to label them with `safe to test` so Github will kick off a integration test in our test cluster with your code.

If you need more assistance regarding testing your code or development, reach out in our Slack channel.

## Technical Architecture
Feathr contains multiple components so you can learn and contribute to various things:
* Python Feathr Client
  * This is the client users use to interact with most of our API. Mostly written in Python.
* Computation Engine
  * The computation engine that execute the actual feature join and generation work. Mostly in Scala and Spark.
* Feature Registry API Layer
  * The storage layer supports SQL, Purview(Atlas).
  * The API layer is in Python(FAST API)
* Feature Registry Web UI layer
  * The Web UI for feature registry. Written in React with a few UI frameworks.

# Developer Guide
See [`feathr/docs/dev_guide`](./)

---
layout: default
title: Feathr Developer Contribution Guide
parent: Feathr Developer Guides
---

# What can I contribute?
All forms of contributions are welcome, including and not limited to:
* Improve or contribute new [notebook samples](https://github.com/linkedin/feathr/tree/main/feathr_project/feathrcli/data/feathr_user_workspace)
* Add tutorial, blog posts, tech talks etc
* Improve user-facing documentation or developer-facing documentation
* Add testing code 
* Add new features
* Refactor and improve architecture

# I am interested, how can I start?

## I am not familiar with XYZ, can I still contribute?
If you are willing to learn, no problem at all!

You can ping your questions in the community Slack channel and Feathr developers will give you more guidance. For example:
* "I am not familiar with Scala, but I want to start to contirbute to Scala code. Can someone give me some guidance?"
* "I am not sure how to setup the cluster and test my code. Can someone help me out?"

## My pull request(PR) requires testing against the database or cluster that I dont' have, how can i fix this?

## Technical Architecutre
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
See (`feathr/docs/dev_guide`)[../]

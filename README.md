# Microservices school project

Microservices school project for the course *Backend programming in Java and Spring Boot*.

## About

This repository contains microservices.
The default (and currently only) service is running on Google App Engine Standard with Java17 runtime.

**Default** is written using String Boot and use Googles Datastore as data backend.

Authentication is achieven using an encryptioon challenge and authorization uses JWT bearer tokens.

## Deployment info

**Default** is deployed to Google App Engine using gcloud like so:

```console
cd default
gcloud app deploy --project <google project id>
```

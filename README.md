# Microservices school project

Microservices school project for the course *Backend programming in Java and Spring Boot*.

## About

This repository contains two microservices. One for authentication and one for public transport route information. The services are running on Google App Engine Standard with Java17 runtime.

Both services are written using String Boot and use Googles Datastore as data backend.

Authentication is achieven using JWT and authorization uses tokens.

## Deployment info

The project is deployed to a Google Cloud project.

Deploying to Google App Engine using gcloud:

```console
cd <microservice path>
gcloud app deploy --project <google project id>
```

Currently deployable services:

* auth - Auithentication service
* pubtrans - Public transportation service

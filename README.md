# My Google Cloud microservices project

My Google Cloud microservice for various school related project.


## Prequisites

Install Googles Clout SDK

## Deploy

Create a Google Cloud project in [Google Console](https://console.cloud.google.com).

Enable App Engine and Datastore (datastore mode).

Deploy to Google Standard Environment using gcloud.

```console
cd <root>/<microservice>
gcloud app deploy --project <project-id>
```

Currently deployable services:

* default - Static page.
* foo - Spring Boot Rest service with Swagger.

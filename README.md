# webee-server

## Pre-requsites

Create a json credentials file for your Google Account on file

    webeedevices/keys.json


## Local Deploy

    mvn compile
    
    mvn exec:java

## App Engine Deploy

To deloy this application using App Engine, modify file

    app.yml
    
and set within your project id environment variable

    WEBEE_PROJECT_ID
 
Then run

    gcloud app deploy app.yml 
 
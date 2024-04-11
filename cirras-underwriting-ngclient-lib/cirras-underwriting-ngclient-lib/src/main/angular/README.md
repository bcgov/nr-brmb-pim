# CIRRAS Claims Rest API

The version and name in `package.json` is irrelevant, as is the version in the POM.
The only version that matters is the one in `projects/cirras-claims-api/package.json`.
Use the commands detailed below to update it.

## Updating to latest Swagger REST API description

1. Download and replace `src/swagger/swagger.json` with the latest version of the Rest API's swagger.json file.

2. Update the version. Run this command to see current version:

`npm run version` 

Decide if the change to the API is a major, minor, patch or rc (prerelease) change. Run this command:

`npm run version-rc` (or -patch, -minor, -major)

3. Commit the changes to the repo

## Publishing library

These are steps for publishing manually. The Jenkins build does same process.

1. Generate REST API library

`npm run generate`

2. Build Angular library

`npm run build`

3. Publish library

`npm run publish-lib`


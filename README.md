![Voxloud Logo](https://www.voxloud.com/wp-content/uploads/2020/07/voxloud_logo_@1x.png)

# Image service #

## Requirements ##
Implement standalone Spring boot application  which will be responsible for uploading/search user images

### Domain ###
Use in memory H2 database
1. Account - user data, account can have multiple images
2. Image - image metadata (original name, content type, size, reference, etc.), image can belong only to one account 
3. Tag - list of string tags which can be used to search images

Image entity should not contain binary content (value object) in database, but only image metadata. Mock file uploader to 3rd party object storage service, which will return some random reference on uploaded object and store reference in image entity.

Account and image entities should also contain creation and update timestamps.

### API ###
1. CRUD API for accounts/images. API should accept multiple image files. Appropriate http status should be returned in case of non existing resource modification attempt.
2. Agile search API for images which allows search by any of image properties
3. Account is allowed to modify only owned images. Appropriate http status should be returned in case of non owner modification attempt.

### Authentication ###
Use basic authentication for REST API
 
### Tests ###
Controller and service layer should be covered with tests.
 
## Project delivery ##
All the code should be pushed in __**your public Git repository**__(Bitbucket, Github, etc), since you can't push branches on this repository. 
These are the steps:

1. You create a public fork of this project on your Bitbucket account clicking [here](https://bitbucket.org/voxloud/image-service-hw/fork) or you clone it and push to your account if you use other Git platforms (Github, Gitlab, etc)
2. You commit and push to your fork master branch
3. You share the (public) repository link with the reviewer when development is completed


A test task - microblogging app

Built with MVC pattern

* install and start the app with mvn clean install spring-boot:run

* Access the app with http://localhost:8080

Sign in with qa/qa


RELEASE NOTES:

Bugs fixed and functionality added:
* Current user is shown incorrectly - I sign in under “blogger” but it shows “current user is qa” - Fixed by merging frontend and backend together
* the website count is broken - it adds 2 views instead of 1 - Fixed by merging frontend and backend together
* author of the post is showed incorrectly - Fixed 
* I am supposed to see only my posts - my blog - Fixed by creating a method to return only the current user's posts
* empty posts without title and content can be created - Fixed by adding validation
* mvn clean install spring-boot:run builds and starts the app
* delete and edit posts of other users - Fixed by implementing validation in backend and disabling functionality in the frontend
* The logged in user has the ability to see posts of other users, navigating to their feed, that increases their post view count
* Implemented individual post view count
* Password security validation when registering. Cannot register if validation not met



A test task - microblogging app

Built with MVC pattern

* install and start the app with mvn clean install spring-boot:run

* Access the app with http://localhost:8080

Sign in with qa/qa


RELEASE NOTES:

Bugs fixed:
* Current user is shown incorrectly - I sign in under “blogger” but it shows “current user is qa” - Fixed by merging frontend and backend together
* the website count is broken - it adds 2 views instead of 1 - Fixed by merging frontend and backend together
* author of the post is showed incorrectly - Fixed 
* I am supposed to see only my posts - my blog - Fixed by creating a method to return only the current user's posts


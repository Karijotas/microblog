# Microblogging App

This microblogging application was built using the MVC architectural pattern.

### Installation and Startup

To install and start the app, use the following command:

    mvn clean install spring-boot:run

Access the app through your browser at http://localhost:8080

If you want to run the app in Docker, run:
  
    docker build -t microblog .
then

    docker run -dp 127.0.0.1:8080:8080 microblog
then you can access the app with http://127.0.0.1:8080

### Sign In

Use the following credentials to sign in:

* Username: qa
  * Password: qa
* Username: blogger2
  * Password: qa
* Username: user123
  * Password: pass123
* Username: cool_blogger
  * Password: qa
* Username: blogger_x
  * Password: qa

Or after login create your own

### RELEASE NOTES

Bugs fixed and functionality added:

* Current user is shown incorrectly - I sign in under “blogger” but it shows “current user is qa” - Fixed by merging frontend and backend together
* The website count is broken - it adds 2 views instead of 1 - Fixed by merging frontend and backend together
* Author of the post is showed incorrectly - Fixed 
* I am supposed to see only my posts - my blog - Fixed by creating a method to return only the current user's posts
* Empty posts without title and content can be created - Fixed by adding validation
* "mvn clean install spring-boot:run"
  * Builds and starts the whole app in one go
* Delete and edit posts of other users - Fixed by implementing validation in backend and disabling functionality in the frontend
* The logged in user has the ability to see posts of other users, navigating to their feed, that increases their post view count
* Implemented individual post view count
* Password security validation when registering. Cannot register if validation not met
* Increased Blog Post length to allow for lengthy posts
* Implemented comments
* Fixed update view not displaying Post title and body
* Implemented Docker containerization for the app
* Users can delete posts on their own posts, they cannot delete comments in the posts of other users
* Refactored service implementation


![img.png](img.png)
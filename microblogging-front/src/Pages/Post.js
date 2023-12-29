import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";

export function Post() {
    const JSON_HEADERS = {
        "Content-Type": "application/json",
    };

    const [isCreatingPost, setIsCreatingPost] = useState(false);
    const [name, setName] = useState("");
    const [postBody, setPostBody] = useState("");
    const [bloggerId, setCurrentUser] = useState("");


    const handlePostBodyChange = (e) => {
        setPostBody(e.target.value);
    };

    const nameHandler = (e) => {
        setName(e.target.value);
        fetchUser();
    }
    const createPost = () => {
        const postData = {
            name,
            body: postBody,
            bloggerId: parseInt(bloggerId),
        };

        fetch("/post", {
            method: "POST",
            headers: JSON_HEADERS,
            body: JSON.stringify(postData),
        })
            .then(applyPostResult)
            .catch((error) => {
                console.error("Error creating post:", error);
                setIsCreatingPost(false);
            });
    };

    const fetchUser = () => {
        fetch(`/blogger/current-user/id`)
            .then((response) => response.text())
            .then((textResponse) => setCurrentUser(textResponse))
            .catch((error) => {
                console.error("Error fetching current user:", error);
            });
    };
    const applyPostResult = () => {
        setIsCreatingPost(false);
        window.location.href = "/"
    };

    const handleCreatePost = () => {
        setIsCreatingPost(true);
        createPost();
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <Form>
                        <Form.Group controlId="formGroupPostBody">
                            <Form.Label>Title</Form.Label>
                            <Form.Control type="name" placeholder="Enter Title" onChange={(e) => nameHandler(e)} />
                            <Form.Label>Post Body</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={6}  
                                placeholder="Write your post here..."
                                onChange={handlePostBodyChange}
                            />
                        </Form.Group>

                        <Button
                            className="btn-block"
                            variant="primary"
                            disabled={isCreatingPost}
                            onClick={!isCreatingPost ? handleCreatePost : null}
                        >
                            {isCreatingPost ? "Creating Post..." : "Create Post"}
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

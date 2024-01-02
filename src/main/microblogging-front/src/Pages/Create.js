import React, { useState } from "react";
import { Button, Form } from "react-bootstrap";

export function Create() {
    const JSON_HEADERS = {
        "Content-Type": "application/json",
    };

    const [isCreatingPost, setIsCreatingPost] = useState(false);
    const [name, setName] = useState("");
    const [postBody, setPostBody] = useState("");
    const [bloggerId, setCurrentUser] = useState("");
    const [isTitleValid, setIsTitleValid] = useState(true);
    const [isBodyValid, setIsBodyValid] = useState(true);
    const [isBack, setBack] = useState(false);


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
        if (!name.trim()) {
            setIsTitleValid(false);
            return;
        }
        if (!postBody.trim()) {
            setIsBodyValid(false);
            return;
        }
        setIsCreatingPost(true);
        createPost();
    };
    const handleBack = () => {
        window.location.href = "/"
    };

    return (
        <div className="container mt-5">
            <Button
                className="d-flex justify-content-between align-items-center mb-3"
                variant="dark"
                disabled={isBack}
                onClick={!isBack ? handleBack : null}
            >
                {isBack ? 'Loading…' : 'Return to the feed'}
            </Button>
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <Form>
                        <Form.Group controlId="formGroupPostBody">
                            <Form.Label>Title</Form.Label>
                            <Form.Control
                                type="name"
                                placeholder="Enter Title"
                                onChange={(e) => {
                                    nameHandler(e);
                                    setIsTitleValid(true);
                                }}
                                isInvalid={!isTitleValid}
                            />
                            {!isTitleValid && (
                                <Form.Control.Feedback type="invalid">
                                    Title cannot be empty
                                </Form.Control.Feedback>
                            )}
                            <Form.Label>Post Body</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={6}
                                placeholder="Write your post here..."
                                onChange={(e) => {
                                    handlePostBodyChange(e);
                                    setIsBodyValid(true);
                                }}
                                isInvalid={!isBodyValid}
                            />
                            {!isBodyValid && (
                                <Form.Control.Feedback type="invalid">
                                    Post body cannot be empty
                                </Form.Control.Feedback>
                            )}
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

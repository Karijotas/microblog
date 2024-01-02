import React, { useState, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import { useParams } from "react-router-dom";

export function UpdatePost() {
    const params = useParams();
    const [post, setPost] = useState("");
    const [postTitle, setPostTitle] = useState("");
    const [postBody, setPostBody] = useState("");
    const [isTitleValid, setIsTitleValid] = useState(true);
    const [isBodyValid, setIsBodyValid] = useState(true);
    const [isUpdatingPost, setIsUpdatingPost] = useState(false);
    const [isBack, setBack] = useState(false);


    useEffect(() => {
        if (params && params.id) {
            fetch(`/post/${params.id}`)
                .then((response) => response.json())
                .then((post) => {
                    setPost(post);
                    setPostTitle(post.name);
                    setPostBody(post.body);
                })
                .catch((error) => {
                    console.error("Error fetching post details:", error);
                });
        }
    }, [params]);



    const handlePostTitleChange = (e) => {
        setPostTitle(e.target.value);
        setIsTitleValid(true);
        console.log(postTitle);
    };

    const handlePostBodyChange = (e) => {
        setPostBody(e.target.value);
        setIsBodyValid(true);
    };

    const updatePost = () => {
        fetch(`/post/${params.id}`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ name: postTitle, body: postBody }),
        })
            .then((response) => response.json())
            .then((updatedPost) => {
                console.log("Updated Post:", updatedPost);
                setIsUpdatingPost(false);
                handleBack();
            })
            .catch((error) => {
                console.error("Error updating post:", error);
                setIsUpdatingPost(false);
            });
    };

    const handleSubmit = (e) => {
        e.preventDefault();

        if (!postTitle.trim()) {
            setIsTitleValid(false);
            return;
        }

        if (!postBody.trim()) {
            setIsBodyValid(false);
            return;
        }

        setIsUpdatingPost(true);
        updatePost();
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
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formGroupPostTitle">
                    <Form.Label>Updated Post Title</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder={post.name !== null ? post.name : "name"}
                        value={postTitle}
                        onChange={handlePostTitleChange}
                        isInvalid={!isTitleValid}
                    />
                    {!isTitleValid && (
                        <Form.Control.Feedback type="invalid">
                            Post title cannot be empty
                        </Form.Control.Feedback>
                    )}
                </Form.Group>
                <Form.Group controlId="formGroupPostBody">
                    <Form.Label>Updated Post Body</Form.Label>
                    <Form.Control
                        as="textarea"
                        rows={6}
                        placeholder="Write your updated post here..."
                        value={postBody}
                        onChange={handlePostBodyChange}
                        isInvalid={!isBodyValid}
                    />
                    {!isBodyValid && (
                        <Form.Control.Feedback type="invalid">
                            Post body cannot be empty
                        </Form.Control.Feedback>
                    )}
                </Form.Group>
                <Button
                    variant="primary"
                    type="submit"
                    disabled={isUpdatingPost}
                >
                    {isUpdatingPost ? "Updating Post..." : "Update Post"}
                </Button>
            </Form>
        </div>
    );
}


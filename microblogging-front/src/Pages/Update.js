import React, { useState, useEffect } from "react";
import { Button, Form } from "react-bootstrap";
import { useParams } from "react-router-dom";

export function Update() {
    const params = useParams();

    const JSON_HEADERS = {
        "Content-Type": "application/json",
    };

    const [isUpdatingPost, setIsUpdatingPost] = useState(false);
    const [postId, setPostId] = useState(params.id);
    const [post, setPost] = useState([]);

    const [postTitle, setPostTitle] = useState("");
    const [postContent, setPostContent] = useState("");

    
        useEffect(() => {
            fetch("/post/" + params.id)
              .then((response) => response.json())
              .then(setPost);
          },  [params]);




    const handlePostTitleChange = (e) => {
        setPostTitle(e.target.value);
    };

    const handlePostContentChange = (e) => {
        setPostContent(e.target.value);
    };

    const updatePost = () => {
        const updatedData = {
            name: postTitle,
            body: postContent,
        };

        fetch(`/post/${params.id}`, {
            method: "PATCH",
            headers: JSON_HEADERS,
            body: JSON.stringify(updatedData),
        })
            .then(applyUpdateResult)
            .catch((error) => {
                console.error("Error updating post:", error);
                setIsUpdatingPost(false);
            });
    };

    const applyUpdateResult = () => {
        setIsUpdatingPost(false);
        window.location.href = "/";
    };

    const handleUpdatePost = () => {
        setIsUpdatingPost(true);
        updatePost();
    };

    return (
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <Form>
                        <Form.Group controlId="formGroupPostBody">
                            <Form.Label>Title</Form.Label>
                            <Form.Control
                                type="text"
                                value={postTitle}
                                onChange={handlePostTitleChange}
                            />
                            <Form.Label>Post Body</Form.Label>
                            <Form.Control
                                as="textarea"
                                rows={6}
                                value={postContent}
                                onChange={handlePostContentChange}
                            />
                        </Form.Group>
                        <Button
                            className="btn-block"
                            variant="primary"
                            disabled={isUpdatingPost}
                            onClick={!isUpdatingPost ? handleUpdatePost : null}
                        >
                            {isUpdatingPost ? "Updating Post..." : "Update Post"}
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

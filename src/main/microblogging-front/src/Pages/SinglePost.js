import React, { useEffect, useState } from "react";
import { Button, Card, ListGroup } from "react-bootstrap";
import { json, useParams } from "react-router-dom";

export function SinglePost() {
    const params = useParams();
    const [isLogin, setLogin] = useState(false);
    const [isRegister, setRegister] = useState(false);
    const [isBack, setBack] = useState(false);
    const [posts, setPosts] = useState([]);
    const [wordCount, setWordCount] = useState(null);
    const [commonWords, setCommonWords] = useState(new Map());
    const [currentUser, setCurrentUser] = useState("");
    const [postAuthor, setPostAuthor] = useState("");
    const [postTitle, setPostTitle] = useState("");
    const [postBody, setPostBody] = useState("");
    const [postCount, setPostCount] = useState("");
    const [comments, setComments] = useState([]);




    useEffect(() => {
        fetchUser();
    },);

    useEffect(() => {
        if (isRegister) {
            register().then(() => {
                setRegister(false);
            });
        }
    }, [isRegister]);

    useEffect(() => {
        if (isLogin) {
            login().then(() => {
                setLogin(false);
            });
        }
    }, [isLogin]);

    const register = () => {
        return new Promise((resolve) => setTimeout(resolve, 2000));
    };

    const login = () => {
        return new Promise((resolve) => setTimeout(resolve, 2000));
    };

    useEffect(() => {
        if (params && params.id) {
            fetch(`/post/${params.id}`)
                .then((response) => response.json())
                .then((post) => {
                    setPostAuthor(post.blogger.userName);
                    setPostTitle(post.name);
                    setPostBody(post.body);
                    setPostCount(post.count);
                })
                .catch((error) => {
                    console.error("Error fetching post details:", error);
                });
            fetch(`/comment/post/${params.id}`)
                .then((response) => response.json())
                .then((jsonResponse) => setComments(jsonResponse))
                .catch((error) => {
                    console.error("Error fetching comments:", error);
                });
        }
    }, [params]);


    const fetchUser = () => {
        fetch(`/blogger/current-user`)
            .then((response) => response.text())
            .then((textResponse) => setCurrentUser(textResponse))
            .catch((error) => {
                console.error("Error fetching current user:", error);
            });
    };


    const fetchWordCount = (postId) => {
        const postIndex = posts.findIndex((post) => post.id === postId);

        if (postIndex !== -1) {
            fetch(`/post/${postId}/wordcount`)
                .then((response) => response.json())
                .then((wordCount) => {
                    const updatedPosts = [...posts];
                    updatedPosts[postIndex].wordCount = wordCount;
                    setPosts(updatedPosts);
                })
                .catch((error) => {
                    console.error("Error fetching word count:", error);
                });
        }
    };

    const fetchMostUsedWords = (postId, limit) => {
        const postIndex = posts.findIndex((post) => post.id === postId);

        if (postIndex !== -1) {
            fetch(`/post/${postId}/wordcount/${limit}`)
                .then((response) => response.json())
                .then((mostUsedWords) => {
                    const updatedPosts = [...posts];
                    const wordsMap = new Map(Object.entries(mostUsedWords));
                    updatedPosts[postIndex].commonWords = wordsMap;
                    setPosts(updatedPosts);
                })
                .catch((error) => {
                    console.error("Error fetching most used words:", error);
                });
        }
    };

    const handleLogout = () => {
        window.location.href = "http://localhost:8080/logout"
    };
    const handleRegister = () => {
        window.location.href = "#/register"
    };
    const handlePost = () => {
        window.location.href = "#/post"
    };
    const handleBack = () => {
        window.location.href = "/"
    };
    return (

        <main className="text-center">
            <Button
                className="d-flex justify-content-between align-items-center mb-3"
                variant="dark"
                disabled={isBack}
                onClick={!isBack ? handleBack : null}
            >
                {isBack ? 'Loading…' : 'Return to the feed'}
            </Button>
            <h1>BLOG</h1>

            <h6>{currentUser ? "Current user is: " + currentUser : ''}</h6>
            <Button
                className="m-1"
                variant="primary"
                disabled={isRegister}
                onClick={!isRegister ? handleRegister : null}
            >
                {isRegister ? 'Loading…' : 'Register'}
            </Button>

            <Button
                className="m-1"
                variant="primary"
                disabled={isRegister}
                onClick={!isRegister ? handlePost : null}
            >
                {isRegister ? 'Loading…' : 'Create'}
            </Button>

            <Button
                className='ml-2'
                variant="danger"
                disabled={isLogin}
                onClick={!isLogin ? handleLogout : null}
            >
                {isLogin ? 'Loading…' : 'LogOut'}
            </Button>

            <div id="login">
                <Card className="mb-3" >
                    <Card.Header>
                        <h3>
                            {postTitle}
                        </h3>
                        <h6>Author:
                            {postAuthor}

                            ViewCount:
                            {postCount}
                        </h6>
                    </Card.Header>
                    <Card.Body>
                        <Card.Text>
                            {postBody}
                        </Card.Text>
                    </Card.Body><div className="d-flex justify-content-end align-items-center">
                    </div>
                </Card>

                <div id="comments">
                    <h3>Comments</h3>
                    {comments.map((comment) => (
                        <Card className="m-5" key={comment.id}>
                            <Card.Header>Anonymous</Card.Header>
                            <Card.Body>
                                <blockquote className="blockquote mb-0">
                                    <p>
                                        {' '}
                                        {comment.content}{' '}
                                    </p>
                                </blockquote>
                            </Card.Body>
                        </Card>
                    ))}
                </div>
            </div>
        </main>
    );
}

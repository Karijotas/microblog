import React, { useEffect, useState } from "react";
import { Button, Card, ListGroup } from "react-bootstrap";
import { Link, json } from "react-router-dom";
import { Counter } from "./Counter";

export function Feed() {
    const [isLogin, setLogin] = useState(false);
    const [isRegister, setRegister] = useState(false);
    const [posts, setPosts] = useState([]);
    const [wordCount, setWordCount] = useState(null);
    const [commonWords, setCommonWords] = useState(new Map());
    const [currentUser, setCurrentUser] = useState("");
    const [others, setOthers] = useState([]);
    const [comments, setComments] = useState(null);


    useEffect(() => {
        fetchPost();
        fetchUser();
        fetchOtherUsers();
    }, []);

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

    const fetchPost = () => {
        fetch(`/post/user`)
            .then((response) => response.json())
            .then((jsonResponse) => {
                const updatedPosts = jsonResponse.map((post) => ({
                    ...post,
                    wordCount: null,
                    commonWords: new Map(),
                }));
                setPosts(updatedPosts);
            });
    };

    const fetchUser = () => {
        fetch(`/blogger/current-user`)
            .then((response) => response.text())
            .then((textResponse) => setCurrentUser(textResponse))
            .catch((error) => {
                console.error("Error fetching current user:", error);
            });
    };

    const fetchOtherUsers = () => {
        fetch(`/blogger/others`)
            .then((response) => response.json())
            .then((textResponse) => setOthers(textResponse))
            .catch((error) => {
                console.error("Error fetching current user:", error);
            });
    };

    const remove = (id) => {
        fetch(`/post/${id}`, {
            method: "DELETE",
        })
            .then(fetchPost)
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

    const fetchCommentCount = (id) => {
        fetch(`/comment/count/` + id)
            .then((response) => response.text())
            .then((textResponse) => setComments(textResponse))
            .catch((error) => {
                console.error("Error fetching commentCount:", error);
            });
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
    return (
        <main className="text-center">

            <h1>BLOG</h1>

            <h6>{currentUser ? "Current user is: " + currentUser : ''}</h6>
            <div className="d-flex flex-column align-items-start">
                <ListGroup className="w-20">
                    {others.map((other) => (
                        <ListGroup.Item
                            key={other.id}
                            action
                            as={Link}
                            to={`/user/${other.id}`}
                            variant="primary"
                        >
                            {other.userName}
                        </ListGroup.Item>
                    ))}
                </ListGroup>
            </div>
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
                {posts.map((post) => (
                    <Card className="mb-3" key={post.id}>
                        <Card.Header>
                            <h3>{post.name}</h3>
                            <h6>Author: {post.blogger.userName} ViewCount: {post.count}</h6>
                        </Card.Header>
                        <Card.Body>
                            <Card.Text>{post.body}</Card.Text>
                        </Card.Body><div className="d-flex justify-content-end align-items-center">
                            <Button className="m-1" size="sm" variant="dark" href={`#/update/${post.id}`}>Update</Button>
                            <Button className="m-1" size="sm" variant="danger" onClick={() => remove(post.id)}>Delete</Button>
                        </div>
                        <Card.Footer className="d-flex justify-content-between align-items-center">
                            <Button
                                className="m-1"
                                variant="dark"
                                disabled={post.wordCount !== null}
                                onClick={() => fetchWordCount(post.id)}
                            >
                                {post.wordCount !== null ? post.wordCount : 'Word count'}
                            </Button>
                            <Button
                                className="m-1"
                                variant="dark"
                                disabled={comments !== null}
                                onClick={() => fetchCommentCount(post.id)}
                            >
                                {comments === null ? "Comment count" : comments + ' Comments'}
                            </Button>
                            <Button
                                className="m-1"
                                variant="dark"
                                disabled={comments !== null}
                                onClick={() => fetchCommentCount(post.id)}
                            >
                                {comments === null ? "See comments" : comments + ' Comments'}
                            </Button>
                            <div className="m-1">
                                {post.commonWords.size === 0 ? (
                                    <Button
                                        variant="dark"
                                        disabled={post.commonWords.size !== 0}
                                        onClick={() => fetchMostUsedWords(post.id, 5)}
                                    >
                                        Most common words
                                    </Button>
                                ) : (
                                    <ListGroup horizontal>
                                        {Array.from(post.commonWords.entries()).map(([count, word]) => (
                                            <ListGroup.Item key={count}>
                                                {word}: {count}
                                            </ListGroup.Item>
                                        ))}
                                    </ListGroup>
                                )}
                            </div>
                        </Card.Footer>


                    </Card>
                ))}
            </div>
        </main>
    );
}

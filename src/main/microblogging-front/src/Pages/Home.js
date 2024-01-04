import React, { useEffect, useState } from "react";
import { Card, ListGroup } from "react-bootstrap";


export function Feed() {
    const [isLogin, setLogin] = useState(false);
    const [isRegister, setRegister] = useState(false);
    const [posts, setPosts] = useState([]);
    const [wordCount, setWordCount] = useState(null);
    const [commonWords, setCommonWords] = useState(new Map());
    const [currentUser, setCurrentUser] = useState("");
    const [others, setOthers] = useState([]);
    const [comments, setComments] = useState(null);
    const [linkComments, setLinkComments] = useState(false);
    const [displayedPostId, setDisplayedPostId] = useState(null);


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
        setDisplayedPostId(id);
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
    const handleComments = (id) => {
        setLinkComments(true);
        window.location.href = `#/comment/${id}`
    };
    return (
        <main className="text-center">

            <h1>BLOG</h1>

            <h6>{currentUser ? "Current user is: " + currentUser : ''}</h6>
            <div className="d-flex flex-column align-items-start">
                {others.map((other) => (<div>
                    <a href={`#/user/${other.id}`}>{other.userName}</a>
                </div>

                ))}
            </div>
            <button
                disabled={isRegister}
                onClick={!isRegister ? handleRegister : null}
            >
                {isRegister ? 'Loading…' : 'Register'}
            </button>

            <button
                disabled={isRegister}
                onClick={!isRegister ? handlePost : null}
            >
                {isRegister ? 'Loading…' : 'Create'}
            </button>

            <button
                disabled={isLogin}
                onClick={!isLogin ? handleLogout : null}
            >
                {isLogin ? 'Loading…' : 'LogOut'}
            </button>

            <div id="login">
                {posts.map((post) => (
                    <article className="mb-3" key={post.id}>
                        <h3>{post.name}</h3>
                        <h6>Author: {post.blogger.userName} ViewCount: {post.count}</h6>
                        <main>
                            <p>{post.body}</p>
                        </main><div className="d-flex justify-content-end align-items-center">
                            <button href={`#/update/${post.id}`}>Update</button>
                            <button onClick={() => remove(post.id)}>Delete</button>
                        </div>
                        <footer>
                            <button
                                disabled={post.wordCount !== null}
                                onClick={() => fetchWordCount(post.id)}
                            >
                                {post.wordCount !== null ? post.wordCount : 'Word count'}
                            </button>
                            <button
                                disabled={displayedPostId === post.id} // Disable if it's the displayed post
                                onClick={() => fetchCommentCount(post.id)}
                            >
                                {displayedPostId === post.id
                                    ? comments === null
                                        ? 'Comment count'
                                        : comments + ' Comments'
                                    : 'Comment count'}
                            </button>
                            <button
                                disabled={linkComments !== false}
                                onClick={() => handleComments(post.id)}
                            >
                                {linkComments == false ? "See comments" : handleComments(post.id)}
                            </button>
                            <div>
                                {post.commonWords.size === 0 ? (
                                    <button
                                        disabled={post.commonWords.size !== 0}
                                        onClick={() => fetchMostUsedWords(post.id, 5)}
                                    >
                                        Most common words
                                    </button>
                                ) : (
                                    <ul horizontal>
                                        {Array.from(post.commonWords.entries()).map(([count, word]) => (
                                            <li key={count}>
                                                {word}: {count}
                                            </li>
                                        ))}
                                    </ul>
                                )}
                            </div>
                        </footer>
                    </article>
                ))}
            </div>
        </main>
    );
}

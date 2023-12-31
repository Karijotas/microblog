import React, { useEffect, useState } from "react";
import { Button, Form } from "react-bootstrap";

export function Register() {
    const JSON_HEADERS = {
        "Content-Type": "application/json",
    };
    const nameHandler = (e) => {
        setUserName(e.target.value);
    }
    const passwordHandler = (e) => {
        setPassword(e.target.value);
    }

    const [isRegistering, setIsRegistering] = useState(false);
    const [userName, setUserName] = useState("");
    const [password, setPassword] = useState("");
    const [id, setId] = useState(1);

    useEffect(() => {
        const register = () => new Promise((resolve) => setTimeout(resolve, 2000));

        if (isRegistering) {
            register().then(() => {
                setIsRegistering(false);
            });
        }
    }, [isRegistering]);

    const create = () => {
        fetch("/blogger/register", {
            method: "POST",
            headers: JSON_HEADERS,
            body: JSON.stringify({
                userName,
                password,
            }),
        }).then(applyResult);
    };

    const applyResult = () => {
        window.location.href = "http://localhost:8080/logout"
    };


    const handleRegister = () => create();

    return (

        <body>
            <div class="container">
                <form class="form-signin" method="post" action="/login">
                    <h2 class="form-signin-heading">Please sign in</h2>
                    <div class="alert alert-success" role="alert">You have been signed out</div>        <p>
                        <label for="username" class="sr-only">Username</label>
                        <input type="text" id="username" name="username" class="form-control" placeholder="Username" required autofocus/>
                    </p>
                    <p>
                        <label for="password" class="sr-only">Password</label>
                        <input type="password" id="password" name="password" class="form-control" placeholder="Password" required/>
                    </p>
                    <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
                </form>
            </div>
        </body>
    );
}

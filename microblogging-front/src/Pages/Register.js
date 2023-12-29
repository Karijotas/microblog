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
        <div className="container mt-5">
            <div className="row justify-content-center">
                <div className="col-md-6">
                    <Form>
                        <Form.Group controlId="formGroupEmail">
                            <Form.Label>Username</Form.Label>
                            <Form.Control type="name" placeholder="Enter Username" onChange={(e) => nameHandler(e)} />

                        </Form.Group>

                        <Form.Group controlId="formGroupPassword">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" placeholder="Enter Password" onChange={(e) => passwordHandler(e)} />
                        </Form.Group>

                        <Button
                            className="btn-block"
                            variant="primary"
                            disabled={isRegistering}
                            onClick={!isRegistering ? handleRegister : null}
                        >
                            {isRegistering ? 'Loading…' : 'Register'}
                        </Button>
                    </Form>
                </div>
            </div>
        </div>
    );
}

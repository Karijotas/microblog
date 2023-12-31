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
    const [isUsernameValid, setIsUsernameValid] = useState(true);
    const [isPasswordValid, setIsPasswordValid] = useState(true);
    const [isBack, setBack] = useState(false);


    useEffect(() => {
        const register = () => new Promise((resolve) => setTimeout(resolve, 2000));

        if (isRegistering) {
            register().then(() => {
                setIsRegistering(false);
            });
        }
    }, [isRegistering]);

    const create = () => {
        if (!userName.trim()) {
            setIsUsernameValid(false);
            return;
        }

        if (!isPasswordSecure(password)) {
            setIsPasswordValid(false);
            return;
        }

        fetch("/blogger/register", {
            method: "POST",
            headers: JSON_HEADERS,
            body: JSON.stringify({
                userName,
                password,
            }),
        }).then(applyResult);
    };

    const isPasswordSecure = (password) => {
        return password.length >= 8;
    };

    const applyResult = () => {
        window.location.href = "http://localhost:8080/logout"
    };


    const handleRegister = () => create();
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

                        {/* Validation feedback */}
                        {!isUsernameValid && (
                            <Form.Text className="text-danger">
                                Username cannot be empty.
                            </Form.Text>
                        )}
                        {!isPasswordValid && (
                            <Form.Text className="text-danger">
                                Password is insecure. Please use a secure password. The length should be no less than 8 symbols
                            </Form.Text>
                        )}
                    </Form>
                </div>
            </div>
        </div>
    );
}

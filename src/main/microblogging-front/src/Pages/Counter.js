import { useEffect, useState } from "react";

export function Counter() {
    const [count, setCount] = useState(0);

    useEffect(() => {
        const storedCount = localStorage.getItem("pageVisits");
        const initialCount = Number(storedCount) || 0;
        setCount(1 + initialCount)
        localStorage.setItem("pageVisits", 1 + initialCount)
    }, []);
    return <div>The website has been visited {count} times</div>

}
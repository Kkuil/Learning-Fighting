import {memo, useEffect, useState} from "react";

const Count = memo(function Count({words}) {
    return (
        <>
            <div>{words}</div>
            <div>{Math.random()}</div>
        </>
    )
})

function MemoFunction() {
    const [count, setCount] = useState(0)
    const [words, setWords] = useState("hello")
    const handleClick = () => setCount(1)
    const handleWords = () => setWords("你好")

    useEffect(() => {
        console.log("enter count = ", count)
        return () => {
            console.log("leave count = ", count)
        }
    }, [count])

    return (
        <div className="App">
            <Count words={words}/>
            {count}
            <button onClick={handleClick}>+</button>
            <button onClick={handleWords}>dawd</button>
        </div>
    );
}

export default MemoFunction;

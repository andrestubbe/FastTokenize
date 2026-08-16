// FastTokenizer Spectrum Test Sample: JavaScript / TypeScript
import React, { useState, useEffect } from 'react';

export interface UserProfileProps {
    id: number;
    username: string;
    isActive?: boolean;
    tags: string[];
}

/**
 * TypeScript / JavaScript Spectrum Component
 */
export const UserProfileCard: React.FC<UserProfileProps> = ({ id, username, isActive = true, tags }) => {
    const [clickCount, setClickCount] = useState<number>(0);
    const apiEndpoint = `https://api.fastjava.dev/v1/users/${id}`;

    useEffect(() => {
        console.log(`[FastTokenizer TS] Component mounted for ${username}`);
    }, [id, username]);

    const handleClick = (e: React.MouseEvent<HTMLButtonElement>): void => {
        e.preventDefault();
        setClickCount((prev) => prev + 1);
    };

    // Regex pattern test
    const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/g;

    return (
        <div className="user-card flex p-4 border rounded shadow-sm">
            <h3 className="text-lg font-bold">{username} (ID: {id})</h3>
            <span className={isActive ? "badge-active" : "badge-inactive"}>
                {isActive ? "Active" : "Disabled"}
            </span>
            <button onClick={handleClick} className="btn-primary mt-2">
                Clicks: {clickCount}
            </button>
        </div>
    );
};

import React, { useState, useEffect, useRef } from 'react';
import { Smile, ThumbsUp, X } from 'lucide-react';
import styles from './Message.module.css';
import angry from '../../assets/icons/angry.svg';
import haha from '../../assets/icons/haha.svg';
import heart from '../../assets/icons/heart.svg';
import huhu from '../../assets/icons/huhu.svg';
import like from '../../assets/icons/like.svg';
import wow from '../../assets/icons/wow.svg';
import send from '../../assets/icons/send.svg';

export default function Messenger() {
    const [isOpen, setIsOpen] = useState(true);
    const [isMinimized, setIsMinimized] = useState(false);
    const [messages, setMessages] = useState([
        {
            id: 1,
            user: 'Bạn',
            avatar: 'https://ui-avatars.com/api/?name=Me&background=42b72a&color=fff',
            content: 'Có chứ, bạn cần gì thế?',
            time: '10:32',
            reactions: {},
            isOwn: true,
            seen: true
        },
        {
            id: 2,
            user: 'Nguyễn Văn A',
            avatar: 'https://ui-avatars.com/api/?name=Nguyen+Van+A&background=0084ff&color=fff',
            content: 'Mình muốn hỏi về dự án tuần này, bạn đã hoàn thành phần của mình chưa?',
            time: '10:33',
            reactions: {},
            isOwn: false,
            seen: true
        }
    ]);

    const [inputText, setInputText] = useState('');
    const [showReactionPicker, setShowReactionPicker] = useState(null);
    const pickerRef = useRef(null);

    const reactions = [
        { icon: like, name: 'like' },
        { icon: heart, name: 'heart' },
        { icon: haha, name: 'haha' },
        { icon: wow, name: 'wow' },
        { icon: huhu, name: 'sad' },
        { icon: angry, name: 'angry' }
    ];

    useEffect(() => {
        const handleClickOutside = (e) => {
            if (pickerRef.current && !pickerRef.current.contains(e.target)) {
                setShowReactionPicker(null);
            }
        };
        document.addEventListener('mousedown', handleClickOutside);
        return () => document.removeEventListener('mousedown', handleClickOutside);
    }, []);

    const handleSendMessage = () => {
        if (inputText.trim()) {
            const newMessage = {
                id: messages.length + 1,
                user: 'Bạn',
                avatar: 'https://ui-avatars.com/api/?name=Me&background=42b72a&color=fff',
                content: inputText,
                time: new Date().toLocaleTimeString('vi-VN', {
                    hour: '2-digit',
                    minute: '2-digit'
                }),
                reactions: {},
                isOwn: true,
                seen: false
            };
            setMessages([...messages, newMessage]);
            setInputText('');
        }
    };

    const handleReaction = (messageId, reactionName) => {
        const userId = 'me';
        setMessages((prev) =>
            prev.map((msg) => {
                if (msg.id === messageId) {
                    const newReactions = { ...msg.reactions };
                    if (newReactions[userId] === reactionName) {
                        delete newReactions[userId];
                    } else {
                        newReactions[userId] = reactionName;
                    }
                    return { ...msg, reactions: newReactions };
                }
                return msg;
            })
        );
        setShowReactionPicker(null);
    };

    if (!isOpen) return null;

    return (
        <div
            className={styles.messengerWrapper}
            style={{ height: isMinimized ? '56px' : '500px' }}
        >
            <div className={styles.header}>
                <div className={styles.headerUser}>
                    <img
                        src="https://ui-avatars.com/api/?name=Nguyen+Van+A&background=0084ff&color=fff"
                        alt="Avatar"
                    />
                    <div>
                        <h2>Nguyễn Văn A</h2>
                        <p>Đang hoạt động</p>
                    </div>
                </div>
                <div className={styles.headerActions}>
                    <button onClick={() => setIsMinimized(!isMinimized)}>
                        {isMinimized ? (
                            <i className={`fi fi-sr-angle-small-up ${styles.upIcon}`}></i>
                        ) : (
                            <i className={`fi fi-rr-minus-small ${styles.upIcon}`}></i>
                        )}
                    </button>
                    <button onClick={() => setIsOpen(false)}>
                        <X size={20} className={styles.closeIcon} />
                    </button>
                </div>
            </div>

            {!isMinimized && (
                <>
                    <div className={styles.messages}>
                        {messages.map((message) => (
                            <div
                                key={message.id}
                                className={`${styles.messageRow} ${
                                    message.isOwn ? styles.own : ''
                                }`}
                            >
                                {!message.isOwn && (
                                    <img src={message.avatar} alt={message.user} />
                                )}

                                <div className={styles.messageContentWrapper}>
                                    <div className={styles.messageBubbleWrapper}>
                                        <div
                                            className={`${styles.messageBubble} ${
                                                message.isOwn
                                                    ? styles.ownBubble
                                                    : styles.otherBubble
                                            }`}
                                        >
                                            {message.content}
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    setShowReactionPicker(
                                                        showReactionPicker === message.id
                                                            ? null
                                                            : message.id
                                                    );
                                                }}
                                                className={`${styles.reactionButton} ${
                                                    message.isOwn
                                                        ? styles.left
                                                        : styles.right
                                                }`}
                                            >
                                                <Smile size={14} />
                                            </button>
                                        </div>

                                        {showReactionPicker === message.id && (
                                            <div
                                                ref={pickerRef}
                                                className={`${styles.reactionPicker} ${
                                                    message.isOwn
                                                        ? styles.right
                                                        : styles.left
                                                }`}
                                            >
                                                {reactions.map((reaction) => (
                                                    <button
                                                        key={reaction.name}
                                                        onClick={() =>
                                                            handleReaction(
                                                                message.id,
                                                                reaction.name
                                                            )
                                                        }
                                                        aria-label={reaction.name}
                                                        type="button"
                                                    >
                                                        <img
                                                            src={reaction.icon}
                                                            alt={reaction.name}
                                                            className={
                                                                styles.reactionIcon
                                                            }
                                                        />
                                                    </button>
                                                ))}
                                            </div>
                                        )}

                                        {Object.keys(message.reactions).length > 0 && (
                                            <div
                                                className={`${styles.reactionDisplay} ${
                                                    message.isOwn
                                                        ? styles.left
                                                        : styles.right
                                                }`}
                                            >
                                                {(() => {
                                                    const counts = {};
                                                    Object.values(
                                                        message.reactions
                                                    ).forEach((r) => {
                                                        counts[r] = (counts[r] || 0) + 1;
                                                    });
                                                    return Object.entries(counts).map(
                                                        ([name, count]) => {
                                                            const reaction = reactions.find(
                                                                (r) => r.name === name
                                                            );
                                                            return (
                                                                <span
                                                                    key={name}
                                                                    className={
                                                                        styles.reactionItem
                                                                    }
                                                                >
                                                                    <img
                                                                        src={reaction.icon}
                                                                        alt={name}
                                                                        className={
                                                                            styles.reactionDisplayIcon
                                                                        }
                                                                    />
                                                                    {count > 1 && (
                                                                        <span
                                                                            className={
                                                                                styles.reactionCount
                                                                            }
                                                                        >
                                                                            {count}
                                                                        </span>
                                                                    )}
                                                                </span>
                                                            );
                                                        }
                                                    );
                                                })()}
                                            </div>
                                        )}
                                    </div>

                                    <div className={styles.messageMeta}>
                                        <span>{message.time}</span>
                                        {message.isOwn && (
                                            <>
                                                <span>•</span>
                                                {message.seen ? (
                                                    <img
                                                        src="https://ui-avatars.com/api/?name=Nguyen+Van+A&background=0084ff&color=fff&size=16"
                                                        alt="Đã xem"
                                                    />
                                                ) : (
                                                    <div
                                                        className={styles.sentDot}
                                                    ></div>
                                                )}
                                            </>
                                        )}
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className={styles.inputArea}>
                        <button>
                            <Smile size={23} />
                        </button>
                        <input
                            type="text"
                            value={inputText}
                            onChange={(e) => setInputText(e.target.value)}
                            onKeyPress={(e) =>
                                e.key === 'Enter' && handleSendMessage()
                            }
                            placeholder="Aa"
                        />
                        <button onClick={handleSendMessage}>
                            {inputText.trim() === '' ? (
                                <ThumbsUp size={24.8} />
                            ) : (
                                <img src={send} alt="send" />
                            )}
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

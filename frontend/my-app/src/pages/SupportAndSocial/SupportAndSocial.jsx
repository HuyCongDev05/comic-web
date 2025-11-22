import styles from './SupportAndSocial.module.css';
import MemberApi from '../../api/Member';
import {useEffect, useState} from 'react';
import {HideScrollbar} from "@comics/shared";

const Icon = {
    X: () => (
        <svg
            className={styles.icon}
            xmlns="http://www.w3.org/2000/svg"
            width="25px"
            height="25px"
            viewBox="0 0 24 24"
        >
            <path
                fill="currentColor"
                d="M10.488 14.651L15.25 21h7l-7.858-10.478L20.93 3h-2.65l-5.117 5.886L8.75 3h-7l7.51 10.015L2.32 21h2.65zM16.25 19L5.75 5h2l10.5 14z"
            />
        </svg>
    ),
    Instagram: () => (
        <svg
            className={styles.icon}
            xmlns="http://www.w3.org/2000/svg"
            width="25px"
            height="25px"
            viewBox="0 0 24 24"
        >
            <path
                fill="currentColor"
                d="M7 2C4.2 2 2 4.2 2 7v10c0 2.8 2.2 5 5 5h10c2.8 0 5-2.2 5-5V7c0-2.8-2.2-5-5-5H7m10 2c1.7 0 3 1.3 3 3v10c0 1.7-1.3 3-3 3H7c-1.7 0-3-1.3-3-3V7c0-1.7 1.3-3 3-3h10m-5 3a5 5 0 1 0 0 10 5 5 0 0 0 0-10m0 2a3 3 0 1 1 0 6 3 3 0 0 1 0-6m4.8-.9a.9.9 0 1 0 0-1.8.9.9 0 0 0 0 1.8"
            />
        </svg>
    ),
    Facebook: () => (
        <svg
            className={styles.icon}
            xmlns="http://www.w3.org/2000/svg"
            width="25px"
            height="25px"
            viewBox="0 0 24 24"
        >
            <path
                fill="currentColor"
                d="M22 12c0-5.52-4.48-10-10-10S2 6.48 2 12c0 4.84 3.44 8.87 8 9.8V15H8v-3h2V9.5C10 7.57 11.57 6 13.5 6H16v3h-2c-.55 0-1 .45-1 1v2h3v3h-3v6.95c5.05-.5 9-4.76 9-9.95"
            ></path>
        </svg>
    ),
};


export default function SupportAndSocial() {
    HideScrollbar();
    const [teamMembers, setTeamMembers] = useState([]);
    useEffect(() => {
        const fetchMembers = async () => {
            try {
                const response = await MemberApi.getAllMember();
                setTeamMembers(response.data);
                console.log('Fetched members:', response.data);
            } catch (error) {
                console.error('Error fetching members:', error);
            }
        };
        fetchMembers();
    }, []);

    const TeamMemberCard = ({ member }) => (
        <div className={`${styles.card} ${styles.fadeInUp}`}>
            <div className={styles.avatar}>
                <img
                    src={member.avatar || "https://i.pinimg.com/736x/7d/b9/56/7db956d51da0e02f621e011879fcef37.jpg"}
                    alt={member.name}
                    onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = `https://placehold.co/200x200/E2E8F0/4A5568?text=${member.name
                            .split(' ')
                            .map((n) => n[0])
                            .join('')}`;
                    }}
                />
            </div>
            <h3>{member.name}</h3>
            <p>{member.role}</p>
            <div className={styles.socials}>
                <a href={member.x}><Icon.X /></a>
                <a href={member.instagram}><Icon.Instagram /></a>
                <a href={member.facebook}><Icon.Facebook /></a>
            </div>
        </div>
    );

    return (
        <>
            <section className={styles.section}>
                <div className={styles.header}>
                    <h2>Thành viên của chúng tôi</h2>
                    <p>
                        Gặp gỡ đội ngũ xuất sắc của chúng tôi - sự kết hợp giữa tài năng, sáng tạo và tận tâm,
                        cùng nhau kiến ​​tạo thành công với niềm đam mê và sự đổi mới.
                        Chúng tôi luôn tuyển dụng những tài năng mới để gia nhập vào hành trình phát triển của mình.
                        Hãy tham gia cùng chúng tôi để cùng nhau tạo nên những điều tuyệt vời!
                    </p>
                </div>
                <div className={styles.grid}>
                    {teamMembers.map((member, index) => (
                        <div key={member.name} style={{ animationDelay: `${index * 0.1}s` }}>
                            <TeamMemberCard member={member} />
                        </div>
                    ))}
                </div>
            </section>
        </>
    )
};
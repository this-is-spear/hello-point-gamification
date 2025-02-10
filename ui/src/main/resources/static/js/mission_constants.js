export const MISSION_STATUS = {
    NOT_STARTED: 'NOT_STARTED',
    IN_PROGRESS: 'IN_PROGRESS',
    COMPLETED: 'COMPLETED'
};

export const MISSIONS = Array.from({ length: 20 }, (_, i) => ({
    id: `mission${i + 1}`,
    title: `미션 ${i + 1}`,
    description: `미션 ${i + 1} 상세 설명`,
    reward: 2
}));

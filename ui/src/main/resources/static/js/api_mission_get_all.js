export class MissionResponses {
    constructor(data) {
        this.missions = data.map(mission => new MissionResponse(mission));
    }

    static async fetchMissionResponses(sessionId) {
        const response = await fetch(`/api/missions?sessionId=${sessionId}`, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }
        const data = await response.json();
        return new MissionResponses(data);
    }
}

export class MissionResponse {
    constructor(data) {
        this.missionId = data.missionId;
        this.status = data.status;
        this.title = data.title;
        this.rewardEggCount = data.rewardEggCount;
    }
}

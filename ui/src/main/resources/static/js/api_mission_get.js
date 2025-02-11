export class MissionDetailResponse {
    constructor(data) {
        this.missionId = data.missionId;
        this.status = data.status;
        this.title = data.title;
        this.description = data.description;
        this.rewardEggCount = data.rewardEggCount;
    }

    static async fetchMissionDetail(missionId, sessionId) {
        const response = await fetch(`/api/missions/${missionId}?sessionId=${sessionId}`, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }
        const data = await response.json();
        return new MissionDetailResponse(data);
    }
}

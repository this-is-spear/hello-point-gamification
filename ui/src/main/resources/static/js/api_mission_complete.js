export class CompleteMission {
    static async fetchMissionComplete(missionId, sessionId) {
        const response = await fetch(`/api/missions/${missionId}/complete?sessionId=${sessionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }
        return new CompleteMission();
    }
}

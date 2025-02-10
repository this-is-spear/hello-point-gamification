export class GameSessionResponse {
    constructor(data) {
        this.availableEggs = data.availableEggs;
        this.availablePoints = data.availablePoints;
    }

    static async fetchMyGameSession(sessionId) {
        const response = await fetch(`/api/games?sessionId=${sessionId}`, {
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }
        const data = await response.json();
        return new GameSessionResponse(data);
    }
}

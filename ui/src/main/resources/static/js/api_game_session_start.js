export class StartGameSessionResponse {
    constructor(data) {
        this.sessionId = data.sessionId;
    }

    static async fetchStartGameSession() {
        const response = await fetch('/api/games', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }

        const data = await response.json();
        return new StartGameSessionResponse(data);
    }
}

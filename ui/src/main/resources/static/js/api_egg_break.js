export class BreakEggResponse {
    constructor(data) {
        this.earnedPoints = data.earnedPoints;
    }

    static async fetchBreakEgg(sessionId) {
        const response = await fetch(`/api/eggs/break?sessionId=${sessionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }
        const data = await response.json();
        return new BreakEggResponse(data);
    }
}

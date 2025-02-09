export class AddEggResponse {
    static async fetchAddEgg(sessionId) {
        const response = await fetch(`/api/eggs/acquire?sessionId=${sessionId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        if (!response.ok) {
            throw new Error('Failed to fetch eggs count');
        }
        return new AddEggResponse();
    }
}

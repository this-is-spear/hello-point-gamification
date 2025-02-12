export class SseClient {
    constructor() {
        this.eventSource = new EventSource('/api/sse/connect');
    }

    connect(sessionId) {
        if (this.eventSource) {
            this.disconnect();
        }

        const url = `/api/sse/connect?sessionId=${encodeURIComponent(sessionId)}`;
        this.eventSource = new EventSource(url);

        this.eventSource.onopen = () => {
        };

        this.eventSource.onmessage = (event) => {
            try {
                const data = new SseMessage(JSON.parse(event.data));
                const customEvent = new CustomEvent('sseMessage', {detail: data});
                window.dispatchEvent(customEvent);
            } catch (error) {
                console.error('메시지 파싱 에러:', error);
            }
        };

        this.eventSource.onerror = (error) => {
            this.disconnect();
            setTimeout(() => this.connect(sessionId), 3000);
        };
    }

    disconnect() {
        if (this.eventSource) {
            this.eventSource.close();
            this.eventSource = null;
        }
    }
}

export class SseMessage {
    constructor(data) {
        this.availableEggs = data.availableEggs;
        this.availablePoints = data.availablePoints;
    }
}
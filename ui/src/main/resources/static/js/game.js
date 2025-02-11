import {GameSessionResponse} from './api_game_session_get.js';

export class Game {
    constructor(sessionId) {
        this.sessionId = sessionId
        this.score = 0;
        this.events = new EventTarget();
        this.createUI();
        this.initializePoints();
    }

    async initializePoints() {
        try {
            let myGameSession = await GameSessionResponse.fetchMyGameSession(this.sessionId);
            this.score = myGameSession.availablePoints
            this.updateScore(this.score);
        } catch (error) {
            console.error('Error fetching points:', error);
        }
    }

    on(eventName, handler) {
        this.events.addEventListener(eventName, handler);
    }

    emit(eventName, detail) {
        const event = new CustomEvent(eventName, {detail});
        this.events.dispatchEvent(event);
    }

    createUI() {
        // 점수 표시
        this.scoreElement = document.createElement('div');
        this.scoreElement.className = 'score';
        this.updateScore();
        document.body.appendChild(this.scoreElement);

        // 컨트롤 버튼
        const controls = document.createElement('div');
        controls.className = 'controls';

        const resetButton = document.createElement('button');
        resetButton.innerHTML = '⬇️';
        resetButton.onclick = () => this.emit('reset');
        resetButton.title = '모든 계란을 중앙에서 떨어뜨리기';

        const addButton = document.createElement('button');
        addButton.innerHTML = '➕';
        addButton.onclick = () => this.emit('add');
        addButton.title = '계란 추가하기';

        controls.appendChild(resetButton);
        controls.appendChild(addButton);
        document.body.appendChild(controls);
    }

    updateScore() {
        this.scoreElement.textContent = `Score: ${this.score}`;
    }

    async addScore() {
        await this.initializePoints()
    }
}

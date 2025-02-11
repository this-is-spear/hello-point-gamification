import { Game } from './game.js';
import { GameSessionResponse } from './api_game_session_get.js';
import { BreakEggResponse } from "./api_egg_break.js";
import { StartGameSessionResponse } from "./api_game_session_start.js";
import { MissionUI } from "./mission_ui.js";
import { EggManager } from './egg_manager.js';

class GameController {
    constructor() {
        this.sessionId = null;
        this.initialize();
    }

    async initialize() {
        await this.initializeSession();
        this.game = new Game(this.sessionId);

        this.eggManager = new EggManager(
            this.sessionId,
            this.handleEggBreak.bind(this),
            this.handleEggRemove.bind(this)
        );

        new MissionUI(this.sessionId, this.eggManager);
        await this.setupEventListeners();
        await this.initializeEggs();
    }

    async initializeSession() {
        let startGameSession = await StartGameSessionResponse.fetchStartGameSession();
        this.sessionId = startGameSession.sessionId;
    }

    setupEventListeners() {
        this.game.on('add', () => this.eggManager.addEgg());
        this.game.on('reset', () => this.breakAll());
    }

    async initializeEggs() {
        try {
            let myGameSession = await GameSessionResponse.fetchMyGameSession(this.sessionId);
            for (let i = 0; i < myGameSession.availableEggs; i++) {
                await this.eggManager.addEgg();
            }
        } catch (error) {
            console.error('Error fetching initial eggs:', error);
        }
    }

    async handleEggBreak(event) {
        let breakEggResponse = await BreakEggResponse.fetchBreakEgg(this.sessionId);
        let earnedPoints = breakEggResponse.earnedPoints;
        if (earnedPoints > 0) {
            const pointsElement = document.createElement('div');
            pointsElement.className = 'points';
            const egg = this.eggManager.getEgg(event.detail.id);
            if (egg) {
                pointsElement.style.left = `${egg.position.x}px`;
                pointsElement.style.bottom = `${egg.position.y + 48}px`;
                pointsElement.textContent = `+${earnedPoints}pt`;
                document.body.appendChild(pointsElement);

                setTimeout(() => {
                    document.body.removeChild(pointsElement);
                }, 1000);
            }
        }
        await this.game.addScore();
    }

    handleEggRemove(event) {
        this.eggManager.deleteEgg(event.detail.id);
    }

    breakAll() {
        const centerX = window.innerWidth / 2;
        this.eggManager.breakAll(centerX, window.innerHeight / 2);
    }
}

window.onload = () => {
    new GameController();
};

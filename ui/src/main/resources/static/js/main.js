import {Game} from './game.js';
import {Egg} from './egg.js';
import {GameSessionResponse} from './api_get_my_game_session.js';
import {AddEggResponse} from "./api_add_egg.js";
import {BreakEggResponse} from "./api_break_egg.js";
import {StartGameSessionResponse} from "./api_start_game_session.js";

class GameController {
    constructor() {
        this.eggs = new Map();
        this.nextId = 1;
        this.sessionId = null
        this.initializeGame();
    }

    async initializeGame() {
        await this.initializeSession()
        this.game = new Game(this.sessionId);
        await this.setupEventListeners();
        await this.initializeEggs();
    }

    async initializeSession() {
        let startGameSession = await StartGameSessionResponse.fetchStartGameSession();
        this.sessionId = startGameSession.sessionId
    }

    setupEventListeners() {
        this.game.on('add', () => this.addEgg());
        this.game.on('reset', () => this.breakAll());

        this.handleEggBreak = this.handleEggBreak.bind(this);
        this.handleEggRemove = this.handleEggRemove.bind(this);
    }

    async initializeEggs() {
        try {
            let myGameSession = await GameSessionResponse.fetchMyGameSession(this.sessionId);
            for (let i = 0; i < myGameSession.availableEggs; i++) {
                this.addEgg();
            }
        } catch (error) {
            console.error('Error fetching initial eggs:', error);
        }
    }

    addEgg() {
        AddEggResponse.fetchAddEgg(this.sessionId).then(r => {
                const randomX = Math.random() * (window.innerWidth - 100) + 50;
                const egg = new Egg(this.nextId, randomX);

                egg.on('explode', this.handleEggBreak);
                egg.on('remove', this.handleEggRemove);

                this.eggs.set(this.nextId, egg);
                this.nextId++;
            }
        )
    }

    async handleEggBreak(event) {
        let breakEggResponse = await BreakEggResponse.fetchBreakEgg(this.sessionId);
        let earnedPoints = breakEggResponse.earnedPoints;
        if (earnedPoints > 0) {
            const pointsElement = document.createElement('div');
            pointsElement.className = 'points';
            const egg = this.eggs.get(event.detail.id);
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
        const {id} = event.detail;
        this.eggs.delete(id);
    }

    breakAll() {
        const centerX = window.innerWidth / 2;
        this.eggs.forEach(egg => {
            egg.reset(centerX, window.innerHeight / 2);
        });
    }
}

window.onload = () => {
    new GameController();
};

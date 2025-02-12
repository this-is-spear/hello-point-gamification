import {Egg} from './egg.js';

export class EggManager {
    constructor(sessionId, onEggBreak, onEggRemove) {
        this.sessionId = sessionId;
        this.nextId = 1;
        this.eggs = new Map();
        this.handleEggBreak = onEggBreak;
        this.handleEggRemove = onEggRemove;
    }

    async addEgg() {
        const randomX = Math.random() * (window.innerWidth - 100) + 50;
        const egg = new Egg(this.nextId, randomX);

        egg.on('explode', this.handleEggBreak);
        egg.on('remove', this.handleEggRemove);

        this.eggs.set(this.nextId, egg);
        this.nextId++;
    }

    getEgg(id) {
        return this.eggs.get(id);
    }

    deleteEgg(id) {
        this.eggs.delete(id);
    }

    breakAll(centerX, centerY) {
        this.eggs.forEach(egg => {
            egg.reset(centerX, centerY);
        });
    }
}

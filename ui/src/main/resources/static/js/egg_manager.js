import { Egg } from './egg.js';
import { AddEggResponse } from "./api_egg_add.js";

export class EggManager {
    constructor(sessionId, onEggBreak, onEggRemove) {
        this.sessionId = sessionId;
        this.nextId = 1;
        this.eggs = new Map();
        this.handleEggBreak = onEggBreak;
        this.handleEggRemove = onEggRemove;
    }

    async addEgg() {
        await AddEggResponse.fetchAddEgg(this.sessionId).then(r => {
            const randomX = Math.random() * (window.innerWidth - 100) + 50;
            const egg = new Egg(this.nextId, randomX);

            egg.on('explode', this.handleEggBreak);
            egg.on('remove', this.handleEggRemove);

            this.eggs.set(this.nextId, egg);
            this.nextId++;
        });
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

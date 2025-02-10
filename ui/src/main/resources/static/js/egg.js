export class Egg {
    constructor(id, initialX) {
        this.id = id;
        this.position = { x: initialX, y: 0 };
        this.isDragging = false;
        this.dragOffset = { x: 0, y: 0 };
        this.direction = 1;
        this.isExploded = false;
        this.fallingSpeed = 0;
        this.element = null;

        this.speed = 2;
        this.gravity = 0.8;
        this.bottomPadding = 24;

        this.createElements();
        this.setupEventListeners();
        this.startAnimation();

        this.events = new EventTarget();
    }

    on(eventName, handler) {
        this.events.addEventListener(eventName, handler);
    }

    emit(eventName, detail) {
        const event = new CustomEvent(eventName, { detail });
        this.events.dispatchEvent(event);
    }

    createElements() {
        this.element = document.createElement('div');
        this.element.className = 'egg';
        this.updateEggSVG();
        this.updatePosition();
        document.body.appendChild(this.element);
    }

    updateEggSVG(isExploded = false) {
        if (isExploded) {
            this.element.innerHTML = `
                <svg width="48" height="48" viewBox="0 0 48 48">
                    <ellipse cx="24" cy="24" rx="22" ry="18" fill="#FFFFFF" stroke="#E5E5E5" stroke-width="2"/>
                    <circle cx="24" cy="24" r="8" fill="#FFD700" stroke="#FFA500" stroke-width="2"/>
                </svg>
            `;
        } else {
            this.element.innerHTML = `
                <svg width="48" height="48" viewBox="0 0 48 48">
                    <ellipse cx="24" cy="24" rx="16" ry="20" fill="#FFFFFF" stroke="#E5E5E5" stroke-width="2"/>
                    <path d="M16 20C16 16 20 12 24 12C28 12 32 16 32 20" stroke="#F5F5F5" stroke-width="2" fill="none"/>
                </svg>
            `;
        }
    }

    setupEventListeners() {
        this.element.addEventListener('mousedown', this.handleMouseDown.bind(this));
        this.element.addEventListener('touchstart', this.handleTouchStart.bind(this), { passive: false });
    }

    handleMouseDown(e) {
        e.preventDefault();
        this.startDrag(e.clientX, e.clientY);

        const handleMove = (e) => {
            this.handleMove(e.clientX, e.clientY);
        };

        const handleUp = () => {
            this.handleEnd();
            window.removeEventListener('mousemove', handleMove);
            window.removeEventListener('mouseup', handleUp);
        };

        window.addEventListener('mousemove', handleMove);
        window.addEventListener('mouseup', handleUp);
    }

    handleTouchStart(e) {
        e.preventDefault();
        const touch = e.touches[0];
        this.startDrag(touch.clientX, touch.clientY);

        const handleMove = (e) => {
            const touch = e.touches[0];
            this.handleMove(touch.clientX, touch.clientY);
        };

        const handleEnd = () => {
            this.handleEnd();
            window.removeEventListener('touchmove', handleMove);
            window.removeEventListener('touchend', handleEnd);
        };

        window.addEventListener('touchmove', handleMove, { passive: false });
        window.addEventListener('touchend', handleEnd);
    }

    startDrag(clientX, clientY) {
        this.isDragging = true;
        this.fallingSpeed = 0;
        this.element.classList.add('grabbing', 'happy');

        const rect = this.element.getBoundingClientRect();
        this.dragOffset = {
            x: clientX - rect.left,
            y: window.innerHeight - clientY - this.bottomPadding
        };
    }

    handleMove(clientX, clientY) {
        if (this.isDragging) {
            const nextX = Math.max(this.bottomPadding,
                Math.min(window.innerWidth - this.bottomPadding, clientX - this.dragOffset.x));
            const nextY = Math.max(0, window.innerHeight - clientY - this.bottomPadding);

            this.position = { x: nextX, y: nextY };
            this.updatePosition();
        }
    }

    handleEnd() {
        if (this.isDragging) {
            this.isDragging = false;
            this.element.classList.remove('grabbing', 'happy');
        }
    }

    updatePosition() {
        this.element.style.left = `${this.position.x}px`;
        this.element.style.bottom = `${this.position.y}px`;
        this.element.style.transform = `translateX(-50%) scaleX(${this.direction})`;
    }

    async break() {
        this.isExploded = true;
        this.updateEggSVG(true);

        this.emit('explode', { id: this.id });

        const boom = document.createElement('div');
        boom.className = 'boom-emoji';
        boom.style.cssText = `
        position: absolute;
        left: ${this.position.x}px;
        bottom: 10px;
        z-index: 2;
    `;
        boom.textContent = '💥';
        document.body.appendChild(boom);

        setTimeout(() => {
            document.body.removeChild(boom);
            this.remove();
        }, 1000);
    }

    remove() {
        document.body.removeChild(this.element);
        this.emit('remove', { id: this.id });
    }

    reset(x, y) {
        this.position.x = x;
        this.position.y = y;
        this.fallingSpeed = 0;
        this.updatePosition();
    }

    startAnimation() {
        const animate = () => {
            if (!this.isDragging && !this.isExploded) {
                // 중력 적용
                if (this.position.y > 0) {
                    this.fallingSpeed += this.gravity;
                    this.position.y = Math.max(0, this.position.y - this.fallingSpeed);

                    if (this.position.y === 0 && this.fallingSpeed > 0) {
                        this.break();
                        return;
                    }
                } else {
                    this.fallingSpeed = 0;
                }

                // 좌우 이동
                this.position.x += this.speed * this.direction;
                if (this.position.x >= window.innerWidth - this.bottomPadding) {
                    this.direction = -1;
                } else if (this.position.x <= this.bottomPadding) {
                    this.direction = 1;
                }

                this.updatePosition();
            }

            if (!this.isExploded) {
                requestAnimationFrame(animate);
            }
        };

        requestAnimationFrame(animate);
    }
}

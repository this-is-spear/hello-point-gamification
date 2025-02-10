import {MISSION_STATUS, MISSIONS} from "./mission_constants.js";


export class MissionUI {
    constructor() {
        this.missions = MISSIONS.map(mission => ({
            ...mission,
            status: MISSION_STATUS.NOT_STARTED
        }));
        this.currentDialog = null;
        this.isDragging = false;
        this.startX = 0;
        this.scrollLeft = 0;

        this.createMissionBar();
        this.setupDragScroll();
        this.createScrollIndicator();
    }

    createMissionBar() {
        this.missionBar = document.createElement('div');
        this.missionBar.className = 'mission-bar';

        this.missionContainer = document.createElement('div');
        this.missionContainer.className = 'mission-container';

        this.missions.forEach(mission => {
            const button = document.createElement('button');
            button.className = 'mission-button';
            button.textContent = mission.title;
            button.onclick = (e) => {
                // 드래그 중에는 클릭 이벤트 방지
                if (!this.isDragging) {
                    this.showMissionDialog(mission);
                }
            };
            this.missionContainer.appendChild(button);
        });

        this.missionBar.appendChild(this.missionContainer);
        document.body.appendChild(this.missionBar);
    }

    setupDragScroll() {
        this.missionContainer.addEventListener('mousedown', (e) => {
            this.isDragging = true;
            this.missionContainer.classList.add('grabbing');
            this.startX = e.pageX - this.missionContainer.offsetLeft;
            this.scrollLeft = this.missionContainer.scrollLeft;
        });

        this.missionContainer.addEventListener('mouseleave', () => {
            this.isDragging = false;
            this.missionContainer.classList.remove('grabbing');
        });

        this.missionContainer.addEventListener('mouseup', () => {
            this.isDragging = false;
            this.missionContainer.classList.remove('grabbing');
        });

        this.missionContainer.addEventListener('mousemove', (e) => {
            if (!this.isDragging) return;
            e.preventDefault();
            const x = e.pageX - this.missionContainer.offsetLeft;
            const walk = (x - this.startX) * 2; // 스크롤 속도
            this.missionContainer.scrollLeft = this.scrollLeft - walk;
            this.updateScrollIndicator();
        });

        // 모바일 터치 지원
        this.missionContainer.addEventListener('touchstart', (e) => {
            this.isDragging = true;
            this.startX = e.touches[0].pageX - this.missionContainer.offsetLeft;
            this.scrollLeft = this.missionContainer.scrollLeft;
        });

        this.missionContainer.addEventListener('touchend', () => {
            this.isDragging = false;
        });

        this.missionContainer.addEventListener('touchmove', (e) => {
            if (!this.isDragging) return;
            const x = e.touches[0].pageX - this.missionContainer.offsetLeft;
            const walk = (x - this.startX) * 2;
            this.missionContainer.scrollLeft = this.scrollLeft - walk;
            this.updateScrollIndicator();
        });

        // 스크롤 이벤트
        this.missionContainer.addEventListener('scroll', () => {
            this.updateScrollIndicator();
        });
    }

    createScrollIndicator() {
        this.scrollIndicator = document.createElement('div');
        this.scrollIndicator.className = 'scroll-indicator';
        this.missionBar.appendChild(this.scrollIndicator);
        this.updateScrollIndicator();
    }

    updateScrollIndicator() {
        const totalWidth = this.missionContainer.scrollWidth;          // 전체 스크롤 가능한 너비
        const containerWidth = this.missionContainer.clientWidth;      // 보이는 영역의 너비
        const scrollLeft = this.missionContainer.scrollLeft;           // 현재 스크롤 위치

        if (totalWidth <= containerWidth) {
            this.scrollIndicator.style.display = 'none';
            return;
        }

        this.scrollIndicator.style.display = 'block';

        const indicatorWidth = (containerWidth / totalWidth) * containerWidth;
        const maxScroll = totalWidth - containerWidth;
        const maxIndicatorOffset = containerWidth - indicatorWidth;
        const indicatorOffset = (scrollLeft / maxScroll) * maxIndicatorOffset;

        this.scrollIndicator.style.width = `${indicatorWidth}px`;
        this.scrollIndicator.style.transform = `translateX(${indicatorOffset}px)`;
    }

    showMissionDialog(mission) {
        if (this.currentDialog) {
            document.body.removeChild(this.currentDialog);
        }

        this.currentDialog = document.createElement('div');
        this.currentDialog.className = 'mission-dialog';

        const dialogContent = document.createElement('div');
        dialogContent.className = 'mission-content';

        if (mission.status === MISSION_STATUS.NOT_STARTED) {
            this.showInitialDialog(mission, dialogContent);
        } else if (mission.status === MISSION_STATUS.IN_PROGRESS) {
            this.showProgressDialog(mission, dialogContent);
        } else {
            this.showCompletedDialog(mission, dialogContent);
        }

        this.currentDialog.appendChild(dialogContent);
        document.body.appendChild(this.currentDialog);
    }

    showInitialDialog(mission, content) {
        content.innerHTML = `
            <h2>${mission.title} 상세 설명</h2>
            <button class="start-button">시작하기</button>
            <button class="home-button">홈으로</button>
        `;

        content.querySelector('.start-button').onclick = () => {
            mission.status = MISSION_STATUS.IN_PROGRESS;
            this.showMissionDialog(mission);
        };

        content.querySelector('.home-button').onclick = () => {
            this.closeDialog();
        };
    }

    showProgressDialog(mission, content) {
        content.innerHTML = `
            <h2>${mission.title}</h2>
            <p>아래 링크를 클릭하자</p>
            <button class="link-button">링크 클릭</button>
            <button class="home-button">홈으로</button>
        `;

        content.querySelector('.link-button').onclick = () => {
            mission.status = MISSION_STATUS.COMPLETED;
            this.showMissionDialog(mission);
        };

        content.querySelector('.home-button').onclick = () => {
            this.closeDialog();
        };
    }

    showCompletedDialog(mission, content) {
        content.innerHTML = `
            <h2>${mission.title} 완료!</h2>
            <p>보상: 알 ${mission.reward}개</p>
            <button class="home-button">홈으로</button>
        `;

        content.querySelector('.home-button').onclick = () => {
            this.closeDialog();
        };
    }

    closeDialog() {
        if (this.currentDialog) {
            document.body.removeChild(this.currentDialog);
            this.currentDialog = null;
        }
    }
}

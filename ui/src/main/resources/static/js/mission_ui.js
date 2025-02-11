import {MISSION_STATUS} from "./mission_constants.js";
import {MissionResponses} from "./api_mission_get_all.js";
import {StartMission} from "./api_mission_start.js";
import {CompleteMission} from "./api_mission_complete.js";
import {MissionDetailResponse} from "./api_mission_get.js";

export class MissionUI {
    constructor(sessionId, eggManager) {
        this.sessionId = sessionId;
        this.currentDialog = null;
        this.isDragging = false;
        this.startX = 0;
        this.scrollLeft = 0;
        this.eggManager = eggManager;
        this.initializeMissions().then(() => {
            this.createMissionBar();
            this.setupDragScroll();
            this.createScrollIndicator();
        })
    }

    async initializeMissions() {
        let missions = await MissionResponses.fetchMissionResponses(this.sessionId);
        this.missions = missions.missions;
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
            button.onclick = async (e) => {
                if (!this.isDragging) {
                    await this.showMissionDialog(mission);
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
        const totalWidth = this.missionContainer.scrollWidth;
        const containerWidth = this.missionContainer.clientWidth;
        const scrollLeft = this.missionContainer.scrollLeft;

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

    async showMissionDialog(mission) {
        if (this.currentDialog) {
            document.body.removeChild(this.currentDialog);
        }

        this.currentDialog = document.createElement('div');
        this.currentDialog.className = 'mission-dialog';

        const dialogContent = document.createElement('div');
        dialogContent.className = 'mission-content';

        if (mission.status === MISSION_STATUS.NOT_STARTED) {
            await this.showInitialDialog(mission, dialogContent);
        } else if (mission.status === MISSION_STATUS.IN_PROGRESS) {
            this.showProgressDialog(mission, dialogContent);
        } else {
            await this.showCompletedDialog(mission, dialogContent);
        }

        this.currentDialog.appendChild(dialogContent);
        document.body.appendChild(this.currentDialog);
    }

    async showInitialDialog(mission, content) {
        let missionDetail = await MissionDetailResponse.fetchMissionDetail(mission.missionId, this.sessionId);

        content.innerHTML = `
            <h2>${missionDetail.title}</h2>
            <p>${missionDetail.description}</p>
            <button class="start-button">시작하기</button>
            <button class="home-button">홈으로</button>
        `;

        content.querySelector('.start-button').onclick = async () => {
            await StartMission.fetchMissionStart(mission.missionId, this.sessionId);
            mission.status = MISSION_STATUS.IN_PROGRESS;
            await this.showMissionDialog(mission);
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

        content.querySelector('.link-button').onclick = async () => {
            await CompleteMission.fetchMissionComplete(mission.missionId, this.sessionId);
            mission.status = MISSION_STATUS.COMPLETED;
            await this.showMissionDialog(mission);
        };

        content.querySelector('.home-button').onclick = () => {
            this.closeDialog();
        };
    }

    async showCompletedDialog(mission, content) {
        content.innerHTML = `
            <h2>${mission.title} 완료!</h2>
            <p>보상: 알 ${mission.rewardEggCount}개</p>
            <button class="home-button">홈으로</button>
        `;

        content.querySelector('.home-button').onclick = () => {
            this.closeDialog();
        };

        for (let i = 0; i < mission.rewardEggCount; i++) {
            await this.eggManager.addEgg();
        }
    }

    closeDialog() {
        if (this.currentDialog) {
            document.body.removeChild(this.currentDialog);
            this.currentDialog = null;
        }
    }
}

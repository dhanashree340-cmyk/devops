const canvas = document.getElementById('gameCanvas');
const ctx = canvas.getContext('2d');
const TILE_SIZE = 20;

const scoreEl = document.getElementById('score');
const livesEl = document.getElementById('lives');
const timerEl = document.getElementById('timer'); // ⏱️ DOM Element
const pauseBtn = document.getElementById('pauseBtn');
const restartBtn = document.getElementById('restartBtn');

// Key Controls
document.addEventListener('keydown', (e) => {
    let action = null;
    if (e.key === 'ArrowUp' || e.key === 'w' || e.key === 'W') action = 'UP';
    if (e.key === 'ArrowDown' || e.key === 's' || e.key === 'S') action = 'DOWN';
    if (e.key === 'ArrowLeft' || e.key === 'a' || e.key === 'A') action = 'LEFT';
    if (e.key === 'ArrowRight' || e.key === 'd' || e.key === 'D') action = 'RIGHT';
    if (e.key === ' ') action = 'PAUSE';

    if (action) sendAction(action);
});

pauseBtn?.addEventListener('click', () => sendAction('PAUSE'));
restartBtn?.addEventListener('click', () => sendAction('RESTART'));

function sendAction(action) {
    fetch('/api/action', {
        method: 'POST',
        body: action
    });
}

// Helper to format seconds into 00:00
function formatTime(seconds) {
    const mins = Math.floor(seconds / 60).toString().padStart(2, '0');
    const secs = (seconds % 60).toString().padStart(2, '0');
    return `${mins}:${secs}`;
}

async function pollGameState() {
    try {
        const response = await fetch('/api/state');
        const state = await response.json();
        render(state);
    } catch (err) {
        console.error("Failed to connect to Java server", err);
    }
}

function render(state) {
    scoreEl.textContent = state.score;
    livesEl.textContent = '❤️'.repeat(Math.max(0, state.lives));
    if (timerEl) timerEl.textContent = formatTime(state.timerSeconds); // ⏱️ Update UI Timer

    // Clear Canvas
    ctx.fillStyle = '#101820';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    // Draw Food
    ctx.fillStyle = '#ff6b81';
    ctx.beginPath();
    ctx.arc(state.food.x * TILE_SIZE + 10, state.food.y * TILE_SIZE + 10, 8, 0, Math.PI * 2);
    ctx.fill();

    // Draw Snake
    state.snake.forEach((segment, index) => {
        ctx.fillStyle = index === 0 ? '#4facfe' : '#00f2fe';
        ctx.fillRect(segment.x * TILE_SIZE, segment.y * TILE_SIZE, TILE_SIZE - 1, TILE_SIZE - 1);
    });

    if (state.isPaused) {
        ctx.fillStyle = 'yellow';
        ctx.font = '20px Arial';
        ctx.fillText('PAUSED', 160, 200);
    }

    if (state.isGameOver) {
        ctx.fillStyle = 'red';
        ctx.font = '24px Arial';
        ctx.fillText('GAME OVER', 130, 200);
    }
}

// Sync loop
setInterval(pollGameState, 50);
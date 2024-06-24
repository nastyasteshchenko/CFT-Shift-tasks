package focus.start.task3.model.listener;

import focus.start.task3.model.GameDifficultyType;

public interface WinEventListener {
    void onGameWin(GameDifficultyType gameDifficultyType);
}

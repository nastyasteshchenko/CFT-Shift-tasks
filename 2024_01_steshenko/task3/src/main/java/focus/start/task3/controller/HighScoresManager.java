package focus.start.task3.controller;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

class HighScoresManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(HighScoresManager.class);

    private static final Path HIGH_SCORES_DIRECTORY = Path.of("high_scores");
    private static final Path HIGH_SCORES_FILE = Path.of("high_scores", "high_scores.json");

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Map<String, Score> scores = new HashMap<>();

    private final InputStream inputStream;

    private HighScoresManager(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    static HighScoresManager create() throws IOException {
        if (!Files.exists(HIGH_SCORES_FILE)) {
            Files.createDirectories(HIGH_SCORES_DIRECTORY);
            Files.createFile(HIGH_SCORES_FILE);
            LOGGER.info("High scores file {} was created.", HIGH_SCORES_FILE.toAbsolutePath());
        }
        try (InputStream inputStream = Files.newInputStream(HIGH_SCORES_FILE)) {
            HighScoresManager highScoresManager = new HighScoresManager(inputStream);
            highScoresManager.scanForScores();
            return highScoresManager;
        }
    }

    void writeHighScore(Score score) {
        if (isNewScore(score.difficulty(), score.score())) {
            scores.put(score.difficulty(), score);
            LOGGER.debug("New high score:{}difficulty: {}{}player name: {}{}score: {}{}was added to high scores.",
                    System.lineSeparator(),
                    score.difficulty(), System.lineSeparator(),
                    score.playerName(), System.lineSeparator(),
                    score.score(), System.lineSeparator());
            storeHighScore();
        }
    }

    boolean isNewScore(String difficulty, int time) {
        Score score = scores.get(difficulty);
        if (score != null) {
            return time < score.score();
        }
        return true;
    }

    Score getScoreForDifficulty(String difficulty) {
        return scores.get(difficulty);
    }

    private void storeHighScore() {
        try (BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE.toAbsolutePath().toString()))) {
            ArrayList<Score> scores = new ArrayList<>(this.scores.values());
            gson.toJson(scores, jsonWriter);
            LOGGER.info("New high score was successfully stored into file {}.", HIGH_SCORES_FILE.toAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Failed to write high scores to file {}.", HIGH_SCORES_FILE.toAbsolutePath());
            LOGGER.error(e.getMessage(), e);
            MessageDialogUtility.showWritingHighScoresError();
        }
    }

    private void scanForScores() throws IOException {
        scores.clear();
        LOGGER.info("Scanning for high scores from {} started.", HIGH_SCORES_FILE.toAbsolutePath());
        try (JsonReader fileReader = new JsonReader(new BufferedReader(new InputStreamReader(inputStream)))) {
            JsonArray ja = gson.fromJson(fileReader, JsonArray.class);
            if (ja == null) {
                return;
            }
            for (JsonElement je : ja) {
                Score score = gson.fromJson(je, Score.class);
                scores.put(score.difficulty(), score);
                LOGGER.debug("Found high score:{}difficulty: {}{}player name: {}{}score: {}",
                        System.lineSeparator(),
                        score.difficulty(), System.lineSeparator(),
                        score.playerName(), System.lineSeparator(),
                        score.score());
            }
        }
        LOGGER.info("Scanning for high scores from {} ended.", HIGH_SCORES_FILE.toAbsolutePath());
    }
}

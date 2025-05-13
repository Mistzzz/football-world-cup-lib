package pl.football.worldcup;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FootballWorldCupScoreBoardTest {

    private static final String HOME_TEAM = "HomeTeam";
    private static final String AWAY_TEAM = "AwayTeam";

    private ScoreBoard scoreBoard;

    @BeforeEach
    void resetState() {
        this.scoreBoard = new FootballWorldCupScoreBoard();
    }

    @Test
    @Order(10)
    void shouldStartMatchSuccessfully() {
        // WHEN
        Long matchId = scoreBoard.startMatch(HOME_TEAM, AWAY_TEAM);

        // THEN
        List<FootballMatch> summary = scoreBoard.getSummaryMatchesByTotalScore();
        assertNotNull(matchId);
        assertEquals(1, summary.size());

        FootballMatch match = summary.get(0);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());

        MatchScore score = match.score();
        assertEquals(0, score.homeScore());
        assertEquals(0, score.awayScore());
        assertEquals(0, match.getTotalScore());
    }

    @Test
    @Order(20)
    void shouldUpdateMatchSuccessfully() {
        // GIVEN
        Long matchId = scoreBoard.startMatch(HOME_TEAM, AWAY_TEAM);

        // WHEN
        boolean successfulUpdate = scoreBoard.updateScore(matchId, new MatchScore(0, 1));

        // THEN
        List<FootballMatch> summaryMatches = scoreBoard.getSummaryMatchesByTotalScore();
        assertTrue(successfulUpdate);
        assertEquals(1, summaryMatches.size());
        FootballMatch match = summaryMatches.get(0);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());

        MatchScore matchScore = match.score();
        assertEquals(0, matchScore.homeScore());
        assertEquals(1, matchScore.awayScore());
        assertEquals(1, match.getTotalScore());
    }

    @Test
    @Order(30)
    void shouldFinishMatchSuccessfully() {
        // GIVEN
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 12, 1, 1, 1, 0);
        Long matchId = scoreBoard.startMatch(HOME_TEAM, AWAY_TEAM, dateTime);

        // WHEN
        scoreBoard.finishMatch(matchId);

        // THEN
        List<FootballMatch> summaryMatchesByTotalScore = scoreBoard.getSummaryMatchesByTotalScore();
        assertEquals(0, summaryMatchesByTotalScore.size());
    }

    @Test
    @Order(40)
    void shouldGetSummaryMatchesByTotalScoreForOneMatch() {
        // GIVEN
        scoreBoard.startMatch(HOME_TEAM, AWAY_TEAM);

        // WHEN
        List<FootballMatch> summaryMatchesByTotalScore = scoreBoard.getSummaryMatchesByTotalScore();

        // THEN
        assertEquals(1, summaryMatchesByTotalScore.size());
        FootballMatch match = summaryMatchesByTotalScore.get(0);
        assertEquals(HOME_TEAM, match.homeTeam());
        assertEquals(AWAY_TEAM, match.awayTeam());
        MatchScore matchScore = match.score();
        assertEquals(0, matchScore.homeScore());
        assertEquals(0, matchScore.awayScore());
        assertEquals(0, match.getTotalScore());
    }

    @Test
    @Order(50)
    void shouldGetSummaryMatchesByTotalScoreForNoMatches() {
        // WHEN
        List<FootballMatch> summaryMatchesByTotalScore = scoreBoard.getSummaryMatchesByTotalScore();

        // THEN
        assertEquals(0, summaryMatchesByTotalScore.size());
    }

    @Test
    @Order(60)
    void shouldGetSummaryMatchesByTotalScoreForMultipleMatches() {
        // GIVEN
        LocalDateTime startTime = LocalDateTime.of(2025, 5, 12, 1, 1, 1, 0);
        System.out.println("\nCurrent data in the system:\n====================================");
        AtomicInteger index = new AtomicInteger(0);

        List<Long> matchIdList = new ArrayList<>();
        matchIdList.add(createMatchWithScore("Mexico", "Canada", startTime.plusSeconds(1), 0, 5, index));
        matchIdList.add(createMatchWithScore("Spain", "Brazil", startTime.plusSeconds(2), 10, 2, index));
        matchIdList.add(createMatchWithScore("Germany", "France", startTime.plusSeconds(3), 2, 2, index));
        matchIdList.add(createMatchWithScore("Uruguay", "Italy", startTime.plusSeconds(4), 6, 6, index));
        matchIdList.add(createMatchWithScore("Argentina", "Australia", startTime.plusSeconds(5), 3, 1, index));

        // WHEN
        List<FootballMatch> summaryMatchesByTotalScore = scoreBoard.getSummaryMatchesByTotalScore();

        System.out.println("\nSummary of games by total score:\n====================================");
        AtomicInteger summaryIndex = new AtomicInteger(1);
        summaryMatchesByTotalScore
                .forEach(match -> System.out.printf("%d. %-10s - %-10s %3d - %-3d%n", summaryIndex.getAndIncrement(), match.homeTeam(),
                        match.awayTeam(), match.score().homeScore(), match.score().awayScore()));

        // THEN
        assertNotNull(summaryMatchesByTotalScore);
        assertEquals(5, matchIdList.size());
        assertEquals(5, summaryMatchesByTotalScore.size());

        // 1
        FootballMatch match = summaryMatchesByTotalScore.get(0);
        assertEquals("Uruguay", match.homeTeam());
        assertEquals("Italy", match.awayTeam());
        assertEquals(12, match.getTotalScore());

        // 2
        match = summaryMatchesByTotalScore.get(1);
        assertEquals("Spain", match.homeTeam());
        assertEquals("Brazil", match.awayTeam());
        assertEquals(12, match.getTotalScore());

        // 3
        match = summaryMatchesByTotalScore.get(2);
        assertEquals("Mexico", match.homeTeam());
        assertEquals("Canada", match.awayTeam());
        assertEquals(5, match.getTotalScore());

        // 4
        match = summaryMatchesByTotalScore.get(3);
        assertEquals("Argentina", match.homeTeam());
        assertEquals("Australia", match.awayTeam());
        assertEquals(4, match.getTotalScore());

        // 5
        match = summaryMatchesByTotalScore.get(4);
        assertEquals("Germany", match.homeTeam());
        assertEquals("France", match.awayTeam());
        assertEquals(4, match.getTotalScore());
    }

    private Long createMatchWithScore(String homeTeam, String awayTeam, LocalDateTime startTime, Integer homeScore, Integer awayScore,
                                      AtomicInteger index) {
        Long matchId = scoreBoard.startMatch(homeTeam, awayTeam, startTime);
        if (scoreBoard.updateScore(matchId, new MatchScore(homeScore, awayScore))) {
            System.out.printf("%s. %-10s - %-10s %3d - %-3d%n", (char) ('a' + index.getAndIncrement()), homeTeam, awayTeam, homeScore, awayScore);

            return matchId;
        } else {
            throw new RuntimeException(MessageFormat.format("Match for ({0},{1}) cannot be updated", homeTeam, awayTeam));
        }
    }
}
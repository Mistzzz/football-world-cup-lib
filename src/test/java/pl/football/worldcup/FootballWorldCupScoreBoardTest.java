package pl.football.worldcup;

import java.util.List;

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

    private MatchStorage matchStorage;
    private MatchFactory matchFactory;
    private ScoreBoard scoreBoard;

    @BeforeEach
    void resetState() {
        this.matchStorage = new InMemoryMatchStorage();
        this.matchFactory = new FootballMatchFactory();
        this.scoreBoard = new FootballWorldCupScoreBoard(this.matchStorage, this.matchFactory);
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
}
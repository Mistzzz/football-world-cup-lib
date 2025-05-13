package pl.football.worldcup;

import java.util.List;
import java.util.stream.Collectors;

import lombok.experimental.UtilityClass;
import pl.football.worldcup.model.FootballMatch;
import pl.football.worldcup.model.MatchScore;

@UtilityClass
class ModelMapper {

    static Score map(MatchScore score) {
        return new Score(score.homeScore(), score.awayScore());
    }

    static MatchScore map(Score score) {
        return new MatchScore(score.homeScore(), score.awayScore());
    }

    static FootballMatch map(Match match) {
        return FootballMatch.builder()
                .id(match.id())
                .homeTeam(match.homeTeam())
                .awayTeam(match.awayTeam())
                .startTime(match.startTime())
                .endTime(match.endTime().orElse(null))
                .score(map(match.score()))
                .build();
    }

    static List<FootballMatch> map(List<Match> match) {
        return match.stream()
                .map(ModelMapper::map)
                .collect(Collectors.toList());
    }
}

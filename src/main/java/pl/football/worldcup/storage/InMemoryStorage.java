package pl.football.worldcup.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import pl.football.worldcup.model.FootballMatch;

@RequiredArgsConstructor
public class InMemoryStorage implements MatchStorage {

    private final Map<Long, FootballMatch> storage;

    public InMemoryStorage() {
        this(new HashMap<>());
    }

    @Override
    public FootballMatch saveMatch(FootballMatch match) {
        return null;
    }

    @Override
    public FootballMatch updateMatch(FootballMatch match) {
        return null;
    }

    @Override
    public FootballMatch getMatch(Long id) {
        return null;
    }

    @Override
    public List<FootballMatch> getAllMatches() {
        return List.of();
    }

    @Override
    public List<FootballMatch> getAllMatchesInProgress() {
        return List.of();
    }
}

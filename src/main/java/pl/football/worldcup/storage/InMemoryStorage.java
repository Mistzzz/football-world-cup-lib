package pl.football.worldcup.storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import lombok.RequiredArgsConstructor;
import pl.football.worldcup.exception.MatchStorageException;
import pl.football.worldcup.model.FootballMatch;

@RequiredArgsConstructor
public class InMemoryStorage implements MatchStorage {

    private final AtomicLong idCounter = new AtomicLong(1);
    private final Map<Long, FootballMatch> storage;

    public InMemoryStorage() {
        this(new HashMap<>());
    }

    @Override
    public FootballMatch saveMatch(FootballMatch match) {
        if (this.storage.containsKey(match.id())) {
            throw new MatchStorageException("Given match already exist in storage");
        }
        FootballMatch footballMatch = match;
        if (match.id() == 0L) {
            footballMatch = new FootballMatch(match, idCounter.getAndIncrement());
        }
        storage.put(footballMatch.id(), footballMatch);

        return footballMatch;
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

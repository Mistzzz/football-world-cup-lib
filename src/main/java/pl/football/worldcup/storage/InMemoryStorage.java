package pl.football.worldcup.storage;

import java.text.MessageFormat;
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
            footballMatch = new FootballMatch(match, this.idCounter.getAndIncrement());
        }
        this.storage.put(footballMatch.id(), footballMatch);

        return footballMatch;
    }

    @Override
    public FootballMatch updateMatch(FootballMatch match) {
        if (this.storage.containsKey(match.id())) {
            return this.storage.replace(match.id(), match);
        } else {
            throw new MatchStorageException("Given match not exist in storage");
        }
    }

    @Override
    public FootballMatch getMatch(Long id) {
        if (!this.storage.containsKey(id)) {
            throw new MatchStorageException(MessageFormat.format("There is no match with id: {0}", id));
        }

        return this.storage.get(id);
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

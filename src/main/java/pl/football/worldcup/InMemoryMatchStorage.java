package pl.football.worldcup;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.football.worldcup.exception.MatchStorageException;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
class InMemoryMatchStorage implements MatchStorage {

    private final AtomicLong idCounter = new AtomicLong(1);
    private final Map<Long, Match> storage;

    public InMemoryMatchStorage() {
        this(new HashMap<>());
    }

    @Override
    public Match saveMatch(Match match) {
        if (match == null) {
            throw new MatchStorageException("Match cannot be null");
        }
        if (this.storage.containsKey(match.id())) {
            throw new MatchStorageException("Given match already exist in storage");
        }
        Match footballMatch = match;
        if (match.id() == 0L) {
            footballMatch = new Match(match, this.idCounter.getAndIncrement());
        }
        this.storage.put(footballMatch.id(), footballMatch);

        return footballMatch;
    }

    @Override
    public Match updateMatch(Match match) {
        if (match == null) {
            throw new MatchStorageException("Match cannot be null");
        } else if (this.storage.containsKey(match.id())) {
            this.storage.put(match.id(), match);

            return match;
        } else {
            throw new MatchStorageException("Given match not exist in storage");
        }
    }

    @Override
    public Match getMatch(Long id) {
        if (!this.storage.containsKey(id)) {
            throw new MatchStorageException(MessageFormat.format("There is no match with id: {0}", id));
        }

        return this.storage.get(id);
    }

    @Override
    public List<Match> getAllMatchesInProgress() {
        return storage.values()
                .stream()
                .filter(match -> match.endTime().isEmpty())
                .collect(Collectors.toList());
    }
}

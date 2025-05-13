package pl.football.worldcup.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MatchValidator {

    private static final String TEAM_NAME_PATTERN = "^\\w{3,}$";

    public boolean isIncorrectName(String name) {
        return name == null || !name.matches(TEAM_NAME_PATTERN);
    }
}

package pl.jakubkonkol.tasteitserver.service;

import pl.jakubkonkol.tasteitserver.data.BadgeData;
import pl.jakubkonkol.tasteitserver.model.User;
import pl.jakubkonkol.tasteitserver.model.value.BadgeBlueprint;

import java.util.List;

public class NewBadgeService {


    public void computeBadges(User user) {
        BadgeDataCollector badgeDataCollector = new BadgeDataCollector(user);
        List<BadgeBlueprint> badgeBlueprintData = BadgeData.badgeBlueprintData;
        for (BadgeBlueprint badgeBlueprintDatum : badgeBlueprintData) {
            int value = badgeBlueprintDatum.countValue(badgeDataCollector);

        }
    }
}

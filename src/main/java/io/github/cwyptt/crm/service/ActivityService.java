package io.github.cwyptt.crm.service;

import io.github.cwyptt.crm.entity.Activity;
import io.github.cwyptt.crm.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;

    public void recordActivity(String action, String entityType, Long entityId, String description) {
        Activity activity = new Activity();
        activity.setAction(action);
        activity.setEntityType(entityType);
        activity.setEntityId(entityId);
        activity.setDescription(description);
        activityRepository.save(activity);
    }

    public List<Activity> getRecentActivities() {
        return activityRepository.findTop20ByOrderByTimestampDesc();
    }
}

package com.truemen.api.activity.controller;

import com.truemen.api.activity.model.Activity;
import com.truemen.api.activity.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/activities")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @PostMapping
    public ResponseEntity<Activity> createActivity(@RequestBody Activity activity) {
        try {
            Activity createdActivity = activityService.createActivity(activity);
            return ResponseEntity.ok(createdActivity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/{activityId}")
    public ResponseEntity<Activity> getActivityById(@PathVariable("activityId") Long activityId) {
        Activity activity = activityService.getActivityById(activityId);
        return activity != null ? ResponseEntity.ok(activity) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Activity>> getAllActivities() {
        List<Activity> activities = activityService.getAllActivities();
        return activities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(activities);
    }

    @GetMapping("/landmark/{landmarkId}")
    public ResponseEntity<List<Activity>> getActivitiesByLandmarkId(@PathVariable Long landmarkId) {
        List<Activity> activities = activityService.getActivitiesByLandmarkId(landmarkId);
        return activities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(activities);
    }

    @GetMapping("/creator/{createdBy}")
    public ResponseEntity<List<Activity>> getActivitiesByCreatedBy(@PathVariable String createdBy) {
        List<Activity> activities = activityService.getActivitiesByCreatedBy(createdBy);
        return activities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(activities);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Activity>> searchActivitiesByKeyword(@RequestParam String keyword) {
        List<Activity> activities = activityService.searchActivitiesByKeyword(keyword);
        return activities.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(activities);
    }

    @PutMapping("/{activityId}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long activityId, @RequestBody Activity updatedActivity) {
        try {
            Activity activity = activityService.updateActivity(activityId, updatedActivity);
            return activity != null ? ResponseEntity.ok(activity) : ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId) {
        boolean isDeleted = activityService.deleteActivity(activityId);
        return isDeleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}

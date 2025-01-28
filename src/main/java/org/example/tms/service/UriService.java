package org.example.tms.service;

import java.util.UUID;

public interface UriService {
    String createCommentUri(UUID id);

    String createTaskUri(UUID id);

    String createTaskAssigneeUri(UUID id);
}

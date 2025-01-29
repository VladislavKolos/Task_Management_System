package org.example.tms.service.impl;

import org.example.tms.service.UriService;
import org.example.tms.util.ConstantUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Service
public class UriServiceImpl implements UriService {
    @Override
    public String createCommentUri(UUID id) {
        return UriComponentsBuilder.fromPath(ConstantUtil.COMMENTS_PATH)
                .buildAndExpand(id)
                .toUriString();
    }

    @Override
    public String createTaskUri(UUID id) {
        return UriComponentsBuilder.fromPath(ConstantUtil.TASKS_PATH)
                .buildAndExpand(id)
                .toUriString();
    }

    @Override
    public String createTaskAssigneeUri(UUID id) {
        return UriComponentsBuilder.fromPath(ConstantUtil.TASK_ASSIGNEES_PATH)
                .buildAndExpand(id)
                .toUriString();
    }
}

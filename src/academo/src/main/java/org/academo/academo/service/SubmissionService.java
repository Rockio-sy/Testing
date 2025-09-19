package org.academo.academo.service;

import org.academo.academo.dto.SubmissionDTO;

import java.util.List;
import java.util.UUID;

public interface SubmissionService {
    void submit(SubmissionDTO submission);

    SubmissionDTO getByTaskId(UUID taskId);

    List<SubmissionDTO> getAll();

    SubmissionDTO getOneById(UUID id);

}

package org.tcpri.workspace.post;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SocialPostRepository extends JpaRepository<SocialPost, Long> {
    List<SocialPost> findByCreatedBy_EmailIgnoreCaseOrderByPlannedAtAsc(String email);
    List<SocialPost> findAllByOrderByPlannedAtAsc();
}

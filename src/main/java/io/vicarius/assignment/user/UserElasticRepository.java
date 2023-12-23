package io.vicarius.assignment.user;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;
public interface UserElasticRepository extends ElasticsearchRepository<UserDocument, String> {
    List<UserDocument> findAll();
}

package io.bookreader.author;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Service;



@Service
public interface AuthorRepository extends CassandraRepository<Author, String> {

}

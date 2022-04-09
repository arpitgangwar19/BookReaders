package io.bookreader.book;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Service;


@Service
public interface BookRepository extends CassandraRepository<Book, String> {

}
